#!/bin/bash
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /opt/security-domain
cd /opt/security-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  auth-service:
    image: ${dockerhub_user}/${auth_service_image}
    ports:
      - "8081:8080"

  user-service:
    image: ${dockerhub_user}/${user_service_image}
    ports:
      - "8082:8080"
EOF

docker-compose pull
docker-compose up -d
