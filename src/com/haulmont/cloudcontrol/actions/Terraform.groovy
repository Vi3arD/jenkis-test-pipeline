package com.haulmont.cloudcontrol.actions

class Terraform implements Action, Serializable {

    private static String CONTAINER = 'terraform'

    @Override
    void action(def script) {
        script.sh "echo TERRAFORM ACTION"
        script.sh "printenv"
        throw new RuntimeException("i'am error")
    }

    @Override
    void rollback(def script) {
        script.sh "echo TERRAFORM ROLLBACK"
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

}
