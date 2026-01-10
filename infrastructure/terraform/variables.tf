variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "desired_capacity" {
  type    = number
  default = 10
}

variable "min_size" {
  type    = number
  default = 2
}

variable "max_size" {
  type    = number
  default = 10
}

variable "docker_image" {
  type        = string
  description = "Imagen Docker en DockerHub en formato user/repo:tag"
  default     = "leochicaiza/hola-mundo:latest"
}

variable "key_name" {
  type        = string
  description = "Nombre del key pair para acceso SSH (dejar vacío si no necesitas SSH)"
  default     = ""
}

variable "entry_service_image" {
  type        = string
  description = "Docker image for Entry Service"
  default     = "leochicaiza/entry-service:latest"
}

variable "exit_service_image" {
  type        = string
  description = "Docker image for Exit Service"
  default     = "leochicaiza/exit-service:latest"
}

variable "parking_space_service_image" {
  type        = string
  description = "Docker image for Parking Space Service"
  default     = "leochicaiza/parking-space-service:latest"
}
