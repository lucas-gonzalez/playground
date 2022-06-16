package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FixedConfigs {

  private final Map<String, Object> fixedConfigs;


  public FixedConfigs(Map<String, Object> fixedConfigs) {
    this.fixedConfigs = new HashMap<>(fixedConfigs);
  }


  public Map<String, Object> getFixedConfigs() {
    return Collections.unmodifiableMap(fixedConfigs);
  }
}
