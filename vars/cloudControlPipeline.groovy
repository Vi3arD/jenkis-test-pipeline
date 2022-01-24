#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.ClassKeeper
import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.GlobalVars

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    Utils.toEnv(this, structure[GlobalVars.ENV])

    int currentStep
    int size = structure[GlobalVars.ACTIONS].size()
    Map beans = ClassKeeper.getBeans()

    if (GlobalVars.INSTALL.equals(structure[GlobalVars.TYPE])) {
        try {
            for (currentStep = 0; currentStep < size; currentStep++) {
                def executor = beans.get(structure[GlobalVars.ACTIONS][currentStep][GlobalVars.EXECUTOR])
                container(executor.getContainerName()) {
                    executor.action(this)
                }
            }
        } catch (Exception e) {
            for (currentStep; currentStep >= 0; currentStep--) {
                def executor = beans.get(structure[GlobalVars.ACTIONS][currentStep][GlobalVars.EXECUTOR])
                container(executor.getContainerName()) {
                    executor.rollback(this)
                }
            }
            sh "echo 'error -> ${e}'"
        }
    } else if (GlobalVars.DESTROY.equals(structure[GlobalVars.TYPE])) {
        for (currentStep = size; currentStep >= 0; currentStep--) {
            def executor = beans.get(structure[GlobalVars.ACTIONS][currentStep][GlobalVars.EXECUTOR])
            container(executor.getContainerName()) {
                executor.rollback(this)
            }
        }
    }

}