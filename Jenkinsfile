pipeline {
    agent any
    environment {
        REGISTRY = 'ku4marez'
    }
    stages {
        stage('Detect Changed Services') {
            steps {
                script {
                    def changedFiles = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim().tokenize("\n")
                    def servicesToBuild = []

                    changedFiles.each { file ->
                        if (file.startsWith('services/')) {
                            def service = file.split('/')[1]
                            if (!servicesToBuild.contains(service)) {
                                servicesToBuild.add(service)
                            }
                        }
                    }

                    env.SERVICES_TO_BUILD = servicesToBuild ? servicesToBuild.join(' ') : ""
                    echo "Detected changed services: ${env.SERVICES_TO_BUILD}"
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'docker-hub-password', variable: 'DOCKER_HUB_PASSWORD')]) {
                        sh "echo ${DOCKER_HUB_PASSWORD} | docker login -u ${REGISTRY} --password-stdin"
                    }
                }
            }
        }

        stage('Build & Test') {
            when {
                expression { return env.SERVICES_TO_BUILD?.trim() }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        echo "Building and testing ${service}..."
                        sh "cd services/${service} && mvn clean package -DskipTests"
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            when {
                expression { return env.SERVICES_TO_BUILD?.trim() }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        echo "Building and pushing Docker image for ${service}..."
                        sh "docker build -t ${REGISTRY}/myrepo:${service}-latest services/${service}"
                        sh "docker push ${REGISTRY}/myrepo:${service}-latest"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { return env.SERVICES_TO_BUILD?.trim() }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        echo "Deploying ${service} to Kubernetes..."
                        sh "kubectl apply -f deployment/kubernetes/${service}-deployment.yaml"
                    }
                }
            }
        }
    }
}
