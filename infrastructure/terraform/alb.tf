resource "aws_lb" "alb" {
  load_balancer_type = "application"
  subnets            = [aws_subnet.alb.id, aws_subnet.services.id]
  security_groups    = [aws_security_group.alb_sg.id]
}
