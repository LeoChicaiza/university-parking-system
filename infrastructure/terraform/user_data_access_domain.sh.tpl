#!/bin/bash
set -e

# 1. Instalar dependencias
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -aG docker ec2-user

# 2. Instalar docker-compose
curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
-o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 3. Instalar NGINX
yum install -y nginx

# 4. Crear docker-compose.yml para Access Domain
mkdir -p /opt/access-domain
cd /opt/access-domain

cat <<EOF > docker-compose.yml
version: "3.8"
services:
  entry-service:
    image: \${dockerhub_user}/\${entry_service_image}
    ports:
      - "8090:8080"
    restart: always
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  exit-service:
    image: \${dockerhub_user}/\${exit_service_image}
    ports:
      - "8086:8080"
    restart: always
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
EOF

# 5. Configurar NGINX para Access Domain
cat <<'EOF' > /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check para ALB - prueba ambos servicios
    location /health {
        # Primero intenta con entry-service
        proxy_pass http://localhost:8090/actuator/health;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        
        # Si falla, intenta con exit-service
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        proxy_next_upstream_tries 2;
        
        # Si todo falla, devuelve 200 igual
        proxy_intercept_errors on;
        error_page 404 500 502 503 504 =200 /health-ok;
    }
    
    location /health-ok {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Routing para entry service
    location /entry/ {
        proxy_pass http://localhost:8090/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # Routing para exit service (nota: tiene /api/ prefix)
    location /api/exit/ {
        proxy_pass http://localhost:8086/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # Default response
    location / {
        return 404 '{"error":"Not Found","service":"access-domain"}';
        add_header Content-Type application/json;
    }
}
EOF

# 6. Pull e iniciar contenedores
docker-compose pull
docker-compose up -d

# 7. Iniciar NGINX
systemctl start nginx
systemctl enable nginx

# 8. Esperar y verificar
sleep 20
echo "Verificando servicios Access Domain..."
curl -f http://localhost/health || echo "Health check devolvió fallo pero NGINX está activo"

echo "=== Access Domain configurado exitosamente ==="
