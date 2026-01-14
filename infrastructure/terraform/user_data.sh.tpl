#!/bin/bash
# user-data de la instancia: instala docker y ejecuta la imagen del Docker Hub en el puerto 80 del host
set -e

# Actualizar e instalar docker (Amazon Linux 2 / Amazon Linux 2023)
if [ -x "$(command -v yum)" ]; then
  sudo dnf update -y || sudo yum update -y
  sudo dnf install -y docker || sudo yum install -y docker
else
  sudo apt-get update -y
  sudo apt-get install -y docker.io
fi

sudo systemctl enable docker
sudo systemctl start docker

# Permitir al usuario ec2-user/ubuntu usar docker (por si necesitas)
sudo usermod -aG docker ec2-user || true
sudo usermod -aG docker ubuntu || true

# Pull y run de la imagen (exponer puerto 80 en host que hace forward al puerto 3000 del contenedor)
# Nota: adapta el puerto si tu app usa otro puerto interno
docker pull ${docker_image}
docker rm -f practica-hola-mundo || true
docker run -d --name practica-hola-mundo -p 80:3000 --restart always ${docker_image}

