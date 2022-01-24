package com.haulmont.cloudcontrol.actions

class Ansible implements Action, Serializable {

    private static String CONTAINER = 'ansible'

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

}