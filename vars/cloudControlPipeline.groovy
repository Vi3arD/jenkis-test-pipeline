#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.ClassKeeper
import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    Utils.toEnv(this, structure[GlobalVars.ENV])

    String flowStatus = "success"
    int currentStep
    int size = structure[GlobalVars.ACTIONS].size()
    Map beans = ClassKeeper.getBeans()

    if (GlobalVars.INSTALL.equals(structure[GlobalVars.TYPE])) {
        try {
            for (currentStep = 0; currentStep < size; currentStep++) {
                def action = structure[GlobalVars.ACTIONS][currentStep]
                def executor = beans.get(action[GlobalVars.EXECUTOR])
//                container(executor.getContainerName()) {
                    executor.action(this)
//                }
                Notifier.send(this, "status", action[GlobalVars.NAME] as String)
            }
        } catch (Exception e) {
            for (currentStep; currentStep >= 0; currentStep--) {
                def action = structure[GlobalVars.ACTIONS][currentStep]
                def executor = beans.get(action[GlobalVars.EXECUTOR])
                container(executor.getContainerName()) {
                    executor.rollback(this)
                }
                Notifier.send(this, "status", action[GlobalVars.NAME] as String)
            }
            flowStatus = "failed"
            echo "error -> ${e}"
        }
    } else if (GlobalVars.DESTROY.equals(structure[GlobalVars.TYPE])) {
        for (currentStep = size - 1; currentStep >= 0; currentStep--) {
            def action = structure[GlobalVars.ACTIONS][currentStep]
            def executor = beans.get(action[GlobalVars.EXECUTOR])
            container(executor.getContainerName()) {
                executor.rollback(this)
            }
            Notifier.send(this, "status", action[GlobalVars.NAME] as String)
        }
    }

    Notifier.send(this, flowStatus)

}