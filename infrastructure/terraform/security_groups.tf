# -------------------------
# SECURITY GROUP - BASTION (PRIMERO - sin dependencias)
# -------------------------
resource "aws_security_group" "bastion_sg" {
  name        = "bastion-sg-${local.environment}"
  description = "Bastion host SG"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "SSH from your IP"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["157.100.140.8/32"]  
  }

  # EGRESS: Permitir todo (sin referencia a otros SGs)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "bastion-sg-${local.environment}"
  })
}

# -------------------------
# SECURITY GROUP - ALB (SEGUNDO - referencia solo a VPC)
# -------------------------
resource "aws_security_group" "alb_sg" {
  name        = "alb-sg-${local.environment}"
  description = "Internal ALB Security Group"
  vpc_id      = aws_vpc.main.id

  # Permitir tráfico desde la VPC completa
  ingress {
    description = "HTTP from VPC"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]  # Desde toda la VPC
  }

  # NO referencias a bastion_sg aquí (causa ciclo)
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "alb-sg-${local.environment}"
  })
}

# -------------------------
# SECURITY GROUP - SERVICES (TERCERO - puede referenciar a otros)
# -------------------------
resource "aws_security_group" "services_sg" {
  name        = "services-sg-${local.environment}"
  description = "Microservices SG"
  vpc_id      = aws_vpc.main.id

  # Traffic from ALB
  ingress {
    description     = "Traffic from internal ALB"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]  # ✅ OK
  }

  # SSH from Bastion
  ingress {
    description     = "SSH from Bastion"
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.bastion_sg.id]  # ✅ OK
  }

  # Comunicación entre microservicios
  ingress {
    description     = "Inter-service communication"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    self            = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "services-sg-${local.environment}"
  })
}