package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.time.Duration;
import java.util.Map;

public class ProducerConfig {

  private final String name;

  private final String topic;

  private final Duration productionInterval;

  private final Integer payloadBytes;

  private final Map<String, String> producerProperties;

  public ProducerConfig(String name, String topic, Duration productionInterval, Integer payloadBytes, Map<String, String> producerProperties) {
    this.name = name;
    this.topic = topic;
    this.productionInterval = productionInterval;
    this.payloadBytes = payloadBytes;
    this.producerProperties = producerProperties;
  }


  public String getName() {
    return this.name;
  }


  public String getTopic() {
    return this.topic;
  }


  public Duration getProductionInterval() {
    return this.productionInterval;
  }


  public Integer getPayloadBytes() {
    return this.payloadBytes;
  }


  public Map<String,String> getProducerProperties() {
    return this.producerProperties;
  }

}
