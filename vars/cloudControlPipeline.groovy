#!/usr/bin/env groovy
package vars

import groovy.json.JsonSlurper
import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.GlobalVars

def call(String request) {
    def jsonSlurper = new JsonSlurper()
    def structure = jsonSlurper.parseText(request)
    AWSS3Download awss3Download = new AWSS3Download();
    awss3Download.action(structure[GlobalVars.ENV])
}