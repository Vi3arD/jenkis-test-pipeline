package com.haulmont.cloudcontrol

class Terraform implements Action {

    private static String CONTAINER = 'terraform'

    @Override
    void action(script, parameters) {
        script.sh "printenv"
        script.sh "echo ${script.env.AWS_REGION}"
    }

    @Override
    void rollback(script, parameters) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
