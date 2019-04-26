package com.wizaord.boursycrypto.gdaxauthservice.beans;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SignatureRequest {
  private String path;
  private String methodType;
  private String bodyContent;
}
