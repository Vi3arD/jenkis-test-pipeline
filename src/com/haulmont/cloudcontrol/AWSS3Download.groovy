package com.haulmont.cloudcontrol

class AWSS3Download implements Action {

    private static String CONTAINER = 'aws'

    @Override
    void action(parameters) {
        container(CONTAINER) {
            sh("aws s3 cp s3://" + parameters[GlobalVars.BUCKET_NAME] + "/terraform /data --recursive")
            sh('cd /data && ls -lsa')
        }
    }

    @Override
    void rollback(parameters) {
    }
}
