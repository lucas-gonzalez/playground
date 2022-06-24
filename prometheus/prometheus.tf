terraform {
  required_version = "1.2.3"

  required_providers {
    helm = {
      source  = "hashicorp/helm"
      version = "2.6.0"
    }
  }
}

provider "helm" {
  kubernetes {
    config_path    = "~/.kube/config"
    config_context = "kind-playground"
  }
}

resource "helm_release" "prometheus" {
  name          = "prometheus"
  chart         = "prometheus"
  repository    = "https://prometheus-community.github.io/helm-charts"
  version       = "15.10.1"
  wait_for_jobs = true
  timeout       = 300
  values = [
    "${file("values.yaml")}"
  ]
}