#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

def call(String request) {

    def newFile = new File("/resources/test.txt")
    newFile.createNewFile()

    def structure = readJSON text: request, returnPojo: true
    Utils.toEnv(this, structure[GlobalVars.ENV])

    String flowStatus = "success"
    int currentStep
    int size = structure[GlobalVars.ACTIONS].size()

    if (GlobalVars.INSTALL.equals(structure[GlobalVars.TYPE])) {
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
        for (currentStep = size - 1; currentStep >= 0; currentStep--) {
            Utils.make(this, structure[GlobalVars.ACTIONS][currentStep], true)
        }
    }

    Notifier.send(this, flowStatus)
}