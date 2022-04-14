package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class AWSS3Download implements Action, Serializable {

    private static String CONTAINER = "aws-cli"
    private static String IMAGE = "amazon/aws-cli:2.4.12"

    @Override
    void action(def script) {
        execute(script)
    }

    @Override
    void rollback(def script) {
        execute(script)
    }

    private void execute(def script) {
        String type = script.env[GlobalVars.DEPLOYMENT_TYPE].toLowerCase()
        script.sh "aws s3 cp s3://${script.env[GlobalVars.BUCKET_NAME]}/${type}/scripts /shared --recursive"
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
