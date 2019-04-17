package com.wizaord.boursycrypto.gatewaywebsocket.rabbitsender;

import com.wizaord.boursycrypto.gatewaywebsocket.config.RabbitMQConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RabbitSender {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(final String myMessage) {
    rabbitTemplate.convertAndSend(RabbitMQConfiguration.RECEIVE_EVENT_EXCHANGE_NAME, "", myMessage);
  }
}
