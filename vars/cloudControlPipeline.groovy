#!/usr/bin/env groovy
package vars

import com.haulmont.cloudcontrol.Utils
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    Utils.toEnv(this, structure[GlobalVars.ENV])

    String flowStatus = "success"
    int currentStep
    int size = structure[GlobalVars.ACTIONS].size()

    podTemplate(yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: terraform
      image: hashicorp/terraform:1.0.6
      command:
        - sleep
      args:
        - "99d"
      volumeMounts:
      - name: data
        mountPath: /data
    - name: ansible
      image: ansible/ansible-runner:1.4.7
      command:
        - sleep
      args:
        - "99d"
      volumeMounts:
      - name: data
        mountPath: /data
''')

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