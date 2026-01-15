#!/bin/bash
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /opt/parking-domain
cd /opt/parking-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  parking-space-service:
    image: ${dockerhub_user}/${parking_space_service_image}
    ports:
      - "8084:8080"

  parking-lot-service:
    image: ${dockerhub_user}/${parking_lot_service_image}
    ports:
      - "8085:8080"
EOF

docker-compose pull
docker-compose up -d

