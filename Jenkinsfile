pipeline {
    agent any
    environment {
        REGISTRY = 'myrepo'
    }
    stages {
        stage('Detect Changed Services') {
            steps {
                script {
                    def changedFiles = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split("\\n")
                    def servicesToBuild = []

                    changedFiles.each { file ->
                        if (file.startsWith('services/')) {
                            def service = file.split('/')[1]
                            if (!servicesToBuild.contains(service)) {
                                servicesToBuild.add(service)
                            }
                        }
                    }

                    env.SERVICES_TO_BUILD = servicesToBuild.join(' ')
                    echo "Changed services: ${env.SERVICES_TO_BUILD}"
                }
            }
        }

        stage('Build & Test') {
            when {
                expression { return env.SERVICES_TO_BUILD != '' }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        sh "cd services/${service} && mvn clean package -DskipTests"
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            when {
                expression { return env.SERVICES_TO_BUILD != '' }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        sh "docker build -t ${REGISTRY}/${service}:latest services/${service}"
                        sh "docker push ${REGISTRY}/${service}:latest"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { return env.SERVICES_TO_BUILD != '' }
            }
            steps {
                script {
                    env.SERVICES_TO_BUILD.split(' ').each { service ->
                        sh "kubectl apply -f deployment/kubernetes/${service}-deployment.yaml"
                    }
                }
            }
        }
    }
}
