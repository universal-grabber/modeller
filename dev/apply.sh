#!/bin/sh

curl -X POST --data-binary "@$1" -H "Accepts: application/yaml" -H "Content-type: application/yaml" "http://localhost:8181/api/1.0/schemas?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0YWxlaCIsImV4cCI6MTkxODQ2MTYwN30.GyMJCuJtB9nHEo7aa96dHmqrwVKQbbNJRC7FQkIKv8uCmlGTR7LvlBBE8ATKAwbLmGbKuVpD7GoOwgYu_wKM9w"
