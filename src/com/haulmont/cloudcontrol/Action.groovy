package com.haulmont.cloudcontrol

interface Action {

    void action(script, parameters)

    void rollback(script, parameters)

    String getContainerName()
}