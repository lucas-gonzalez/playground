terraform {
  required_version = "1.2.3"

  required_providers {
    kind = {
      source  = "tehcyx/kind"
      version = "0.0.12"
    }
  }
}

provider "kind" {}

resource "kind_cluster" "kubernetes" {

  name           = "playground"
  wait_for_ready = true

  kind_config {
    kind        = "Cluster"
    api_version = "kind.x-k8s.io/v1alpha4"

    node {
      role  = "control-plane"
      image = "kindest/node:v1.23.4"
    }

    node {
      role  = "worker"
      image = "kindest/node:v1.23.4"
    }

    node {
      role  = "worker"
      image = "kindest/node:v1.23.4"
    }
  }
}