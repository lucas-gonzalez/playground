package dev.lucasgonzalez.playground.kafkaproducer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.Disposable;

@Getter
@AllArgsConstructor
public class RunningProducer {
  
  private final Disposable subscription;

  private final ProducerConfig configuration;
}
