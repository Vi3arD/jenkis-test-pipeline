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

    def arr = []
    arr.add(containerTemplate(name: 'terraform', image: 'hashicorp/terraform:1.0.6', command: 'sleep', args: '99d'))
    arr.add(containerTemplate(name: 'ansible', image: 'ansible/ansible-runner:1.4.7', command: 'sleep', args: '99d'))
    arr.add(containerTemplate(name: 'aws', image: 'amazon/aws-cli:2.4.12', command: 'sleep', args: '99d'))


    podTemplate(containers: arr) {
        node(POD_LABEL) {

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
    }
}