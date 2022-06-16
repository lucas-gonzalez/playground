package dev.lucasgonzalez.playground.kafkaproducer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class FixedConfigsTest {

  @Test
  void testFixedConfigReturnsArgumentConfig() {

    final var fixedConfig = "bootstrap.servers";
    final var fixedConfigValue = "host:1234";

    final var fixedConfigs = new FixedConfigs(Collections.singletonMap(fixedConfig, fixedConfigValue));

    assertNotNull(fixedConfigs.getFixedConfigs());
    assertFalse(fixedConfigs.getFixedConfigs().isEmpty());
    assertEquals(fixedConfigValue, fixedConfigs.getFixedConfigs().get(fixedConfig));
  }

  @Test
  void testFixedConfigsCannotBeModified() {

    final var fixedConfig = "bootstrap.servers";
    final var fixedConfigValue = "host:1234";
    final var modifiedValue = "modified-value";
    final var map = new HashMap<String, Object>();
    map.put(fixedConfig, fixedConfigValue);

    final var fixedConfigs = new FixedConfigs(map);
    map.put(fixedConfig, modifiedValue);

    assertNotNull(fixedConfigs.getFixedConfigs());
    assertFalse(fixedConfigs.getFixedConfigs().isEmpty());
    assertEquals(fixedConfigValue, fixedConfigs.getFixedConfigs().get(fixedConfig));
    assertThrows(UnsupportedOperationException.class, () -> fixedConfigs.getFixedConfigs().put(fixedConfig, modifiedValue));
  }
}
