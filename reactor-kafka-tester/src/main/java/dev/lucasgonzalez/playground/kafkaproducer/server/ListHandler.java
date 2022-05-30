package dev.lucasgonzalez.playground.kafkaproducer.server;

import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import dev.lucasgonzalez.playground.kafkaproducer.domain.RunningProducer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ListHandler implements HandlerFunction<ServerResponse> {

  private final ProducerRunner runner;

  public ListHandler(ProducerRunner runner) {
    this.runner = runner;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    return Flux.fromIterable(runner.runningProducers())
        .map(RunningProducer::getConfiguration)
        .collectList()
        .flatMap(producers -> ServerResponse.ok().bodyValue(producers));
  }

}
