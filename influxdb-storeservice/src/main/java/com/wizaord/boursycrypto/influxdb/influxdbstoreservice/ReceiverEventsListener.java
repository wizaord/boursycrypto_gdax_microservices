package com.wizaord.boursycrypto.influxdb.influxdbstoreservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizaord.boursycrypto.influxdb.influxdbstoreservice.beans.GenericFeedMessage;
import com.wizaord.boursycrypto.influxdb.influxdbstoreservice.beans.Ticker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ReceiverEventsListener {

  private ObjectMapper jsonMapper;
  private TickerService tickerService;

  @RabbitListener(queues = "#{influxDbQueue.name}")
  public void receiveMessage(final String message) {
    log.debug("Receive message => {}", message);

    // try to convert in Ticker
    try {
      final GenericFeedMessage feedMessage = jsonMapper.readValue(message, GenericFeedMessage.class);
      log.debug("Parse message in JSON object with type {}", feedMessage.getType());

      if (feedMessage.getType().equals("ticker")) {
        final Ticker ticker = jsonMapper.readValue(message, Ticker.class);
        tickerService.storeTicker(ticker);
      }
    }
    catch (IOException e) {
      log.error("Unable to parse input String in JSON message", e);
    }

  }
}
