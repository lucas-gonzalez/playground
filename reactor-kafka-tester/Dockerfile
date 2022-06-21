FROM eclipse-temurin:17.0.3_7-jdk-alpine as extractor
WORKDIR "/application"
ARG JAR_FILE=target/reactor-kafka-tester.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17.0.3_7-jre-alpine
WORKDIR "/application"
COPY --from=extractor application/dependencies/ ./
COPY --from=extractor application/spring-boot-loader/ ./
COPY --from=extractor application/snapshot-dependencies/ ./
COPY --from=extractor application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
