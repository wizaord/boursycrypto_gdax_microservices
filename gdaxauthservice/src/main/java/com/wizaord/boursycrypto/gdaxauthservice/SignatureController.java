package com.wizaord.boursycrypto.gdaxauthservice;

import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureHeader;
import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class SignatureController {

  private static final Logger LOG = LoggerFactory.getLogger(SignatureController.class);
  SignatureService signatureService;

  @PostMapping("/signature")
  SignatureHeader getSignature(@RequestBody final SignatureRequest signatureRequest) {
    LOG.debug("Call get signature on path {} with body {}", signatureRequest.getPath(), signatureRequest.getBodyContent());
    final SignatureHeader signature = signatureService
            .getSignature(signatureRequest.getPath(), signatureRequest.getMethodType(), signatureRequest.getBodyContent());

    return signature;
  }

}
