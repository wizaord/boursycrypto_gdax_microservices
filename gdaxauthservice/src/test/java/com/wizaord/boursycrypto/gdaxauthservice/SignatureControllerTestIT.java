package com.wizaord.boursycrypto.gdaxauthservice;

import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureHeader;
import com.wizaord.boursycrypto.gdaxauthservice.beans.SignatureRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignatureControllerTestIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void generateSignature() {
    // given
    SignatureRequest signReq = new SignatureRequest("/myPath/plop", "POST", "my body content {}");

    // When
    final ResponseEntity<SignatureHeader> signatureHeaderResponseEntity = this.restTemplate
            .postForEntity("/signature", signReq, SignatureHeader.class);

    // Then
    assertThat(signatureHeaderResponseEntity).isNotNull();
    assertThat(signatureHeaderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    final SignatureHeader signatureHeader = signatureHeaderResponseEntity.getBody();
    assertThat(signatureHeader).isNotNull();
    assertThat(signatureHeader.getCbAccessKey()).isEqualTo("myApiKey");
    assertThat(signatureHeader.getCbAccessPassphrase()).isEqualTo("myPassphrase");
    assertThat(signatureHeader.getCbAccessSign()).isNotNull();
    assertThat(signatureHeader.getCbAccessTimestamp()).isNotNull();
  }

  @Test
  public void generateSignature_when_bodyIsMissing_then_return_a_siganture() {
    // given
    SignatureRequest signReq = new SignatureRequest("/myPath/plop", "POST", null);

    // When
    final ResponseEntity<SignatureHeader> signatureHeaderResponseEntity = this.restTemplate
            .postForEntity("/signature", signReq, SignatureHeader.class);

    // Then
    assertThat(signatureHeaderResponseEntity).isNotNull();
    assertThat(signatureHeaderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

  }
}