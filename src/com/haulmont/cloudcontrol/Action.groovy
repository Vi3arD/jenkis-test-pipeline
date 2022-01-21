package com.haulmont.cloudcontrol

interface Action {

    void action(def script)

    void rollback(def script)

    String getContainerName()
}