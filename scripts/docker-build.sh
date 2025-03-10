#!/bin/bash

# Set variables
REGISTRY="your-docker-registry"
SERVICES=("api-gateway" "config-server" "document-service" "eureka-server" "grafana-service" "patient-service" "prometheus" "report-service" "trial-service")

echo "Building and pushing Docker images..."

for SERVICE in "${SERVICES[@]}"
do
  echo "Building $SERVICE..."
  docker build -t $REGISTRY/$SERVICE:latest -f docker/$SERVICE/Dockerfile .

  echo "Pushing $SERVICE..."
  docker push $REGISTRY/$SERVICE:latest
done

echo "All Docker images pushed successfully!"
