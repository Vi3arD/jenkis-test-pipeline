package com.haulmont.cloudcontrol

class AWSS3Download implements Action {

    private static String CONTAINER = 'aws'

    @Override
    void action(script, parameters) {
        script.sh "export AWS_REGION=${parameters[GlobalVars.AWS_REGION][0]} " +
                "&& export AWS_ACCESS_KEY_ID=${parameters[GlobalVars.AWS_ACCESS_KEY_ID] as String} " +
                "&& export AWS_SECRET_ACCESS_KEY=${parameters[GlobalVars.AWS_SECRET_ACCESS_KEY as String]}"
        script.sh "aws s3 cp s3://${parameters[GlobalVars.BUCKET_NAME] as String}/terraform /data --recursive"
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
