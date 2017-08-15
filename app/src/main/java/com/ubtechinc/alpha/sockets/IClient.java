package com.ubtechinc.alpha.sockets;

import java.io.IOException;

public interface IClient {

	public void sendMsgData(byte[] msgData) throws IOException;

	public byte[] readData() throws IOException;

	public void close();

	public boolean isClosed();

	public boolean isConnected();

	public void setKeepAlive(boolean isAlive);

	public void releaseWriteConnection();

	public void releaseReadConnection();

}
