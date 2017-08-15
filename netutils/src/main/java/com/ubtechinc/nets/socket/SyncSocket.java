package com.ubtechinc.nets.socket;

import android.os.SystemClock;


import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.NetworkUtils;
import com.ubtechinc.nets.utils.BufferAllocator;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

import timber.log.Timber;

/**
 * @desc : socket
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public class SyncSocket {
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

    private SocketChannel mChannel;

    private Selector mSelector;

    private InetSocketAddress mSocketAddress;

    private int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;

    private BufferAllocator mAllocator;

    public SyncSocket() {
        mAllocator = new BufferAllocator();
    }

    public void connect(final InetSocketAddress ip)
            throws DNSException, IOException, ConnectTimeoutException {
        if (ip.isUnresolved()) {
            String host = ip.getHostName();
            try {
                InetAddress[] addrsss = InetAddress.getAllByName(host);
                if (addrsss == null || addrsss.length == 0) {
                    throw new DNSException("no addresses for host");
                }

                mSocketAddress = new InetSocketAddress(addrsss[0], ip.getPort());
            } catch (UnknownHostException e) {

            }
        } else {
            mSocketAddress = ip;
        }
        connectSocket();
    }

    public void connect(String host, final int port)
            throws DNSException, IOException, ConnectTimeoutException {
        InetAddress[] address = null;
        try {
            long elapsedTime = SystemClock.elapsedRealtime();
            //DNS lookup
            address = InetAddress.getAllByName(host);
            Timber.d("NetState=%s, DNS lookup time=%dms",
                    NetworkUtils.getNetworkType().name(),
                    (SystemClock.elapsedRealtime() - elapsedTime));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (address == null || address.length == 0) {
            throw new DNSException("no addresses for host");
        }
        mSocketAddress = new InetSocketAddress(address[0], port);
        connectSocket();
    }

    public int getConnTimeout() {
        return mConnectTimeout;
    }

    public void setConnTimeout(int timeout) {
        this.mConnectTimeout = timeout;
    }

    public void disconnect() {
        if (mChannel == null && mSelector == null) {
            return;
        }
        closeQuietly(mChannel);
        shutdownKeys(mSelector);
        mSelector = null;
        mChannel = null;
    }

    public boolean isConnected() {
        return mChannel != null && mChannel.isConnected();
    }

    public void write(ByteBufferList list)
            throws NotYetConnectedException, SocketClosedException {
        assert mChannel != null;

        if (!isConnected()) {
            list.recycle();
            throw new NotYetConnectedException();
        }

        int before = list.remaining();
        int ret = 0;
        IOException ioe = null;
        do {
            ByteBuffer[] arr = list.getAllArray();
            try {
                ret = (int) mChannel.write(arr);
                Timber.v("mChannel has writen bytes = %d", ret);
            } catch (IOException e) {
                ioe = e;
            } finally {
                if (ret != before) list.addAll(arr);
            }

            if(ret == -1){
                throw new SocketClosedException("server socket closed!");
            }

            if (ret == before) {// success
                break;
            }

            if (ioe != null) {// fail
                throw new SocketClosedException("server socket closed!");
            }

            //部分写入，可能通道已塞满，休眠一段时间再写入
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        } while ((before = list.remaining()) != 0);
    }

    public void read(ByteBufferList bbList) throws SocketClosedException {
        assert mChannel != null && bbList != null;

        if (!isConnected()) {
            throw new NotYetConnectedException();
        }

        long read = 0;

        boolean closed = false;
        IOException ioe = null;

        ByteBuffer bb = mAllocator.allocate();
        try {
            read = mChannel.read(bb);
        } catch (IOException e) {
            ioe = e;
        }

        if (read < 0) {
            closed = true;
        }

        if (read > 0) {
            bb.flip();
            bbList.add(bb);
        } else {
            ByteBufferList.reclaim(bb);
        }

        if (closed) {
            throw new SocketClosedException("server socket closed!");
        }

        if (ioe != null) {
            throw new SocketClosedException("server socket closed!");
        }
    }

    //TCP handshake
    private void connectSocket()
            throws IOException, DNSException, ConnectTimeoutException {
        assert mChannel == null && mSelector == null;
        mChannel = SocketChannel.open();
        mChannel.configureBlocking(false);
        mSelector = SelectorProvider.provider().openSelector();
        mChannel.register(mSelector, SelectionKey.OP_CONNECT);
        long elapsedTime = SystemClock.elapsedRealtime();
        mChannel.connect(mSocketAddress);
        finishConnect();
        Timber.d("NetState=%s, TCP handsake time=%dms",
                NetworkUtils.getNetworkType().name(),
                (SystemClock.elapsedRealtime() - elapsedTime));
    }

    //TCP handshake
    private void finishConnect() throws IOException, ConnectTimeoutException {
        int timeout = 0;
        final int timeSlice = mConnectTimeout / 500;
        for (; ; ) {
            mSelector.select(timeSlice);
            Set<SelectionKey> readyKeys = mSelector.selectedKeys();
            Iterator<SelectionKey> iter = readyKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isConnectable()) {
                    if (mChannel.isConnectionPending()) mChannel.finishConnect();
                    mChannel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    return;
                }
            }
            timeout += timeSlice;
            if (timeout >= mConnectTimeout) break;
        }
        throw new ConnectTimeoutException("socket connect timeout:" + mConnectTimeout + "ms");
    }

    private static void closeQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void shutdownKeys(Selector selector) {
        try {
            for (SelectionKey key : selector.keys()) {
                closeQuietly(key.channel());
                try {
                    key.cancel();
                } catch (Exception e) {
                }
            }
        } catch (Exception ex) {
        }
    }
}
