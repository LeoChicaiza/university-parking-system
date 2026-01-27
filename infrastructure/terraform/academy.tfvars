# academy-config.tfvars
environment = "academy"
account_id  = "553140059605"
aws_region  = "us-east-1"
instance_type = "t3.micro"
key_name     = "academy-key"
ami_id       = "ami-0c55b159cbfafe1f0"  # Amazon Linux 2

# Docker
dockerhub_user = "leochicaiza"

# Reducir escala para Academy
asg_min_size = 1
asg_max_size = 1

# Metadata
project_name = "UniversityParkingSystem-Academy"
owner        = "blchicaiza@uce.edu.ec"
