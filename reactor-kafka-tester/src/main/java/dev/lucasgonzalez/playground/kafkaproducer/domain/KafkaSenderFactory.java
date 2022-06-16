package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.util.HashMap;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

public class KafkaSenderFactory {

  private final FixedConfigs fixedConfigs;

  public KafkaSenderFactory(final FixedConfigs fixedConfigs) {
    this.fixedConfigs = fixedConfigs;
  }

  public KafkaSender<Long, byte[]> create(final ProducerConfig config) {
    var props = new HashMap<>(config.getProducerProperties());
    props.putAll(fixedConfigs.getFixedConfigs());
    return KafkaSender.<Long, byte[]>create(SenderOptions.create(props));
  }
}
