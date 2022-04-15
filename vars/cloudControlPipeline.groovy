#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.Notifier
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.ActionConfigurations

def call(String request) {
    env[GlobalVars.CLOUD_CONTROL_CONTEXT] = request
    def structure = readJSON text: request, returnPojo: true
    env[GlobalVars.CLOUD_CONTROL_CONTEXT] = structure
    echo "structure -> ${env[GlobalVars.CLOUD_CONTROL_CONTEXT]}"
    Utils.toEnv(this, structure)

    def configurations = ActionConfigurations.getConfiguration(env[GlobalVars.JOB] as String)
    podTemplate(containers: Utils.getContainers(this, configurations),
            volumes: [emptyDirVolume(mountPath: '/shared')]
    ) {
        node(POD_LABEL) {
            int currentStep
            int size = configurations.size()
            String status = GlobalVars.SUCCESS

            if (GlobalVars.TRUE == env[GlobalVars.DIRECTION]) {
                try {
                    for (currentStep = 0; currentStep < size; currentStep++) {
                        echo "action ${configurations[currentStep]}"
                        Utils.make(this, configurations[currentStep])
                    }
                } catch (Exception e) {
//                    for (currentStep; currentStep >= 0; currentStep--) {
//                        Utils.make(this, configurations[currentStep], true)
//                    }
                    status = GlobalVars.FAILED
                    echo "error -> ${e}"
                }
            } else if (GlobalVars.FALSE == structure[GlobalVars.DIRECTION]) {
                for (currentStep = 0; currentStep < size; currentStep++) {
                    Utils.make(this, configurations[currentStep], true)
                }
            }

            Notifier.send(this, status)
        }
    }
}