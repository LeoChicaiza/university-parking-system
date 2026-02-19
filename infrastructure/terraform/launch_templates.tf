# =====================================================
# SECURITY DOMAIN
# =====================================================
resource "aws_launch_template" "security_domain_lt" {
  name_prefix   = "security-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(<<-EOF
#!/bin/bash
set -e

# 1. Configurar repositorio LOCAL para evitar problemas de internet
cat <<'REPO' | sudo tee /etc/yum.repos.d/amzn2-local.repo
[amzn2-core-local]
name=Amazon Linux 2 core repository
baseurl=https://amazonlinux-2-repos-us-east-1.s3.us-east-1.amazonaws.com/2/core/latest/x86_64/
enabled=1
gpgcheck=0
metadata_expire=300
REPO

# 2. Instalar NGINX desde repositorio local
sudo yum install -y nginx --disablerepo="*" --enablerepo="amzn2-core-local"

# 3. Configurar NGINX para responder 200 en cualquier request
cat <<'NGINX' | sudo tee /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check endpoint
    location /health {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Cualquier otra ruta - siempre 200
    location / {
        return 200 '{"service":"running"}';
        add_header Content-Type application/json;
    }
}
NGINX

# 4. Iniciar NGINX
sudo systemctl start nginx
sudo systemctl enable nginx

# 5. Verificar que funciona
sleep 5
curl -s -f http://localhost/health > /dev/null && echo "✅ NGINX funcionando" || echo "⚠️  NGINX instalado pero verificación falló"
EOF
  )
}

# =====================================================
# VEHICLE DOMAIN
# =====================================================
resource "aws_launch_template" "vehicle_domain_lt" {
  name_prefix   = "vehicle-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(<<-EOF
#!/bin/bash
set -e

# 1. Configurar repositorio LOCAL para evitar problemas de internet
cat <<'REPO' | sudo tee /etc/yum.repos.d/amzn2-local.repo
[amzn2-core-local]
name=Amazon Linux 2 core repository
baseurl=https://amazonlinux-2-repos-us-east-1.s3.us-east-1.amazonaws.com/2/core/latest/x86_64/
enabled=1
gpgcheck=0
metadata_expire=300
REPO

# 2. Instalar NGINX desde repositorio local
sudo yum install -y nginx --disablerepo="*" --enablerepo="amzn2-core-local"

# 3. Configurar NGINX para responder 200 en cualquier request
cat <<'NGINX' | sudo tee /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check endpoint
    location /health {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Cualquier otra ruta - siempre 200
    location / {
        return 200 '{"service":"running"}';
        add_header Content-Type application/json;
    }
}
NGINX

# 4. Iniciar NGINX
sudo systemctl start nginx
sudo systemctl enable nginx

# 5. Verificar que funciona
sleep 5
curl -s -f http://localhost/health > /dev/null && echo "✅ NGINX funcionando" || echo "⚠️  NGINX instalado pero verificación falló"
EOF
  )
}

# =====================================================
# PARKING DOMAIN
# =====================================================
resource "aws_launch_template" "parking_domain_lt" {
  name_prefix   = "parking-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(<<-EOF
#!/bin/bash
set -e

# 1. Configurar repositorio LOCAL para evitar problemas de internet
cat <<'REPO' | sudo tee /etc/yum.repos.d/amzn2-local.repo
[amzn2-core-local]
name=Amazon Linux 2 core repository
baseurl=https://amazonlinux-2-repos-us-east-1.s3.us-east-1.amazonaws.com/2/core/latest/x86_64/
enabled=1
gpgcheck=0
metadata_expire=300
REPO

# 2. Instalar NGINX desde repositorio local
sudo yum install -y nginx --disablerepo="*" --enablerepo="amzn2-core-local"

# 3. Configurar NGINX para responder 200 en cualquier request
cat <<'NGINX' | sudo tee /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check endpoint
    location /health {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Cualquier otra ruta - siempre 200
    location / {
        return 200 '{"service":"running"}';
        add_header Content-Type application/json;
    }
}
NGINX

# 4. Iniciar NGINX
sudo systemctl start nginx
sudo systemctl enable nginx

# 5. Verificar que funciona
sleep 5
curl -s -f http://localhost/health > /dev/null && echo "✅ NGINX funcionando" || echo "⚠️  NGINX instalado pero verificación falló"
EOF
  )
}

# =====================================================
# ACCESS DOMAIN
# =====================================================
resource "aws_launch_template" "access_domain_lt" {
  name_prefix   = "access-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(<<-EOF
#!/bin/bash
set -e

# 1. Configurar repositorio LOCAL para evitar problemas de internet
cat <<'REPO' | sudo tee /etc/yum.repos.d/amzn2-local.repo
[amzn2-core-local]
name=Amazon Linux 2 core repository
baseurl=https://amazonlinux-2-repos-us-east-1.s3.us-east-1.amazonaws.com/2/core/latest/x86_64/
enabled=1
gpgcheck=0
metadata_expire=300
REPO

# 2. Instalar NGINX desde repositorio local
sudo yum install -y nginx --disablerepo="*" --enablerepo="amzn2-core-local"

# 3. Configurar NGINX para responder 200 en cualquier request
cat <<'NGINX' | sudo tee /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check endpoint
    location /health {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Cualquier otra ruta - siempre 200
    location / {
        return 200 '{"service":"running"}';
        add_header Content-Type application/json;
    }
}
NGINX

# 4. Iniciar NGINX
sudo systemctl start nginx
sudo systemctl enable nginx

# 5. Verificar que funciona
sleep 5
curl -s -f http://localhost/health > /dev/null && echo "✅ NGINX funcionando" || echo "⚠️  NGINX instalado pero verificación falló"
EOF
  )
}

# =====================================================
# BILLING DOMAIN
# =====================================================
resource "aws_launch_template" "billing_domain_lt" {
  name_prefix   = "billing-domain-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  key_name      = var.key_name

  user_data = base64encode(<<-EOF
#!/bin/bash
set -e

# 1. Configurar repositorio LOCAL para evitar problemas de internet
cat <<'REPO' | sudo tee /etc/yum.repos.d/amzn2-local.repo
[amzn2-core-local]
name=Amazon Linux 2 core repository
baseurl=https://amazonlinux-2-repos-us-east-1.s3.us-east-1.amazonaws.com/2/core/latest/x86_64/
enabled=1
gpgcheck=0
metadata_expire=300
REPO

# 2. Instalar NGINX desde repositorio local
sudo yum install -y nginx --disablerepo="*" --enablerepo="amzn2-core-local"

# 3. Configurar NGINX para responder 200 en cualquier request
cat <<'NGINX' | sudo tee /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;
    
    # Health check endpoint
    location /health {
        return 200 '{"status":"UP"}';
        add_header Content-Type application/json;
    }
    
    # Cualquier otra ruta - siempre 200
    location / {
        return 200 '{"service":"running"}';
        add_header Content-Type application/json;
    }
}
NGINX

# 4. Iniciar NGINX
sudo systemctl start nginx
sudo systemctl enable nginx

# 5. Verificar que funciona
sleep 5
curl -s -f http://localhost/health > /dev/null && echo "✅ NGINX funcionando" || echo "⚠️  NGINX instalado pero verificación falló"
EOF
  )
}