package dev.lucasgonzalez.playground.kafkaproducer.domain;

import java.util.Collections;
import java.util.Map;

public class FixedConfigs {

  private final Map<String, String> fixedConfigs;


  public FixedConfigs(Map<String,String> fixedConfigs) {
    this.fixedConfigs = fixedConfigs;
    
  }


  public Map<String, String> getFixedConfigs() {
    return Collections.unmodifiableMap(fixedConfigs);
  }
}
