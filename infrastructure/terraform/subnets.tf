# -------------------------
# PUBLIC SUBNET - ALB
# -------------------------
resource "aws_subnet" "public_alb" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "public-alb-${local.environment}"
  })
}

# -------------------------
# PUBLIC SUBNET - BASTION
# -------------------------
resource "aws_subnet" "public_bastion" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "public-bastion-${local.environment}"
  })
}

# -------------------------
# PRIVATE SUBNET - SERVICES AZ1
# -------------------------
resource "aws_subnet" "private_az1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.10.0/24"
  availability_zone = "us-east-1a"

  tags = merge(local.common_tags, {
    Name = "private-services-az1-${local.environment}"
  })
}

# -------------------------
# PRIVATE SUBNET - SERVICES AZ2
# -------------------------
resource "aws_subnet" "private_az2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.11.0/24"
  availability_zone = "us-east-1b"

  tags = merge(local.common_tags, {
    Name = "private-services-az2-${local.environment}"
  })
}


