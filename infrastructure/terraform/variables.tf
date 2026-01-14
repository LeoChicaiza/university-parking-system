# -------------------------
# AWS GENERAL
# -------------------------
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "key_name" {
  description = "EC2 Key Pair name (created manually in AWS)"
  type        = string
}

# -------------------------
# DOCKER HUB
# -------------------------
variable "dockerhub_user" {
  description = "DockerHub username"
  type        = string
}

variable "dockerhub_token" {
  description = "DockerHub access token"
  type        = string
  sensitive   = true
}

# =====================================================
# IMÁGENES DOCKER — POR DOMINIO / MICROSERVICIO
# =====================================================

# -------------------------
# SECURITY DOMAIN
# (auth-service + user-service)
# -------------------------
variable "auth_service_image" {
  description = "Docker image for Auth Service"
  type        = string
}

variable "user_service_image" {
  description = "Docker image for User Service"
  type        = string
}

# -------------------------
# VEHICLE DOMAIN
# (vehicle-service)
# -------------------------
variable "vehicle_service_image" {
  description = "Docker image for Vehicle Service"
  type        = string
}

# -------------------------
# PARKING DOMAIN
# (parking-space-service + parking-lot-service)
# -------------------------
variable "parking_space_service_image" {
  description = "Docker image for Parking Space Service"
  type        = string
}

variable "parking_lot_service_image" {
  description = "Docker image for Parking Lot Service"
  type        = string
}

# -------------------------
# ACCESS DOMAIN
# (entry-service + exit-service)
# -------------------------
variable "entry_service_image" {
  description = "Docker image for Entry Service"
  type        = string
}

variable "exit_service_image" {
  description = "Docker image for Exit Service"
  type        = string
}

# -------------------------
# BILLING DOMAIN
# (billing + notification + reporting)
# -------------------------
variable "billing_service_image" {
  description = "Docker image for Billing Service"
  type        = string
}

variable "notification_service_image" {
  description = "Docker image for Notification Service"
  type        = string
}

variable "reporting_service_image" {
  description = "Docker image for Reporting Service"
  type        = string
}

