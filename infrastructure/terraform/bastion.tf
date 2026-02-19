resource "aws_instance" "bastion" {
  ami                    = data.aws_ami.amazon_linux.id
  instance_type          = var.bastion_instance_type  
  subnet_id              = aws_subnet.public_bastion.id
  vpc_security_group_ids = [aws_security_group.bastion_sg.id]
  key_name               = var.key_name

  # USER-DATA CRÍTICO: Configura SSH automáticamente
  user_data = <<-EOF
    #!/bin/bash
    set -x  # Modo debug
    
    echo "=== INICIANDO CONFIGURACIÓN BASTION ==="
    
    # 1. Actualizar sistema
    yum update -y
    
    # 2. Instalar paquetes esenciales
    yum install -y curl wget telnet
    
    # 3. Configurar SSH
    echo "Configurando SSH..."
    
    # Backup de configuración original
    cp /etc/ssh/sshd_config /etc/ssh/sshd_config.backup
    
    # Configuración segura de SSH
    cat > /etc/ssh/sshd_config.d/99-custom.conf <<'SSHD_EOF'
    Port 22
    Protocol 2
    PasswordAuthentication no
    PermitRootLogin no
    PubkeyAuthentication yes
    AuthorizedKeysFile .ssh/authorized_keys
    UsePAM yes
    X11Forwarding no
    ClientAliveInterval 60
    ClientAliveCountMax 3
    MaxAuthTries 3
    MaxSessions 10
    SSHD_EOF
    
    # 4. Asegurar permisos
    chmod 600 /etc/ssh/sshd_config.d/99-custom.conf
    
    # 5. Reiniciar SSH
    systemctl restart sshd
    systemctl enable sshd
    
    # 6. Verificar que SSH está corriendo
    systemctl status sshd
    
    # 7. Crear archivo de log
    echo "Bastion configurado exitosamente en: $(date)" >> /var/log/bastion-setup.log
    echo "Hostname: $(hostname)" >> /var/log/bastion-setup.log
    echo "IP: $(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)" >> /var/log/bastion-setup.log
    
    echo "=== CONFIGURACIÓN COMPLETADA ==="
  EOF

  tags = merge(local.common_tags, {
    Name = "bastion-${local.environment}"
  })
  
  # Metadata para troubleshooting
  metadata_options {
    http_endpoint = "enabled"
    http_tokens   = "optional"
  }
}