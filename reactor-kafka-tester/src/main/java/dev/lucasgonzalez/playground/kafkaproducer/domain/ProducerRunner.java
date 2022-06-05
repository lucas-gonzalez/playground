package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

public class ProducerRunner {

  private final ConcurrentMap<String, RunningProducer> runningProducers = new ConcurrentHashMap<>();

  private final Scheduler producerScheduler;

  private final FixedConfigs fixedConfigs;

  private final MeterRegistry registry;

  public ProducerRunner(final Scheduler producerScheduler, final FixedConfigs fixedConfigs, final MeterRegistry registry) {
    this.producerScheduler = producerScheduler;
    this.fixedConfigs = fixedConfigs;
    this.registry = registry;
  }

  public Disposable run(final ProducerConfig config) {
    var runningProducer = new RunningProducer(
      assembleProducer(config)
        .subscribeOn(producerScheduler)
        .subscribe(),
      config);
    runningProducers.put(config.getName(), runningProducer);
    return runningProducer.getSubscription();
  }

  private Flux<SenderResult<Long>> assembleProducer(ProducerConfig config) {
    final var sender = sender(config);
    final var summary = metric(config);
    return sender.send(Flux.interval(config.getProductionInterval())
          .publishOn(producerScheduler)
          .map(record(config)))
      .doOnNext(measure(summary))
      .doOnComplete(sender::close);
  }

  private Function<Long, SenderRecord<Long, byte[], Long>> record(ProducerConfig config) {
    final var supplier = payloadSupplier(config.getPayloadBytes());
    return i -> SenderRecord.create(
        new ProducerRecord<Long, byte[]>(
          config.getTopic(),
          null,
          Instant.now().toEpochMilli(),
          i, supplier.get()),
        i);
  }

  private Consumer<SenderResult<Long>> measure(final DistributionSummary summary) {
    return r -> summary.record(Instant.now().toEpochMilli() - r.recordMetadata().timestamp());
  }

  private DistributionSummary metric(ProducerConfig config) {
    return DistributionSummary.builder("reactor.kafka.producer.times")
        .tag("name", config.getName())
        .scale(100d)
        .serviceLevelObjectives(95d, 99d)
        .register(registry);
  }

  private Supplier<byte[]> payloadSupplier(final int payloadLength) {
    final var random = new Random();
    return () -> {
      var bytes = new byte[payloadLength];
      random.nextBytes(bytes);
      return bytes;
    };
  }

  private KafkaSender<Long, byte[]> sender(ProducerConfig config) {
    final var props = new Properties();
    props.putAll(config.getProducerProperties());
    props.putAll(fixedConfigs.getFixedConfigs());
    return KafkaSender.<Long, byte[]>create(SenderOptions.create(props));
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
