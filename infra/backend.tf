terraform {
  backend "local" {
    path = "/var/tfstate/ugm-api.tfstate"
  }
}

provider "kubernetes" {
  config_path    = "~/.kube/config"
  config_context = "minikube"
}
