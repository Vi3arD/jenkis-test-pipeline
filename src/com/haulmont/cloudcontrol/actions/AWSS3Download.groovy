package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class AWSS3Download implements Action, Serializable {

    private static String CONTAINER = 'aws'

    @Override
    void action(def script) {
//        script.sh "aws s3 cp s3://${script.env[GlobalVars.BUCKET_NAME]}/terraform /data --recursive"
//        script.sh "cd /data && ls -lsa"

        script.container(CONTAINER) {
            script.sh "echo AWSS3Download"
            script.sh "echo AWSS3Download"
            script.sh "echo AWSS3Download"
            script.sh "echo AWSS3Download"
            script.sh "echo AWSS3Download"
        }
    }

    @Override
    void rollback(def script) {
        script.sh "echo AWSS3Download rollback"
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
