#!/bin/bash

echo "Running Jenkins pipeline script..."

# Step 1: Build and push Docker images
./scripts/docker-build.sh

# Step 2: Deploy to Kubernetes
./scripts/deploy-k8s.sh

echo "Jenkins pipeline execution complete!"
