package com.wizaord.boursycrypto.influxdb.influxdbstoreservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class JsonConfiguration {

  @PostConstruct
  public void log() {
    log.info("JSON Configuration : Successfully loaded");
  }

  @Bean
  public ObjectMapper jsonMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule timeModule = new JavaTimeModule();
    timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    objectMapper.registerModule(timeModule);
    return objectMapper;
  }

}
