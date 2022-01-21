package com.haulmont.cloudcontrol

class AWSS3Download implements Action {

    private static String CONTAINER = 'aws'

    @Override
    void action(script, parameters) {
        script.sh "aws s3 cp s3://${parameters[GlobalVars.BUCKET_NAME]}/terraform /data --recursive"
        script.sh "cd /data && ls -lsa"
    }

    @Override
    void rollback(script, parameters) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }
}
