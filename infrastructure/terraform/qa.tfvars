# ==========================
# ENVIRONMENT & ACCOUNT
# ==========================
environment = "qa"
account_id  = "506474937999"  # Tu cuenta actual de QA
assume_role_arn = ""  # Vacío para QA (misma cuenta)

# ==========================
# BACKEND CONFIGURATION
# ==========================
backend_bucket = "terraform-state-qa-506474937999"
aws_profile = "qa" 

# ==========================
# PROJECT METADATA
# ==========================
project_name = "UniversityParkingSystem"
owner        = "DevOps"





# ==========================
# AWS
# ==========================
aws_region   = "us-east-1"
instance_type = "t3.micro"
key_name     = "parking-qa-key"

# ==========================
# DockerHub
# ==========================
dockerhub_user  = "leochicaiza"
#dockerhub_token = "REPLACE_WITH_ENV_VAR"

# ==========================
# SECURITY DOMAIN
# ==========================
auth_service_image = "leochicaiza/auth-service:latest"
user_service_image = "leochicaiza/user-service:latest"

# ==========================
# VEHICLE DOMAIN
# ==========================
vehicle_service_image = "leochicaiza/vehicle-service:latest"

# ==========================
# PARKING DOMAIN
# ==========================
parking_space_service_image = "leochicaiza/parking-space-service:latest"
parking_lot_service_image   = "leochicaiza/parking-lot-service:latest"

# ==========================
# ACCESS DOMAIN
# ==========================
entry_service_image = "leochicaiza/entry-service:latest"
exit_service_image  = "leochicaiza/exit-service:latest"

# ==========================
# BILLING DOMAIN
# ==========================
billing_service_image      = "leochicaiza/billing-service:latest"
notification_service_image = "leochicaiza/notification-service:latest"
reporting_service_image    = "leochicaiza/reporting-service:latest"
