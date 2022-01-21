#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.ClassKeeper
import com.haulmont.cloudcontrol.ParserUtils
import com.haulmont.cloudcontrol.Terraform
import com.haulmont.cloudcontrol.GlobalVars

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    ParserUtils.toEnv(this, structure[GlobalVars.ENV])

    int currentStep
    Map beans = ClassKeeper.getBeans()

//    try {
        for (currentStep = 0; currentStep < structure[GlobalVars.ACTIONS].size(); currentStep++) {
            def executor = beans.get(structure[GlobalVars.ACTIONS][currentStep][GlobalVars.EXECUTOR])
            container(executor.getContainerName()) {
                executor.action(this)
            }
        }
//    } catch (Exception e) {
//        script.sh "error -> ${e}"
    }

}