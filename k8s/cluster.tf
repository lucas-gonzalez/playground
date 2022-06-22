terraform {
  required_version = ">= 1.2.3"

  required_providers {
    kind = {
      source  = "tehcyx/kind"
      version = ">= 0.0.12"
    }
  }
}

provider "kind" {}

resource "kind_cluster" "local" {

  name           = "k8s-local"
  wait_for_ready = true

  kind_config {
    kind        = "Cluster"
    api_version = "kind.x-k8s.io/v1alpha4"

    node {
      role = "control-plane"
    }

    node {
      role  = "worker"
      image = "kindest/node:v1.24.0"
    }

    node {
      role  = "worker"
      image = "kindest/node:v1.24.0"
    }
  }

}