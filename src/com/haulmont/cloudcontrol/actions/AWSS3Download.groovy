package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class AWSS3Download implements Action, Serializable {

    private static String CONTAINER = 'aws-cli'
    private static String IMAGE = 'amazon/aws-cli:2.4.12'

    @Override
    void action(def script) {
        script.sh "aws s3 cp s3://${script.env[GlobalVars.BUCKET_NAME]}/terraform /data --recursive"
        script.sh "cd /data && ls -lsa"
    }

    @Override
    void rollback(def script) {
        script.sh "echo AWSS3Download rollback"
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
