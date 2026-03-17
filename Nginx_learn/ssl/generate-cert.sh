#!/bin/sh

# Tạo self-signed SSL certificate cho development
# Certificate có hiệu lực 365 ngày

if [ ! -f /etc/nginx/ssl/server.key ]; then
    echo ">>> Generating self-signed SSL certificate..."
    mkdir -p /etc/nginx/ssl

    openssl req -x509 -nodes -days 365 \
        -newkey rsa:2048 \
        -keyout /etc/nginx/ssl/server.key \
        -out /etc/nginx/ssl/server.crt \
        -subj "/C=VN/ST=HoChiMinh/L=HoChiMinh/O=Dev/CN=localhost"

    echo ">>> SSL certificate generated successfully!"
else
    echo ">>> SSL certificate already exists, skipping..."
fi
