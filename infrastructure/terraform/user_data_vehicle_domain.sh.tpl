#!/bin/bash
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /opt/vehicle-domain
cd /opt/vehicle-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  vehicle-service:
    image: ${dockerhub_user}/${vehicle_service_image}
    ports:
      - "8083:8080"
EOF

docker-compose pull
docker-compose up -d
