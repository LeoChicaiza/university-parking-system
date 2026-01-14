#!/bin/bash
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /opt/billing-domain
cd /opt/billing-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  billing-service:
    image: ${dockerhub_user}/${billing_service_image}
    ports:
      - "8088:8080"

  notification-service:
    image: ${dockerhub_user}/${notification_service_image}
    ports:
      - "8089:8080"

  reporting-service:
    image: ${dockerhub_user}/${reporting_service_image}
    ports:
      - "8090:8080"
EOF

docker-compose pull
docker-compose up -d
