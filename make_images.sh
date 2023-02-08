#!/bin/bash

cd music_microservice
mvn clean compile package
docker build -t music_microservice . --no-cache

cd ../playlist_microservice
mvn clean compile package
docker build -t playlist_microservice . --no-cache

cd ../gateway_microservice
mvn clean compile package
docker build -t gateway_microservice . --no-cache

docker-compose up