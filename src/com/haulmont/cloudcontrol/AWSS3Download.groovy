package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars

class AWSS3Download implements Action, Serializable {

    private static String CONTAINER = 'aws'

    @Override
    void action(def script) {
        script.sh "aws s3 cp s3://${script.env[GlobalVars.BUCKET_NAME]}/terraform /data --recursive"
        script.sh "cd /data && ls -lsa"
    }

    @Override
    void rollback(def script) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
