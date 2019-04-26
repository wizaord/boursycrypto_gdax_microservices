package com.wizaord.boursycrypto.gdaxauthservice;

import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureHeader;
import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class SignatureController {

  private SignatureService signatureService;

  @PostMapping("/signature")
  SignatureHeader getSignature(@RequestBody final SignatureRequest signatureRequest) {
    log.debug("Call get signature on path {} with body {}", signatureRequest.getPath(), signatureRequest.getBodyContent());
    final SignatureHeader signature = signatureService
            .getSignature(signatureRequest.getPath(), signatureRequest.getMethodType(), signatureRequest.getBodyContent());

    return signature;
  }

}
