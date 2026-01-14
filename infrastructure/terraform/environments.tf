locals {
  environment = terraform.workspace

  common_tags = {
    Project     = "UniversityParkingSystem"
    Environment = terraform.workspace
    ManagedBy   = "Terraform"
  }
}
