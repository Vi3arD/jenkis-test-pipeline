package com.haulmont.cloudcontrol

class Terraform implements Action, Serializable {

    private static String CONTAINER = 'terraform'

    @Override
    void action(script, parameters) {
        script.sh "echo ${script.env.AWS_REGION}"
        script.sh "printenv ${script.env.AWS_REGION}"
    }

    @Override
    void rollback(script, parameters) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
