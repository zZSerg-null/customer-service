@Library('zinoviev-shared-library') _

pipeline {
    agent {
        label 'master'
    }

    environment {
        IMAGE_NAME = 'zinoviev-customer-service'
        IMAGE_TAG = 'develop'
        DOCKERFILE_PATH = 'Dockerfile'
        json_key = credentials('yandex_pass')
        registryName = "cr.yandex/crpqhm9qhte79o09rkl7"
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {
        stage('Unit & Integration Tests, SonarQube, Build') {
            tools {
                jdk 'jdk1.7'
                }
            steps {
                script {
                    buildGradle 'test sonar build'
                }
            }
            post {
                always {
                junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Build Docker image') {
            steps {
                sh 'docker build -f ${DOCKERFILE_PATH} -t ${registryName}/${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Push Docker image to the Container Registry') {
            steps {
                sh """
                    cat ${json_key} | docker login --username json_key --password-stdin cr.yandex
                    docker push ${registryName}/${IMAGE_NAME}:${IMAGE_TAG}
                    docker logout
                """
            }
        }

        stage('Remove unused Docker image from Jenkins Node') {
            steps {
                sh 'docker rmi -f ${registryName}/${IMAGE_NAME}:${IMAGE_TAG}'
            }
        }

        stage('Deploy to Kubernetes') {
            agent {
                docker { image 'dtzar/helm-kubectl'
                args '-u root --privileged'
                }
            }
            steps {
                withCredentials([file(credentialsId: 'k8s-config-zinoviev', variable: 'config')]) {
                    sh 'mkdir ~/.kube'
                    sh "cp \$config ~/.kube/config && chmod 600 ~/.kube/config"
                    sh 'kubectl config use-context jenkins@customer'
                    sh 'kubectl patch deployment/customer-service -p "{\\"spec\\":{\\"template\\":{\\"metadata\\":{\\"annotations\\":{\\"Commit\\":\\"${GIT_COMMIT}\\"}}}}}" -n dev'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
