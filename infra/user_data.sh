#!/bin/bash

sudo su
yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user

docker run -p 127.0.0.1:80 kievmaia/public-api:latest