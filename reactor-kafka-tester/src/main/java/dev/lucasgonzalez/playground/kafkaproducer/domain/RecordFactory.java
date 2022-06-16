package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.time.Instant;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.kafka.clients.producer.ProducerRecord;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.kafka.sender.SenderRecord;

public class RecordFactory {

  private final Scheduler producerScheduler;

  public RecordFactory(Scheduler producerScheduler) {
    this.producerScheduler = producerScheduler;
  }

  public Flux<SenderRecord<Long, byte[], Long>> produce(ProducerConfig config) {
    return Flux.interval(config.getProductionInterval())
      .publishOn(producerScheduler)
      .map(record(config));
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
}
