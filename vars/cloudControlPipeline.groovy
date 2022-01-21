#!/usr/bin/env groovy
package vars

import groovy.json.JsonSlurperClassic
import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.GlobalVars

def call(String request) {
//    def jsonSlurper = new JsonSlurperClassic()
    def structure = readJSON text: request, returnPojo: true
//    def structure = jsonSlurper.parseText(request)
    AWSS3Download awss3Download = new AWSS3Download()
//    echo structure[GlobalVars.ENV][GlobalVars.BUCKET_NAME]
    container(awss3Download.getContainerName()) {
        awss3Download.action(this, structure[GlobalVars.ENV])
    }
}