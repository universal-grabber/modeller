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

        if ((env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'master') && (env.GITLAB_OBJECT_KIND == 'none' || env.GITLAB_OBJECT_KIND == 'push')){
            stage ('Build Image') {
                sh './gradlew bootJar'
				sh "docker build -t hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER} ."
            }

      	    stage ('Push&Clean Image') {
				sh "docker push hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER}"
				sh "docker rmi -f hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER}"
			}

            stage ('deploy') {
               sh "/usr/local/bin/kubectl --kubeconfig /var/lib/jenkins/.kube/config --record deployment.apps/$RESOURCE_NAME set image deployment.apps/$RESOURCE_NAME $APP_NAME=hub.tisserv.net/$RESOURCE_NAME:v${env.BUILD_NUMBER} -n $NAMESPACE"
			   sh "/usr/local/bin/kubectl --kubeconfig /var/lib/jenkins/.kube/config rollout status deployment/$RESOURCE_NAME -n $NAMESPACE"
            }
        }
    } catch (err) {
        throw err
    }
}
