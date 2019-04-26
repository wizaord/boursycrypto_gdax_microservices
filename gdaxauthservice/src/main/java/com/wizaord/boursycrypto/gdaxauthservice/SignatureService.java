package com.wizaord.boursycrypto.gdaxauthservice;

import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureHeader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Component
@Setter
@Slf4j
public class SignatureService {

  @Value("${gdax.auth.apisecretkey}")
  private String secretApiKey;

  @Value("${gdax.auth.apikey}")
  private String apiKey;

  @Value("${gdax.auth.passphrase}")
  private String passphrase;

  protected String generateCbAccessSign(String requestPath, String method, String body, String timestamp) {
    log.debug("Generate Signature service with body : {}", body);

    if (body == null) {
      body = "";
    }

    try {
      log.debug("timestamp : {}", timestamp);
      log.debug("method : {}", method.toUpperCase());
      log.debug("requestPath : {}", requestPath);
      log.debug("body : {}", body);

      String prehash = timestamp + method.toUpperCase() + requestPath + body;
      byte[] secretDecoded = Base64.getDecoder().decode(secretApiKey);
      SecretKeySpec keyspec = new SecretKeySpec(secretDecoded, "HmacSHA256");

      Mac sha256 = Mac.getInstance("HmacSHA256");
      sha256.init(keyspec);

      return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes(StandardCharsets.UTF_8)));
    } catch (InvalidKeyException e) {
      log.error("Cannot set up authentification header", e);
      throw new RuntimeErrorException(new Error("Cannot set up authentication headers."));
    }
    catch (NoSuchAlgorithmException e) {
      log.error("Algorithme HmacSHA256 error", e);
      throw new RuntimeErrorException(new Error("Algorithme HmacSHA256 not implemented"));
    }
  }

  public SignatureHeader getSignature(final String requestPath, final String methodType, final String messageContent) {
    final String timestamp = String.valueOf(Instant.now().getEpochSecond());
    return new SignatureHeader(apiKey,
            this.generateCbAccessSign(requestPath, methodType, messageContent, timestamp),
            timestamp,
            passphrase);
  }
}
