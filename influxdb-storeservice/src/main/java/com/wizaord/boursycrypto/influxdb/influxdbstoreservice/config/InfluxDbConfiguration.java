package com.wizaord.boursycrypto.influxdb.influxdbstoreservice.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class InfluxDbConfiguration {

  private InfluxDB influxDB;

  @PostConstruct
  public void initInfluxDbPolicies() {
    influxDB.enableBatch(100, 200, TimeUnit.MILLISECONDS);
    influxDB.setRetentionPolicy("autogen");
    influxDB.setDatabase("db0");
  }

  @PreDestroy
  public void disablePatch() {
    influxDB.disableBatch();
  }
}
