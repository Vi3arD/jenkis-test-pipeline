package com.haulmont.cloudcontrol

interface Action {

    void action(parameters)

    void rollback(parameters)

    String getContainerName()
}