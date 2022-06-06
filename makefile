.PHONY:
build:
	mvn package -f reactor-kafka-tester
	docker build reactor-kafka-tester -t reactor-kafka-tester:latest
