package com.wizaord.boursycrypto.gatewaywebsocket;

import com.wizaord.boursycrypto.gatewaywebsocket.beans.SignatureHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SignatureServiceTest {


  private SignatureService signatureService;

  @Before
  public void initProperties() {
    signatureService = new SignatureService() {{
      setApiKey("myApiKey");
      setPassphrase("myPassPhrase");
      setSecretApiKey("myApiSecretKey");
    }};
  }

  @Test
  public void generatecbAccessSign_when_parameters_are_ok_then_signature_is_the_same() {
    // given
    final String timestamp = "1554285306";

    // When
    final String cbAccessSign = signatureService.generateCbAccessSign("/TOTO", "GET", null, timestamp);

    // Then
    assertThat(cbAccessSign).isNotNull().isNotEmpty().isEqualTo("kD2QAiIMTZoWFZ75dYCLV298Nv25U0oD1iPKoJVK6SY=");
  }

  @Test
  public void getSignature_when_parameters_are_ok_then_return_signatureHeader_with_all_parameters_set() {
      // given
    final String path = "/TOTO";
    final String verb = "GET";
    final String body = "My message";

      // When
    final SignatureHeader signatureHeader = signatureService.getSignature(path, verb, body);

    // Then
    assertThat(signatureHeader).isNotNull();
    assertThat(signatureHeader.getCbAccessKey()).isEqualTo("myApiKey");
    assertThat(signatureHeader.getCbAccessPassphrase()).isEqualTo("myPassPhrase");
    assertThat(signatureHeader.getCbAccessSign()).isNotNull();
    assertThat(signatureHeader.getCbAccessTimestamp()).isNotNull().isNotEmpty();
  }

}