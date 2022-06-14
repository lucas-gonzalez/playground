package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
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

  public ProducerRunner(
      final Scheduler producerScheduler,
      final FixedConfigs fixedConfigs,
      final MeterRegistry registry) {
    this.producerScheduler = producerScheduler;
    this.fixedConfigs = fixedConfigs;
    this.registry = registry;
  }

  public Disposable run(final ProducerConfig config) {
    var runningProducer = new RunningProducer(
        assembleProducer(config)
          .name("producer-publisher")
          .tag("name", config.getName())
          .metrics()
          .subscribeOn(producerScheduler)
          .subscribe(),
        config);
    runningProducers.put(config.getName(), runningProducer);
    return runningProducer.getSubscription();
  }

  private Flux<SenderResult<Long>> assembleProducer(ProducerConfig config) {
    final var sender = sender(config);
    return sender
    .send(Flux.interval(config.getProductionInterval())
          .publishOn(producerScheduler)
          .map(record(config)))
      .doOnSubscribe(s -> 
          sender.doOnProducer(producer -> {
            var m = new KafkaClientMetrics(producer, Tags.of("name", config.getName()));
            m.bindTo(registry);
            return m;
          }).subscribe())
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
