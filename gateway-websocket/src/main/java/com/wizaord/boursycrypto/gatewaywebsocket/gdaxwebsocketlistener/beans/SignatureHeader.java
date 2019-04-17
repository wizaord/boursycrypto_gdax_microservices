package com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.beans;

import lombok.Value;

@Value
public class SignatureHeader {

  private String cbAccessKey;
  private String cbAccessSign;
  private String cbAccessTimestamp;
  private String cbAccessPassphrase;

}
