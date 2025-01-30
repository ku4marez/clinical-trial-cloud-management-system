# Clinical Trial Cloud Management System

This is a **monorepo** containing all microservices for managing clinical trials.

## Microservices
- **User Service** - Authentication, Roles
- **Trial Service** - Manage trials
- **Patient Service** - Handle patient data
- **Notification Service** - Kafka-driven notifications
- **Document Service** - AWS S3 for trial docs
- **Report Service** - Data analytics & reporting
- **API Gateway** - Routes requests to services
- **Config Server** - Centralized configuration management

## Deployment
- **Docker** for containerization
- **Kubernetes** for orchestration
- **Jenkins** for CI/CD automation
- **AWS S3, DynamoDB** for cloud storage

## Getting Started
1. Clone the repo
2. Run `docker-compose up` for local testing
3. Deploy with `kubectl apply -f deployment/kubernetes/`