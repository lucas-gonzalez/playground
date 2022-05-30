package dev.lucasgonzalez.playground.kafkaproducer.server;

import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import reactor.core.publisher.Mono;

public class UnregisterHandler implements HandlerFunction<ServerResponse> {

  private final ProducerRunner runner;


  public UnregisterHandler(ProducerRunner runner) {
    this.runner = runner;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    var id = request.pathVariable("id");
    return Mono.fromSupplier(() -> runner.stop(id))
        .flatMap(config -> ServerResponse.ok().bodyValue(config))
        .onErrorResume(IllegalArgumentException.class, e -> ServerResponse.notFound().build());
  }

}
