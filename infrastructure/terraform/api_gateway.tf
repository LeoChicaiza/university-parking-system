# api_gateway.tf

# -------------------------
# VPC LINK (para conectar API Gateway con ALB interno)
# -------------------------
resource "aws_apigatewayv2_vpc_link" "alb_vpc_link" {
  name               = "parking-alb-vpc-link-${local.environment}"
  security_group_ids = [aws_security_group.alb_sg.id]
  subnet_ids         = [aws_subnet.private_az1.id, aws_subnet.private_az2.id]

  tags = merge(local.common_tags, {
    Name = "alb-vpc-link-${local.environment}"
  })
}

# -------------------------
# API GATEWAY (HTTP API - más económico que REST API)
# -------------------------
resource "aws_apigatewayv2_api" "parking_api" {
  name          = "university-parking-api-${local.environment}"
  protocol_type = "HTTP"
  description   = "API Gateway for University Parking System"

  cors_configuration {
    allow_origins = ["*"]  # En producción, restringe esto
    allow_methods = ["GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"]
    allow_headers = ["*"]
    max_age       = 300
  }

  tags = merge(local.common_tags, {
    Name = "parking-api-${local.environment}"
  })
}

# -------------------------
# CUSTOM DOMAIN (opcional pero recomendado)
# -------------------------
# Necesitarías un certificado SSL y dominio personalizado
# resource "aws_apigatewayv2_domain_name" "api_domain" {
#   domain_name = "api.parking.university.com"
#   
#   domain_name_configuration {
#     certificate_arn = aws_acm_certificate.api_cert.arn
#     endpoint_type   = "REGIONAL"
#     security_policy = "TLS_1_2"
#   }
# }

# -------------------------
# API STAGE
# -------------------------
resource "aws_apigatewayv2_stage" "api_stage" {
  api_id      = aws_apigatewayv2_api.parking_api.id
  name        = "$default"
  auto_deploy = true

  # Access logging (opcional pero recomendado)
  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gw_logs.arn
    format = jsonencode({
      requestId               = "$context.requestId"
      ip                      = "$context.identity.sourceIp"
      requestTime             = "$context.requestTime"
      httpMethod              = "$context.httpMethod"
      routeKey                = "$context.routeKey"
      status                  = "$context.status"
      protocol                = "$context.protocol"
      responseLength          = "$context.responseLength"
      integrationErrorMessage = "$context.integrationErrorMessage"
    })
  }

  tags = merge(local.common_tags, {
    Name = "api-stage-${local.environment}"
  })
}

# -------------------------
# CLOUDWATCH LOG GROUP
# -------------------------
resource "aws_cloudwatch_log_group" "api_gw_logs" {
  name              = "/aws/apigateway/parking-api-${local.environment}"
  retention_in_days = 7

  tags = merge(local.common_tags, {
    Name = "api-gw-logs-${local.environment}"
  })
}

# -------------------------
# INTEGRATIONS (conexión con ALB)
# -------------------------
resource "aws_apigatewayv2_integration" "alb_integration" {
  api_id             = aws_apigatewayv2_api.parking_api.id
  description        = "Integration with internal ALB"
  integration_type   = "HTTP_PROXY"
  connection_type    = "VPC_LINK"
  connection_id      = aws_apigatewayv2_vpc_link.alb_vpc_link.id
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.http.arn
  
  # Timeouts
  request_parameters = {
    "overwrite:path" = "$request.path"  # Pasa el path completo al ALB
  }
  
  # Pasar headers importantes
  payload_format_version = "1.0"
}

# -------------------------
# ROUTES (mapeo de paths)
# -------------------------

# Ruta para Security Domain
resource "aws_apigatewayv2_route" "auth_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /auth/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_route" "users_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /api/users/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# Ruta para Vehicle Domain
resource "aws_apigatewayv2_route" "vehicles_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /vehicles/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# Ruta para Parking Domain
resource "aws_apigatewayv2_route" "parking_lots_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /parking-lots/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_route" "parking_spaces_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /parking-spaces/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# Ruta para Access Domain
resource "aws_apigatewayv2_route" "entry_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /entry/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_route" "exit_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /api/exit/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# Ruta para Billing Domain
resource "aws_apigatewayv2_route" "billing_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /billing/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_route" "notifications_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /notifications/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_route" "reports_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "ANY /reports/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# Ruta por defecto (catch-all)
resource "aws_apigatewayv2_route" "default_route" {
  api_id    = aws_apigatewayv2_api.parking_api.id
  route_key = "$default"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

# -------------------------
# OUTPUTS
# -------------------------
output "api_gateway_url" {
  description = "URL del API Gateway"
  value       = "${aws_apigatewayv2_api.parking_api.api_endpoint}/"
}

output "api_gateway_id" {
  description = "ID del API Gateway"
  value       = aws_apigatewayv2_api.parking_api.id
}

output "vpc_link_id" {
  description = "ID del VPC Link"
  value       = aws_apigatewayv2_vpc_link.alb_vpc_link.id
}
