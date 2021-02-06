terraform {
  backend "local" {
    path = "/var/tfstate/ugm-api.tfstate"
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
  config_context = "gke_fifth-subject-300120_europe-west2-c_cluster-1"
}
