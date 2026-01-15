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

# =====================================================
# MULTI-ACCOUNT / ENVIRONMENT CONFIGURATION
# =====================================================

# -------------------------
# ENVIRONMENT IDENTIFICATION
# -------------------------
variable "environment" {
  description = "Environment name: 'qa' or 'prod'"
  type        = string
  default     = "qa"
}

variable "account_id" {
  description = "12-digit AWS Account ID"
  type        = string
  default     = ""
}

# -------------------------
# CROSS-ACCOUNT ACCESS (for PROD only)
# -------------------------
variable "assume_role_arn" {
  description = "ARN of IAM Role for cross-account access (empty for QA)"
  type        = string
  default     = ""
}

# -------------------------
# BACKEND CONFIGURATION
# -------------------------
variable "backend_bucket" {
  description = "S3 bucket for Terraform state storage"
  type        = string
  default     = ""
}

# -------------------------
# PROJECT METADATA
# -------------------------
variable "project_name" {
  description = "Project name for resource tagging"
  type        = string
  default     = "UniversityParkingSystem"
}

# -------------------------
# OPTIONAL ENVIRONMENT TWEAKS
# -------------------------
variable "bastion_instance_type" {
  description = "EC2 instance type for bastion host"
  type        = string
  default     = "t3.micro"
}

variable "asg_min_size" {
  description = "Minimum instances in Auto Scaling Groups"
  type        = number
  default     = 1
}

variable "asg_max_size" {
  description = "Maximum instances in Auto Scaling Groups"
  type        = number
  default     = 2
}

# =====================================================
# ADDITIONAL VARIABLES FOR ENVIRONMENTS.TF
# =====================================================

variable "owner" {
  description = "Owner/team name for resource tagging"
  type        = string
  default     = "DevOps"
}

variable "allowed_ssh_cidr" {
  description = "CIDR block allowed to SSH to bastion host"
  type        = string
  default     = "0.0.0.0/0"
}

variable "aws_profile" {
  description = "AWS CLI profile name"
  type        = string
  default     = "default"
}