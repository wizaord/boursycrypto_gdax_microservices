package com.wizaord.boursycrypto.gatewaywebsocket.rabbitsender;

import com.wizaord.boursycrypto.gatewaywebsocket.config.RabbitMQConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class RabbitSenderTest {

  @Mock
  RabbitTemplate rabbitTemplate;

  @InjectMocks
  RabbitSender rabbitSender;

  @Test
  public void sendMessage_when_send_message_then_message_is_sent() {
      // given
    final String myMessage = "myMessage";

      // When
    rabbitSender.sendMessage(myMessage);

      // Then
    Mockito.verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfiguration.RECEIVE_EVENT_EXCHANGE_NAME), anyString(), eq(myMessage));
  }
}