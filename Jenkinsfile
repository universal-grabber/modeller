node {
    deleteDir()
    APP_NAME="ugm-api"
    RESOURCE_NAME="ugm-api"
    NAMESPACE="default"

    try {
        stage ('Checkout') {
        	checkout scm
        }
        stage ('Build Project') {
            sh './gradlew clean build -x test --no-daemon --info'
        }
        stage ('Install') {
            sh './gradlew clean install'
        }

//         if (env.BRANCH_NAME == 'master'){
            stage ('Build Image') {
                sh './gradlew bootJar'
				sh "docker build -t hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER} api"
            }

      	    stage ('Push&Clean Image') {
				sh "docker push hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER}"
				sh "docker rmi -f hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER}"
			}

            stage ('deploy') {
                sh '''
                    cd infra

                    terraform init
                    terraform validate .
                    terraform plan -var DOCKER_IMG_TAG=v${BUILD_NUMBER}
                    terraform apply -var DOCKER_IMG_TAG=v${BUILD_NUMBER} -auto-approve
                '''
           }
//         }
    } catch (err) {
        throw err
    }
}
