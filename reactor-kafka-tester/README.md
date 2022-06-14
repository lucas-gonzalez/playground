# Reactor Kafka Tester

## Running

```shell
make build && docker compose up -d
```

## Create Topic

```shell
curl localhost:8080/topic/basic -XPOST -v
```

## Start Producer

```shell
curl localhost:8080/producer -d'
{
  "name": "basic",
  "topic": "basic",
  "productionInterval": "PT0.1S",
  "payloadBytes": 20,
  "producerProperties": {}
}' -H "Content-Type:application/json"
```