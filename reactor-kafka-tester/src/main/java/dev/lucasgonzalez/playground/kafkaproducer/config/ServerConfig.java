package dev.lucasgonzalez.playground.kafkaproducer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import dev.lucasgonzalez.playground.kafkaproducer.domain.ProducerRunner;
import dev.lucasgonzalez.playground.kafkaproducer.server.DetailHandler;
import dev.lucasgonzalez.playground.kafkaproducer.server.ListHandler;
import dev.lucasgonzalez.playground.kafkaproducer.server.RegisterHandler;
import dev.lucasgonzalez.playground.kafkaproducer.server.UnregisterHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
@Import({AppConfig.class})
public class ServerConfig {

  private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

  @Bean
  RegisterHandler registerHandler(final ProducerRunner runner) {
    return new RegisterHandler(runner);
  }

  @Bean
  UnregisterHandler unregisterHandler(final ProducerRunner runner) {
    return new UnregisterHandler(runner);
  }

  @Bean
  ListHandler listHandler(final ProducerRunner runner) {
    return new ListHandler(runner);
  }

  @Bean
  DetailHandler detailHandler(final ProducerRunner runner) {
    return new DetailHandler(runner);
  }

  @Bean
  public RouterFunction<ServerResponse> monoRouterFunction(
    RegisterHandler registerHandler,
    UnregisterHandler unregisterHandler,
    ListHandler listHandler,
    DetailHandler detailHandler) {
      return route()
              .POST("/producer", ACCEPT_JSON, registerHandler)
              .GET("/producers", ACCEPT_JSON, listHandler)
              .GET("/producers/{id}", ACCEPT_JSON, detailHandler)
              .DELETE("/producers/{id}", ACCEPT_JSON, unregisterHandler)
              .build();
  }
}
