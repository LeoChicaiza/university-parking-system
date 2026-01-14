# =====================================================
# SECURITY DOMAIN
# =====================================================
resource "aws_launch_template" "security_domain_lt" {
  name_prefix   = "security-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(templatefile(
    "${path.module}/user_data_security_domain.sh.tpl",
    {
      auth_service_image = var.auth_service_image
      user_service_image = var.user_service_image
      dockerhub_user     = var.dockerhub_user
      dockerhub_token    = var.dockerhub_token
    }
  ))
}

# =====================================================
# VEHICLE DOMAIN
# =====================================================
resource "aws_launch_template" "vehicle_domain_lt" {
  name_prefix   = "vehicle-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(templatefile(
    "${path.module}/user_data_vehicle_domain.sh.tpl",
    {
      vehicle_service_image = var.vehicle_service_image
      dockerhub_user        = var.dockerhub_user
      dockerhub_token       = var.dockerhub_token
    }
  ))
}

# =====================================================
# PARKING DOMAIN
# =====================================================
resource "aws_launch_template" "parking_domain_lt" {
  name_prefix   = "parking-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(templatefile(
    "${path.module}/user_data_parking_domain.sh.tpl",
    {
      parking_space_service_image = var.parking_space_service_image
      parking_lot_service_image   = var.parking_lot_service_image
      dockerhub_user              = var.dockerhub_user
      dockerhub_token             = var.dockerhub_token
    }
  ))
}

# =====================================================
# ACCESS DOMAIN
# =====================================================
resource "aws_launch_template" "access_domain_lt" {
  name_prefix   = "access-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(templatefile(
    "${path.module}/user_data_access_domain.sh.tpl",
    {
      entry_service_image = var.entry_service_image
      exit_service_image  = var.exit_service_image
      dockerhub_user      = var.dockerhub_user
      dockerhub_token     = var.dockerhub_token
    }
  ))
}

# =====================================================
# BILLING DOMAIN
# =====================================================
resource "aws_launch_template" "billing_domain_lt" {
  name_prefix   = "billing-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(templatefile(
    "${path.module}/user_data_billing_domain.sh.tpl",
    {
      billing_service_image      = var.billing_service_image
      notification_service_image = var.notification_service_image
      reporting_service_image    = var.reporting_service_image
      dockerhub_user             = var.dockerhub_user
      dockerhub_token            = var.dockerhub_token
    }
  ))
}

