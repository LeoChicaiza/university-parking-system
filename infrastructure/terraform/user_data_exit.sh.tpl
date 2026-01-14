#!/bin/bash
set -e

# Install Docker
if [ -x "$(command -v yum)" ]; then
  sudo dnf update -y || sudo yum update -y
  sudo dnf install -y docker || sudo yum install -y docker
else
  sudo apt-get update -y
  sudo apt-get install -y docker.io
fi

sudo systemctl enable docker
sudo systemctl start docker

# Docker permissions
sudo usermod -aG docker ec2-user || true
sudo usermod -aG docker ubuntu || true

# Run Exit Service container
docker pull ${docker_image}
docker rm -f exit-service || true
docker run -d \
  --name exit-service \
  -p 80:8082 \
  --restart always \
  ${docker_image}
