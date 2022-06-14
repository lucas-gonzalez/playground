package dev.lucasgonzalez.playground.kafkaproducer.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.lucasgonzalez.playground.kafkaproducer.domain.FixedConfigs;
import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

  @Bean
  FixedConfigs fixedConfigs() {
    var fixedConfigs = new HashMap<String, String>();
    fixedConfigs.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getCanonicalName());
    fixedConfigs.put(VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
    fixedConfigs.put(BOOTSTRAP_SERVERS_CONFIG, "kevin:9092,stuart:9093,bob:9094");
    fixedConfigs.put(CLIENT_ID_CONFIG, "kafka-producer-" + UUID.randomUUID().toString());
    return new FixedConfigs(fixedConfigs);
  }

  @Bean
  ProducerRunner producerRunner(FixedConfigs fixedConfigs, MeterRegistry registry, AdminClient adminClient) {
    return new ProducerRunner(
      Schedulers.newBoundedElastic(50, 5, "producer-runner"),
      fixedConfigs,
      registry);
  }

  @Bean
  public AdminClient adminClient() {
    var clientProperties = new Properties();
    clientProperties.put(BOOTSTRAP_SERVERS_CONFIG, "kevin:9092,stuart:9093,bob:9094");
    return AdminClient.create(clientProperties);
  }
}
