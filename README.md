# Playground

`Playground` is a [monorepo](https://en.wikipedia.org/wiki/Monorepo) that hosts multiple research and fun projects.

`Playground` projects are referred to as _modules_ and are meant to be composable. `kubernetes` _module_, for example, sets a [Kubernetes](https://kubernetes.io/) cluster up for other _modules_ to leverage as target deployment environment.

## Modules

* `kubernetes`: [Kubernetes](https://kubernetes.io/) cluster setup using [Kind](https://kind.sigs.k8s.io/).
* `prometheus`: [Prometheus](https://prometheus.io/) deployment on top of `kubernetes` using [Helm](https://helm.sh/).
* `reactor-kafka-tester`: [Spring Boot](https://spring.io/projects/spring-boot) application to test out [Reactor Kafka](https://projectreactor.io/docs/kafka/release/reference/).

## Tooling

Common tooling across different _modules_:

* [asdf](https://asdf-vm.com/) _(pre-requisite)_
* [Docker](https://docs.docker.com/get-started/overview/) _(pre-requisite)_
* [GitHub Actions](https://github.com/features/actions)
* [Terraform](https://www.terraform.io/)
* [GNU make](https://www.gnu.org/software/make/manual/make.html) _(pre-requisite)_

Tooling marked as _pre-requisite_ currently needs to be manually installed. [GNU make](https://www.gnu.org/software/make/manual/make.html) will be moved into [asdf](https://asdf-vm.com/) in upcoming changes.

## Upcoming

Beyond the features outlined in the [issues](https://github.com/lucas-gonzalez/playground/issues) section, support for building each and all of the _modules_ from the project root, likely via [GNU make](https://www.gnu.org/software/make/manual/make.html) is going to be added, as well as documentation for each _module_.
