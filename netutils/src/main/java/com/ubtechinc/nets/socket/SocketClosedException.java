package com.ubtechinc.nets.socket;

import java.net.SocketException;

public class SocketClosedException extends SocketException {

  private static final long serialVersionUID = -6208848522563216081L;

  public SocketClosedException(String msg) {
    super(msg);
  }
}
