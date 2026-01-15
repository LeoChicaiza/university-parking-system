terraform {
  required_version = ">= 1.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 6.0"
    }
  }

  # Configuración de backend DINÁMICA
  backend "s3" {
    # Variables se llenarán desde archivos .conf
  }
}

# Provider ÚNICO que usa el perfil especificado en variables
provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile  # <-- Esto es clave
  
  # Assume_role solo si se especifica
  dynamic "assume_role" {
    for_each = var.assume_role_arn != "" ? [1] : []
    content {
      role_arn     = var.assume_role_arn
      session_name = "TerraformSession-${var.environment}"
    }
  }
}