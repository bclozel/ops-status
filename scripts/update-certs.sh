#!/usr/bin/env sh

echo ""
echo "=================================================================="
echo "Refresh certificate under certs/example-org.crt"
echo "=================================================================="
echo ""

openssl req -batch -key certs/example-org.key -new -x509 -days 365 -out certs/example-org.crt
