resource "aws_autoscaling_group" "entry_asg" {
  name             = "EntryService-ASG"
  desired_capacity = 2
  min_size         = 1
  max_size         = 3

  vpc_zone_identifier = [
    aws_subnet.services.id
  ]

  launch_template {
    id      = aws_launch_template.entry_lt.id
    version = "$Latest"
  }

  target_group_arns = [
    aws_lb_target_group.entry_tg.arn
  ]

  health_check_type = "ELB"
}



