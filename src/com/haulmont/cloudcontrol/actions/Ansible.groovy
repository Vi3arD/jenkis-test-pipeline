package com.haulmont.cloudcontrol.actions

class Ansible implements Action, Serializable {

    private static String CONTAINER = 'ansible'
    private static String IMAGE = 'ansible/ansible-runner:1.4.7'

    @Override
    void action(def script) {
        script.sh "echo ANSIBLE ACTION"
    }

    @Override
    void rollback(def script) {
        script.sh "echo ANSIBLE ROLLBACK"
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
