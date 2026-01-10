resource "aws_subnet" "alb" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "services" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "bastion" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "us-east-1c"
  map_public_ip_on_launch = true
}

resource "aws_route_table_association" "alb" {
  subnet_id      = aws_subnet.alb.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "services" {
  subnet_id      = aws_subnet.services.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "bastion" {
  subnet_id      = aws_subnet.bastion.id
  route_table_id = aws_route_table.public_rt.id
}
