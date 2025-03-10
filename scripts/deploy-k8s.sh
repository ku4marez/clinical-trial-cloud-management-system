#!/bin/bash

echo "Deploying services to Kubernetes..."

SERVICES=("api-gateway" "config-server" "document-service" "eureka-server" "grafana-service" "patient-service" "prometheus" "report-service" "trial-service")

for SERVICE in "${SERVICES[@]}"
do
  echo "Deploying $SERVICE..."
  kubectl apply -f kubernetes/$SERVICE/$SERVICE-deployment.yaml
  kubectl apply -f kubernetes/$SERVICE/$SERVICE-service.yaml
done

echo "All services deployed successfully!"
