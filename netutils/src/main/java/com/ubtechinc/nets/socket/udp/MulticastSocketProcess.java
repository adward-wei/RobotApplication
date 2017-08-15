package com.ubtechinc.nets.socket.udp;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


/**
 * 与PC连接的组播处理类
 *
 * @author UBX-dean
 *
 */
public class MulticastSocketProcess {
	private static final int ROBOT_STATE_IDLE = 10000;
	private static final int ROBOT_STATE_BUSY = 10001;

	private static MulticastSocketProcess sMulticastSocketProcess;



//	private GetActionFileList mActions; //动作文件列表

	/**
	 * 调用者的CONTEXT
	 */
	private Context mContext;
	/**
	 * 停止处理
	 */
	private boolean mStopProcess;

	/**
	 * 组播IP地址
	 */
	private String mGroupIpAddress;

	/**
	 * 组播端口
	 */
	private int mGroupPort;

	MulticastSocketRcv mRcv;
	BroadCastThread mBroadThread;
	private long mStartTime = 0;
	private long mStopTime = 0;

	// 机器人序列号
	private String sid;

	// IM忙碌状态
	private boolean isIMBusy;

	public String getmGroupIpAddress() {
		return mGroupIpAddress;
	}

	public void setmGroupIpAddress(String mGroupIpAddress) {
		this.mGroupIpAddress = mGroupIpAddress;

		// mRcv.releaseConnection();

		// mRcv = new MulticastSocketRcv();
		// mRcv.start();
	}


