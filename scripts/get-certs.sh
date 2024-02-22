#!/usr/bin/env sh

openssl s_client -connect localhost:8443 </dev/null 2>/dev/null | openssl x509