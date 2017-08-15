package com.ubtechinc.nets.socket;

import java.util.concurrent.TimeoutException;

public class ConnectTimeoutException extends TimeoutException {
  private static final long serialVersionUID = 6537563369185579305L;

  public ConnectTimeoutException(String msg) {
    super(msg);
  }
}
