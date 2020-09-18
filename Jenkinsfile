pipeline {
    agent any

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "./gradlew clean build -x test"
            }
        }
        stage('Api') {
            stages {
                stage('Build Image') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker build api/ -t hub.tisserv.net/ugm-api:v${env.BUILD_NUMBER}"
                    }
                }
                stage('Push Image') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker push hub.tisserv.net/ugm-api:v${env.BUILD_NUMBER}"
                    }
                }
                stage('Run Docker') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker kill ugm-api || true"
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker rm ugm-api || true"
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker run -d --name ugm-api -p 8101:8080 hub.tisserv.net/ugm-api:v${env.BUILD_NUMBER}"
                    }
                }
            }
        }
        stage('Vaadin UI') {
            stages {
                stage('Build Image') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker build ui/ -t hub.tisserv.net/ugm-ui-old:v${env.BUILD_NUMBER}"
                    }
                }
                stage('Push Image') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker push hub.tisserv.net/ugm-ui-old:v${env.BUILD_NUMBER}"
                    }
                }
                stage('Run Docker') {
                    steps {
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker kill ugm-ui-old || true"
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker rm ugm-ui-old || true"
                        sh "eval \$(cat /var/lib/jenkins/.docker/env.sh); docker run -d --name ugm-ui-old -p 8102:8080 hub.tisserv.net/ugm-ui-old:v${env.BUILD_NUMBER}"
                    }
                }
            }
        }
    }
}
