#environments.tf
locals {
  # Environment basado en el workspace de Terraform
  environment = terraform.workspace
  
  # Common tags para todos los recursos
  common_tags = {
    Project     = var.project_name != "" ? var.project_name : "UniversityParkingSystem"
    Environment = local.environment
    ManagedBy   = "Terraform"
    Owner       = var.owner != "" ? var.owner : "DevOps"
    AccountID   = var.account_id != "" ? var.account_id : "685755358010"
  }
  
  # Prefijo para nombres de recursos
  name_prefix = "parking-${local.environment}"
  
  # Configuración específica por entorno
  instance_type = local.environment == "prod" ? "t3.small" : "t3.micro"
  
  # VPC CIDR diferente por entorno
  vpc_cidr = local.environment == "prod" ? "10.1.0.0/16" : "10.0.0.0/16"
  
  # Auto Scaling configuration
  asg_config = {
    min_size     = local.environment == "prod" ? 2 : 1
    max_size     = local.environment == "prod" ? 4 : 2
    desired_size = local.environment == "prod" ? 2 : 1
  }
  
  # Bastion configuration
  bastion_instance_type = local.environment == "prod" ? "t3.small" : "t3.micro"
  
  # SSH access restriction (más restrictivo en prod)
  allowed_ssh_cidr = local.environment == "prod" ? var.allowed_ssh_cidr : "0.0.0.0/0"
}
