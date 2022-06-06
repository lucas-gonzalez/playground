# Reactor Kafka Tester

## Producer Example

```shell
curl localhost:8080/producer -d'
{
  "name":"basic",
  "topic":"basic-topic",
  "productionInterval":"PT0.1S",
  "payloadBytes":20,
  "producerProperties":{}
}' -H"Content-Type:application/json"
```