package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.time.Duration;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
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
}
