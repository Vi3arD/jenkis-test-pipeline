package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class RegisterDomain implements Action, Serializable {

    private static String CONTAINER = "alpine"
    private static String IMAGE = "alpine:3.15.0"

    @Override
    void action(def script) {
//        def response = httpRequest consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', httpMode: 'GET', customHeaders: [[name: 'X-Jenkins-Token', value: "${script.env[GlobalVars.CLOUD_CONTROL_AUTH_TOKEN]}"]], url: "${script.env[GlobalVars.CALLBACK_URL]}/rest/jenkins/register_domain?workspaceId=${script.env[GlobalVars.WORKSPACE_ID]}&deploymentId=${script.env[GlobalVars.DEPLOYMENT_ID]}&instanceIp=${script.env[GlobalVars.INSTANCE_IP]}", validResponseCodes: '200'
//        if (response.status != "200") {
//            throw new RuntimeException("Error during register domain")
//        }
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
