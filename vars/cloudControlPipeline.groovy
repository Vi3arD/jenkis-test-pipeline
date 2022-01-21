#!/usr/bin/env groovy
package vars

import groovy.json.JsonSlurperClassic
import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.Terraform
import com.haulmont.cloudcontrol.GlobalVars

def call(String request) {
    def structure = readJSON text: request, returnPojo: true
    AWSS3Download awss3Download = new AWSS3Download()
    Terraform terraform = new Terraform()
    container(awss3Download.getContainerName()) {
        awss3Download.action(this, structure[GlobalVars.ENV])
    }
    container(terraform.getContainerName()) {
        terraform.action(this, structure[GlobalVars.ENV])
    }
}