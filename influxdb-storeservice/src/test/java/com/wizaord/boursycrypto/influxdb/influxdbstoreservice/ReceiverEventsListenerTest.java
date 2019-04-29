package com.wizaord.boursycrypto.influxdb.influxdbstoreservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizaord.boursycrypto.influxdb.influxdbstoreservice.beans.GenericFeedMessage;
import com.wizaord.boursycrypto.influxdb.influxdbstoreservice.beans.Ticker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReceiverEventsListenerTest {

  @Mock
  ObjectMapper objectMapper;

  @Mock
  TickerService ticketService;

  @InjectMocks
  ReceiverEventsListener receiverEventsListener;


  @Test
  public void receiveMessage_when_message_is_ticker_then_call_ticker_service() throws IOException {
    // given
    String tickerMessage = "{\"type\":\"ticker\",\"sequence\":5261096257,\"product_id\":\"BTC-EUR\",\"price\":\"4611.00000000\",\"open_24h\":\"4622.47000000\",\"volume_24h\":\"984.84156246\",\"low_24h\":\"4553.34000000\",\"high_24h\":\"4649.98000000\",\"volume_30d\":\"45256.95033911\",\"best_bid\":\"4610.95\",\"best_ask\":\"4611\",\"side\":\"buy\",\"time\":\"2019-04-29T19:56:33.458000Z\",\"trade_id\":18840360,\"last_size\":\"0.01826233\"}";
    when(objectMapper.readValue(tickerMessage, GenericFeedMessage.class)).thenReturn(new GenericFeedMessage() {{
      setType("ticker");
    }});
    when(objectMapper.readValue(tickerMessage, Ticker.class)).thenReturn(new Ticker());
    // When
    receiverEventsListener.receiveMessage(tickerMessage);

    // Then
    verify(ticketService, times(1)).storeTicker(any(Ticker.class));
  }

  @Test
  public void receiveMessage_when_message_is_not_ticker_then_do_nothing() throws IOException {
    String tickerMessage = "{\"type\":\"Notticker\",\"sequence\":5261096257,\"product_id\":\"BTC-EUR\",\"price\":\"4611.00000000\",\"open_24h\":\"4622.47000000\",\"volume_24h\":\"984.84156246\",\"low_24h\":\"4553.34000000\",\"high_24h\":\"4649.98000000\",\"volume_30d\":\"45256.95033911\",\"best_bid\":\"4610.95\",\"best_ask\":\"4611\",\"side\":\"buy\",\"time\":\"2019-04-29T19:56:33.458000Z\",\"trade_id\":18840360,\"last_size\":\"0.01826233\"}";
    when(objectMapper.readValue(tickerMessage, GenericFeedMessage.class)).thenReturn(new GenericFeedMessage() {{
      setType("notticker");
    }});
    // When
    receiverEventsListener.receiveMessage(tickerMessage);

    // Then
    verify(ticketService, times(0)).storeTicker(any());
  }
}