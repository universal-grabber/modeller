variable "DOCKER_IMG_TAG" {}

variable "base_name" {
  type        = string
  description = "Base name"
  default     = "ugm-api"
}


locals {
  repository    = "hub.kube.tisserv.net"
  service_image = "${local.repository}/${var.base_name}:${var.DOCKER_IMG_TAG}"
}
