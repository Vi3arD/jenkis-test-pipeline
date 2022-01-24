package com.haulmont.cloudcontrol.actions

interface Action {

    void action(def script)

    void rollback(def script)

    String getContainerName()
}