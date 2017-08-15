package com.ubtechinc.nets.socket;

import java.net.SocketException;

public class SocketTimeoutException extends SocketException {

  private static final long serialVersionUID = 8888274175776727969L;

  public SocketTimeoutException() {
    super();
  }

  public SocketTimeoutException(String arg0) {
    super(arg0);
  }
}
