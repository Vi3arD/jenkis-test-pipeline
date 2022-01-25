package com.haulmont.cloudcontrol.actions

class Terraform implements Action, Serializable {

    private static String CONTAINER = 'terraform'
    private static String IMAGE = 'hashicorp/terraform:1.0.6'

    @Override
    void action(def script) {
        script.sh "echo TERRAFORM ACTION"
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

    @Override
    String getImage() {
        return IMAGE
    }

}
