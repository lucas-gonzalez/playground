package dev.lucasgonzalez.playground.kafkaproducer.domain;

import reactor.core.Disposable;

public class RunningProducer {
  
  private final Disposable subscription;

  private final ProducerConfig configuration;


  public RunningProducer(Disposable subscription, ProducerConfig configuration) {
    this.subscription = subscription;
    this.configuration = configuration;
  }


  public Disposable getSubscription() {
    return this.subscription;
  }


  public ProducerConfig getConfiguration() {
    return this.configuration;
  }

}
