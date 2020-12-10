terraform {
  backend "local" {
    path = "/var/tfstate/ugm-api.tfstate"
  }
}

provider "kubernetes" {
  config_context = "gke_ivory-being-291419_us-central1-c_universal-grabber"
}
