# -------------------------
# APPLICATION LOAD BALANCER (INTERNO)
# -------------------------
resource "aws_lb" "alb" {
  name               = "parking-alb-${local.environment}"
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  internal           = true  # ← INTERNO
  
  # SOLO subnets PRIVADAS
  subnets = [
    aws_subnet.private_az1.id,  # us-east-1a
    aws_subnet.private_az2.id   # us-east-1b
  ]

  tags = merge(local.common_tags, {
    Name = "parking-alb-${local.environment}"
  })
}

# -------------------------
# ALB LISTENER (HTTP)
# -------------------------
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      message_body = "Service not found"
      status_code  = "404"
    }
  }
}

# -------------------------
# LISTENER RULES (PATH BASED)
# -------------------------
resource "aws_lb_listener_rule" "security_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 10

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.security_tg.arn
  }

  condition {
    path_pattern {
      values = ["/auth/*", "/users/*"]
    }
  }
}

resource "aws_lb_listener_rule" "vehicle_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 20

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.vehicle_tg.arn
  }

  condition {
    path_pattern {
      values = ["/vehicles/*"]
    }
  }
}

resource "aws_lb_listener_rule" "parking_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 30

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.parking_tg.arn
  }

  condition {
    path_pattern {
      values = ["/parking/*"]
    }
  }
}

resource "aws_lb_listener_rule" "access_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 40

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.access_tg.arn
  }

  condition {
    path_pattern {
      values = ["/entry/*", "/exit/*"]
    }
  }
}

resource "aws_lb_listener_rule" "billing_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 50

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.billing_tg.arn
  }

  condition {
    path_pattern {
      values = ["/billing/*", "/reports/*", "/notifications/*"]
    }
  }
}