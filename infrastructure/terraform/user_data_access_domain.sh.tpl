#!/bin/bash
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /opt/access-domain
cd /opt/access-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  entry-service:
    image: ${dockerhub_user}/${entry_service_image}
    ports:
      - "8086:8080"

  exit-service:
    image: ${dockerhub_user}/${exit_service_image}
    ports:
      - "8087:8080"
EOF

docker-compose pull
docker-compose up -d
