package dev.lucasgonzalez.playground.kafkaproducer.domain;

import static org.mockito.ArgumentMatchers.eq;

import java.time.Duration;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

public class KafkaSenderFactoryTest {

  @Test
  void testCreateCannotOverrideFixedConfigs() {

    final var fixedConfig = "bootstrap.servers";
    final var fixedConfigValue = "host:1234";
    final var fixedConfigs = new FixedConfigs(Collections.singletonMap(fixedConfig, fixedConfigValue));
    final var kafkaSenderFactory = new KafkaSenderFactory(fixedConfigs);
    final var producerConfig = new ProducerConfig(
      "name",
      "topic",
      Duration.ofSeconds(1),
      8,
      Collections.singletonMap(fixedConfig, "some-value"));
    final var expectedSenderOptions = SenderOptions.create(Collections.singletonMap(fixedConfig, fixedConfigValue));

    try (var kafkaSenderMock = Mockito.mockStatic(KafkaSender.class)) {
      kafkaSenderFactory.create(producerConfig);
      kafkaSenderMock.verify(() -> KafkaSender.create(eq(expectedSenderOptions)));
    }
  }
}