	class BroadCastThread extends Thread {
//		private boolean mStop;
//		private WifiControl mWifiControl;
//
//		public BroadCastThread(Context context) {
//			mWifiControl = WifiControl.get(context);
//		}
//
//		public void startRun() {
//			mStop = false;
//			this.start();
//		}
//
//		public void stopRun() {
//			mStop = true;
//			this.interrupt();
//		}
//
//		@Override
//		public void run() {
//			while (mStop == false) {
//				try {
//					Thread.sleep(3000);
//					if (mWifiControl.isWifiConnect()) {
//						broadCastDevice();
//					} else {
//						// break;
//					}
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					break;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	class MulticastSocketRcv extends Thread {
		private InetAddress group = null;
		private MulticastSocket socket = null;

		// private DatagramSocket socketUdp;

		public MulticastSocketRcv() {
			try {
				group = InetAddress.getByName(mGroupIpAddress);
				//socket = new MulticastSocket();
				socket = new MulticastSocket(mGroupPort+2);
				socket.joinGroup(group);
				// socketUdp = new DatagramSocket(mGroupPort);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}

		public void releaseConnection() {
			socket.close();
			// socketUdp.close();
		}

		/**
		 * 解析命令
		 *
		 * @param msg
		 */
		void parseMsg(byte[] msg) {
			if (msg == null)
				return;

//			try {
//				HeaderCmd cmd = MessagePack.unpack(msg, HeaderCmd.class);
//				switch (cmd.getMsgID()) {
//				case MulticastCmdId.CMD_GET_DEV_INFO:
//					broadCastDevice();
//					break;
//				case MulticastCmdId.CMD_CTRL_APP_ROBOT:
//					long time = System.currentTimeMillis();
//					if((time - mStartTime) > 2000) {
//						mStartTime = time;
//						CtrlAllRobotInfo ctrlAllRobotInfo = MessagePack.unpack(cmd.getMsgBuf(),
//								CtrlAllRobotInfo.class);
//						String actionName = mActions.getFileList().get(Integer.parseInt(ctrlAllRobotInfo.getActionName()));
//						//mActionServerUtil.playActionName(actionName, null);
//						LogUtils.d("udpbrocast","udp play action:"+actionName);
//						//broadcastAction(ctrlAllRobotInfo);
//						broadcastPlayAction(actionName);
//					} else {
//
//					}
//					break;
//				}
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}


//		void broadcastPlayAction(String actionName) {
//			Intent intent2 = new Intent(StaticValue.ALPHA_MSG_PLAY_ACTION);
//			intent2.putExtra("fileName", actionName);
//			mContext.sendBroadcast(intent2);
//		}

		void broadcastPlayAction(String actionName) {
//			Intent intent2 = new Intent(StaticValue.ALPHA_MSG_PLAY_ACTION);
//			intent2.putExtra("fileName", actionName);
//			mContext.sendBroadcast(intent2);
		}


		@Override
		public void run() {
			byte[] data = null;
			while (!mStopProcess) {
				Log.d("", "!!!!!! ");
				data = new byte[1000];
				DatagramPacket packet = null;
				packet = new DatagramPacket(data, data.length
															 , group,
															 mGroupPort
															 );

				try {
					socket.receive(packet);
					// socketUdp.receive(packet);



//					Header header = new Header();
//					InputStream sbs = new ByteArrayInputStream(packet.getData());
//					// Log.v("chenlin", new String(packet.getData()));
//					int nRet = header
//							.readSocketInputStream(new DataInputStream(sbs));
//
//					if (nRet == -3)
//						return;
//
//					if (nRet != 1)
//						continue;
//
//					parseMsg(header.getMsg());


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}catch (Exception e){
					e.printStackTrace();
					break;
				}

//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					break;
//				}catch (Exception e){
//					e.printStackTrace();
//					break;
//				}

			}
		}
	}

	/**
	 * 构造函数
	 *
	 * @param context
	 * @param addr
	 * @param port
	 */
	private MulticastSocketProcess(Context context, String addr, int port) {


//		mContext = context;
//		mStopProcess = false;
//		mGroupIpAddress = addr;
//		mGroupPort = port;
//
//		mRcv = new MulticastSocketRcv();
//		mBroadThread = new BroadCastThread(context);

//		mActions = new GetActionFileList();
	}

	public static MulticastSocketProcess get(Context context, String addr, int port) {
		if(sMulticastSocketProcess == null) {
			synchronized (MulticastSocketProcess.class) {
				if(sMulticastSocketProcess == null)
					sMulticastSocketProcess = new MulticastSocketProcess(context, addr, port);
			}
		}

		return sMulticastSocketProcess;
	}

	public void stopProcess() {
		mStopProcess = true;
		mRcv.releaseConnection();

		stopBroadCastThread();
	}

	public void startProcess() {
		mStopProcess = false;
		mRcv.start();
		// mBroadThread.start();

		startBroadCastThread();
	}

	public void startBroadCastThread() {
//		mBroadThread.stopRun();
//		mBroadThread.startRun();
	}

	public void stopBroadCastThread() {
//		mBroadThread.stopRun();
	}

	/**
	 * 广播设备信息
	 */
	public void broadCastDevice() {
		new Thread() {

			@Override
			public void run() {
				// for(int i=0; i<3; i++){
				InetAddress group = null;
				MulticastSocket socket = null;

				try {
					group = InetAddress.getByName(mGroupIpAddress);
					socket = new MulticastSocket();
					socket.setTimeToLive(1);
					socket.joinGroup(group);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					// return;
				}

//				DeviceInfo info = new DeviceInfo(mContext);
//
//				try {
//					//若没有序列号则不发送udp数据
//					if (!TextUtils.isEmpty(sid)) {
//
////						info.setRobotNo(AlphaMainSeviceImpl.serialNumber);
//						info.setRobotNo(getSerialAndStatus());
//
//						byte[] bys = MessagePack.pack(info);
//						HeaderCmd headcmd = new HeaderCmd(
//								MulticastCmdId.CMD_RSP_DEV_INFO, bys);
//						byte[] byss = MessagePack.pack(headcmd);
//
//						// 组包
//						PacketData packetData = new PacketData(4);
//						// 包标志
//						packetData.putShort(StaticValue.SOCKET_FLAG);
//						// 长度
//						packetData.putInt(byss.length + 4);
//						// 通信版本号
//						packetData.putShort(StaticValue.SOCKET_VERSION);
//						// 辅助信息
//						packetData.putShort((short) 0);
//						// 消息内容
//						packetData.putBytes(byss);
//
//						byte[] b = packetData.getBuffer();
//						DatagramPacket packet = null;
//
//						packet = new DatagramPacket(b, b.length, group,
//								mGroupPort);
//						socket.send(packet);
//					}
//					Thread.sleep(100);
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				// }
			}

		}.start();
	}

	private String getSerialAndStatus(){
		JSONObject object = new JSONObject();
		try {
			object.put("serialNumber",sid);
			if(isIMBusy){
				object.put("status", ROBOT_STATE_BUSY);
			}else{
				object.put("status", ROBOT_STATE_IDLE);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}


	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public boolean isIMBusy() {
		return isIMBusy;
	}

	public void setIMBusy(boolean IMBusy) {
		isIMBusy = IMBusy;
	}
}
