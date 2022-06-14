package dev.lucasgonzalez.playground.kafkaproducer.server;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public class CreateTopicHandler implements HandlerFunction<ServerResponse> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AdminClient adminClient;


  public CreateTopicHandler(AdminClient adminClient) {
    this.adminClient = adminClient;
  }


  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    var topicName = request.pathVariable("id");
    try {
      adminClient.createTopics(
        Collections.singleton(
          new NewTopic(
            topicName,
            3,
            (short) 2)))
        .all()
        .get(5, TimeUnit.SECONDS);
      return ServerResponse.created(request.uri()).build();
    } catch (InterruptedException|ExecutionException|TimeoutException e) {
      logger.error("Topic creation failed for topic=" + topicName, e);
      return ServerResponse.status(500).build();
    }
  }
}
