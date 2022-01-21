package com.haulmont.cloudcontrol

class AWSS3Download implements Action {

    private static String CONTAINER = 'aws'

    @Override
    void action(parameters) {
//        sh("aws s3 cp s3://" + parameters[GlobalVars.BUCKET_NAME] + "/terraform /data --recursive")
//        sh('cd /data && ls -lsa')
        sh('echo pwd')
    }

    @Override
    void rollback(parameters) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }
}
