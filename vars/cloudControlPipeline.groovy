#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    env[GlobalVars.TYPE] = structure[GlobalVars.TYPE]
    Utils.toEnv(this, structure[GlobalVars.ENV])

    podTemplate(containers: Utils.getContainers(this, structure[GlobalVars.ACTIONS]),
            volumes: [emptyDirVolume(mountPath: '/shared')]
    ) {
        node(POD_LABEL) {
            String flowStatus = "success"
            int currentStep
            int size = structure[GlobalVars.ACTIONS].size()

            if (GlobalVars.CREATE.equals(structure[GlobalVars.TYPE])) {
                try {
                    for (currentStep = 0; currentStep < size; currentStep++) {
                        Utils.make(this, structure[GlobalVars.ACTIONS][currentStep])
                    }
                } catch (Exception e) {
                    for (currentStep; currentStep >= 0; currentStep--) {
                        Utils.make(this, structure[GlobalVars.ACTIONS][currentStep], true)
                    }
                    flowStatus = "failed"
                    echo "error -> ${e}"
                }
            } else if (GlobalVars.DESTROY.equals(structure[GlobalVars.TYPE])) {
                for (currentStep = 0; currentStep < size; currentStep++) {
                    Utils.make(this, structure[GlobalVars.ACTIONS][currentStep], true)
                }
            }

            Notifier.send(this, flowStatus)
        }
    }
}