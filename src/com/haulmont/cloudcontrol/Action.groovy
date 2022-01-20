package src.com.haulmont.cloudcontrol

interface Action {

    void action(parameters)

    void rollback(parameters)

}