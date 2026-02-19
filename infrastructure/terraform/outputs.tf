output "alb_dns" {
  value = aws_lb.alb.dns_name
}

output "bastion_ip" {
  value = aws_instance.bastion.public_ip
}


# API Gateway
output "api_gateway_invoke_url" {
  description = "URL para invocar el API Gateway"
  value       = "${aws_apigatewayv2_api.parking_api.api_endpoint}/"
}

output "alb_dns_name" {
  description = "DNS name del ALB interno"
  value       = aws_lb.alb.dns_name
}

output "bastion_public_ip" {
  description = "IP pública del bastion host"
  value       = aws_instance.bastion.public_ip
}

output "vpc_id" {
  description = "ID de la VPC"
  value       = aws_vpc.main.id
}