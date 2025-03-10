#!/bin/bash

echo "Setting up local environment..."

# Copy environment variables
cp .env.example .env

# Install dependencies
npm install

echo "Local environment setup complete."
