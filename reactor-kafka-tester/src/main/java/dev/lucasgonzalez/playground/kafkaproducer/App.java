package dev.lucasgonzalez.playground.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import dev.lucasgonzalez.playground.kafkaproducer.config.ServerConfig;

@Import({ServerConfig.class})
@SpringBootApplication
public class App {
    public static void main( String[] args) {
        SpringApplication.run(App.class, args);
    }
}
