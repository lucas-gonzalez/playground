package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;

public class ProducerRunner {

  private final ConcurrentMap<String, RunningProducer> runningProducers = new ConcurrentHashMap<>();

  private final Scheduler producerScheduler;

  private final KafkaSenderFactory kafkaSenderFactory;

  private final RecordFactory recordFactory;

  private final MeterRegistry registry;

  public ProducerRunner(
      final Scheduler producerScheduler,
      final KafkaSenderFactory kafkaSenderFactory,
      final RecordFactory recordFactory,
      final MeterRegistry registry) {
    this.producerScheduler = producerScheduler;
    this.kafkaSenderFactory = kafkaSenderFactory;
    this.recordFactory = recordFactory;
    this.registry = registry;
  }

  public Disposable run(final ProducerConfig config) {
    var runningProducer = new RunningProducer(
        runProducer(config),
        config);
    runningProducers.put(config.getName(), runningProducer);
    return runningProducer.getSubscription();
  }

  private Disposable runProducer(ProducerConfig config) {
    final var sender = kafkaSenderFactory.create(config);
    return sender.send(recordFactory.produce(config))
      .doOnSubscribe(s -> 
          sender.doOnProducer(producer -> {
            var m = new KafkaClientMetrics(producer, Tags.of("name", config.getName()));
            m.bindTo(registry);
            return m;
          }).subscribe())
      .name("producer-publisher")
      .tag("name", config.getName())
      .metrics()
      .subscribeOn(producerScheduler)
      .doOnComplete(sender::close)
      .subscribe();
  }

  public RunningProducer runningProducer(String name) {
    return Optional.ofNullable(runningProducers.get(name))
      .orElseThrow(() -> new IllegalArgumentException());
  }

  public Collection<RunningProducer> runningProducers() {
    return this.runningProducers.values();
  }

  public ProducerConfig stop(String name) {
    var runningProducer = Optional.ofNullable(runningProducers.remove(name));
    runningProducer.ifPresent(p -> p.getSubscription().dispose());
    return runningProducer
      .map(RunningProducer::getConfiguration)
      .orElseThrow(() -> new IllegalArgumentException());
  }
}
