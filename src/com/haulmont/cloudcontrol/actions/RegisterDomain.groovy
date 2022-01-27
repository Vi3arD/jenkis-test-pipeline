package src.com.haulmont.cloudcontrol.actions

class RegisterDomain implements Action, Serializable {

    private static String CONTAINER = 'alpine'
    private static String IMAGE = 'latest'

    @Override
    void action(def script) {
        try {
            def response = httpRequest consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', httpMode: 'GET', customHeaders: [[name: 'X-Jenkins-Token', value: "${CLOUD_CONTROL_AUTH_TOKEN}"]], url: "${CALLBACK_URL}/rest/jenkins/failed?workspaceId=${WORKSPACE_ID}&deploymentId=${DEPLOYMENT_ID}", validResponseCodes: '200'

            return response.status == "200"
        } catch (err) {
            return false
        }
    }

    @Override
    void rollback(def script) {
        script.sh "echo RegisterDomain rollback not implemented"
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

    @Override
    String getImage() {
        return IMAGE
    }

}
