#!/bin/bash
set -e

# Update system and install Docker
if [ -x "$(command -v yum)" ]; then
  sudo dnf update -y || sudo yum update -y
  sudo dnf install -y docker || sudo yum install -y docker
else
  sudo apt-get update -y
  sudo apt-get install -y docker.io
fi

sudo systemctl enable docker
sudo systemctl start docker

# Allow docker without sudo
sudo usermod -aG docker ec2-user || true
sudo usermod -aG docker ubuntu || true

# Run Entry Service container
docker pull ${docker_image}
docker rm -f entry-service || true
docker run -d \
  --name entry-service \
  -p 80:8081 \
  --restart always \
  ${docker_image}
