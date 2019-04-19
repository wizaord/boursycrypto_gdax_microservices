package com.wizaord.boursycrypto.influxdb.influxdbstoreservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverEventsListener {

  private static final Logger LOG = LoggerFactory.getLogger(ReceiverEventsListener.class);

  @RabbitListener(queues = "#{influxDbQueue.name}")
  public void receiveMessage(final String message) {
    LOG.info("Receive message => {}", message);
  }
}
