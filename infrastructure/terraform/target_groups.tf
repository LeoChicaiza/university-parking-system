# SECURITY DOMAIN
resource "aws_lb_target_group" "security_tg" {
  name = "security-tg-${local.environment}"
  port     = 80
  protocol = "HTTP"
  vpc_id  = aws_vpc.main.id

  health_check {
    path = "/actuator/health"
  }
}

# VEHICLE DOMAIN
resource "aws_lb_target_group" "vehicle_tg" {
  name = "vehicle-tg-${local.environment}"
  port     = 80
  protocol = "HTTP"
  vpc_id  = aws_vpc.main.id

  health_check {
    path = "/actuator/health"
  }
}

# PARKING DOMAIN
resource "aws_lb_target_group" "parking_tg" {
  name = "parking-tg-${local.environment}"
  port     = 80
  protocol = "HTTP"
  vpc_id  = aws_vpc.main.id

  health_check {
    path = "/actuator/health"
  }
}

# ACCESS DOMAIN
resource "aws_lb_target_group" "access_tg" {
  name = "access-tg-${local.environment}"
  port     = 80
  protocol = "HTTP"
  vpc_id  = aws_vpc.main.id

  health_check {
    path = "/actuator/health"
  }
}

# BILLING DOMAIN
resource "aws_lb_target_group" "billing_tg" {
  name = "billing-tg-${local.environment}"
  port     = 80
  protocol = "HTTP"
  vpc_id  = aws_vpc.main.id

  health_check {
    path = "/actuator/health"
  }
}
