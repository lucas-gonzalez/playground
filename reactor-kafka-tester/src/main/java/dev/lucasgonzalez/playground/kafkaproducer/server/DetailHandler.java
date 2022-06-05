package dev.lucasgonzalez.playground.kafkaproducer.server;

import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import reactor.core.publisher.Mono;

public class DetailHandler implements HandlerFunction<ServerResponse> {

  private final ProducerRunner runner;

  public DetailHandler(final ProducerRunner runner) {
    this.runner = runner;
  }


  @Override
  public Mono<ServerResponse> handle(final ServerRequest request) {
    var id = request.pathVariable("id");
    return Mono.just(runner.runningProducer(id).getConfiguration())
        .flatMap(config -> ServerResponse.ok().bodyValue(config))
        .onErrorResume(IllegalArgumentException.class, e -> ServerResponse.notFound().build());
  }
}
