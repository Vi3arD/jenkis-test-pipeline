package com.haulmont.cloudcontrol

class Terraform implements Action, Serializable {

    private static String CONTAINER = 'terraform'

    @Override
    void action(def script) {
        script.sh "ALIVE!!!!!!!!!!"
        script.sh "echo ${script.env.AWS_REGION}"
        script.sh "printenv ${script.env.AWS_REGION}"
    }

    @Override
    void rollback(def script) {
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
