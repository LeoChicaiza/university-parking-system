# ==========================
# ENVIRONMENT & ACCOUNT
# ==========================
environment = "prod"
account_id  = "445408867885"  
assume_role_arn = ""

# ==========================
# BACKEND CONFIGURATION
# ==========================
backend_bucket = "terraform-state-prod-445408867885"
aws_profile = "prod" 

# ==========================
# PROJECT METADATA
# ==========================
project_name = "UniversityParkingSystem"
owner        = "DevOps"

# ==========================
# AWS GENERAL
# ==========================
aws_region   = "us-east-1"
# instance_type se define en locals.tf basado en environment

# ==========================
# DOCKER HUB
# ==========================
dockerhub_user  = "leochicaiza"
dockerhub_token = "REPLACE_WITH_ENV_VAR"

# ==========================
# SECURITY DOMAIN
# ==========================
auth_service_image = "leochicaiza/auth-service:prod"      # Cambiar :latest por :prod
user_service_image = "leochicaiza/user-service:prod"

# ==========================
# VEHICLE DOMAIN
# ==========================
vehicle_service_image = "leochicaiza/vehicle-service:prod"

# ==========================
# PARKING DOMAIN
# ==========================
parking_space_service_image = "leochicaiza/parking-space-service:prod"
parking_lot_service_image   = "leochicaiza/parking-lot-service:prod"

# ==========================
# ACCESS DOMAIN
# ==========================
entry_service_image = "leochicaiza/entry-service:prod"
exit_service_image  = "leochicaiza/exit-service:prod"

# ==========================
# BILLING DOMAIN
# ==========================
billing_service_image      = "leochicaiza/billing-service:prod"
notification_service_image = "leochicaiza/notification-service:prod"
reporting_service_image    = "leochicaiza/reporting-service:prod"