package dev.lucasgonzalez.playground.kafkaproducer.server;

import java.net.URI;

import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerConfig;
import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import reactor.core.publisher.Mono;

public class RegisterHandler implements HandlerFunction<ServerResponse> {

  private final ProducerRunner runner;


  public RegisterHandler(ProducerRunner runner) {
    this.runner = runner;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    return request.bodyToMono(ProducerConfig.class)
        .doOnNext(config -> runner.run(config))
        .map(config -> runner.runningProducer(config.getName()).getConfiguration())
        .flatMap(config -> 
            ServerResponse.created(URI.create("producers/" + config.getName()))
            .bodyValue(config));
  }

}
