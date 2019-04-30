package com.wizaord.boursycrypto.influxdb.influxdbstoreservice;

import com.wizaord.boursycrypto.influxdb.influxdbstoreservice.beans.Ticker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class TickerService {

  private InfluxDB influxDB;

  public void storeTicker(final Ticker tickerMessage) {
    Point tickerPoint = Point.measurement(tickerMessage.getProductId())
            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .addField("product_id", tickerMessage.getProductId())
            .addField("type", "ticker")
            .addField("price", tickerMessage.getPrice())
            .build();

    influxDB.write(tickerPoint);
    log.info("Successfully store point message for {} in influxDb", tickerMessage.getProductId());
  }
}
