package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars

class AWSS3Download implements Action, Serializable {

    private static String CONTAINER = 'aws'

    @Override
    void action(script, parameters) {

//        def clz = Class.forName("Terraform", true, this.class.classLoader)
//        container(clz.getContainerName()) {
//            clz.action(this, structure[GlobalVars.ENV])
//        }

        script.env.AWS_REGION = parameters[GlobalVars.AWS_REGION][0]
        script.env.AWS_ACCESS_KEY_ID = parameters[GlobalVars.AWS_ACCESS_KEY_ID][0]
        script.env.AWS_SECRET_ACCESS_KEY = parameters[GlobalVars.AWS_SECRET_ACCESS_KEY][0]
//        script.sh "export AWS_REGION=${parameters[GlobalVars.AWS_REGION][0]} " +
//                "&& export AWS_ACCESS_KEY_ID=${parameters[GlobalVars.AWS_ACCESS_KEY_ID][0]} " +
//                "&& export AWS_SECRET_ACCESS_KEY=${parameters[GlobalVars.AWS_SECRET_ACCESS_KEY][0]}"
        script.sh "aws s3 cp s3://${parameters[GlobalVars.BUCKET_NAME][0]}/terraform /data --recursive"
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
