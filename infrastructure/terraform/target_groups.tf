# Target Group principal (root)
resource "aws_lb_target_group" "main_tg" {
  name     = "systemParking-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = data.aws_vpc.default.id

  health_check {
    path = "/"
  }
}

# Entry Service
resource "aws_lb_target_group" "entry_tg" {
  name     = "entry-service-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = data.aws_vpc.default.id

  health_check {
    path = "/api/entry"
  }
}

# Exit Service
resource "aws_lb_target_group" "exit_tg" {
  name     = "exit-service-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = data.aws_vpc.default.id

  health_check {
    path = "/api/exit"
  }
}

# Parking Space Service
resource "aws_lb_target_group" "parking_space_tg" {
  name     = "parking-space-service-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = data.aws_vpc.default.id

  health_check {
    path = "/api/parking-spaces"
  }
}
