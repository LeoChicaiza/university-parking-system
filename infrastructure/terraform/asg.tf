# -------------------------
# SECURITY DOMAIN ASG
# -------------------------
resource "aws_autoscaling_group" "security_asg" {
  name                = "security-asg-${local.environment}"
  max_size            = var.asg_max_size
  min_size            = var.asg_min_size
  desired_capacity    = var.asg_min_size 
  vpc_zone_identifier = [
  aws_subnet.private_az1.id,
  aws_subnet.private_az2.id
  ]


  launch_template {
    id      = aws_launch_template.security_domain_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.security_tg.arn
  ]

  tag {
    key                 = "Name"
    value               = "security-domain-${local.environment}"
    propagate_at_launch = true
  }
}

# -------------------------
# VEHICLE DOMAIN ASG
# -------------------------
resource "aws_autoscaling_group" "vehicle_asg" {
  name                = "vehicle-asg-${local.environment}"
  max_size            = var.asg_max_size
  min_size            = var.asg_min_size
  desired_capacity    = var.asg_min_size
  vpc_zone_identifier = [
  aws_subnet.private_az1.id,
  aws_subnet.private_az2.id
  ]

  launch_template {
    id      = aws_launch_template.vehicle_domain_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.vehicle_tg.arn
  ]

  tag {
    key                 = "Name"
    value               = "vehicle-domain-${local.environment}"
    propagate_at_launch = true
  }
}

# -------------------------
# PARKING DOMAIN ASG
# -------------------------
resource "aws_autoscaling_group" "parking_asg" {
  name                = "parking-asg-${local.environment}"
  max_size            = var.asg_max_size
  min_size            = var.asg_min_size
  desired_capacity    = var.asg_min_size
  vpc_zone_identifier = [
  aws_subnet.private_az1.id,
  aws_subnet.private_az2.id
  ]

  launch_template {
    id      = aws_launch_template.parking_domain_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.parking_tg.arn
  ]

  tag {
    key                 = "Name"
    value               = "parking-domain-${local.environment}"
    propagate_at_launch = true
  }
}

# -------------------------
# ACCESS DOMAIN ASG
# -------------------------
resource "aws_autoscaling_group" "access_asg" {
  name                = "access-asg-${local.environment}"
  max_size            = var.asg_max_size
  min_size            = var.asg_min_size
  desired_capacity    = var.asg_min_size
  vpc_zone_identifier = [
  aws_subnet.private_az1.id,
  aws_subnet.private_az2.id
  ]

  launch_template {
    id      = aws_launch_template.access_domain_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.access_tg.arn
  ]

  tag {
    key                 = "Name"
    value               = "access-domain-${local.environment}"
    propagate_at_launch = true
  }
}

# -------------------------
# BILLING DOMAIN ASG
# -------------------------
resource "aws_autoscaling_group" "billing_asg" {
  name                = "billing-asg-${local.environment}"
  max_size            = var.asg_max_size
  min_size            = var.asg_min_size
  desired_capacity    = var.asg_min_size
  vpc_zone_identifier = [
  aws_subnet.private_az1.id,
  aws_subnet.private_az2.id
  ]

  launch_template {
    id      = aws_launch_template.billing_domain_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.billing_tg.arn
  ]

  tag {
    key                 = "Name"
    value               = "billing-domain-${local.environment}"
    propagate_at_launch = true
  }
}

