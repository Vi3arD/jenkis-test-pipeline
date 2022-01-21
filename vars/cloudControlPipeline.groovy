#!/usr/bin/env groovy
package vars

import groovy.json.JsonSlurperClassic
import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.Terraform
import com.haulmont.cloudcontrol.GlobalVars
@Grab( 'org.reflections:reflections:0.9.9-RC1' )
import org.reflections.*

def call(String request) {
    def structure = readJSON text: request, returnPojo: true

    Reflections reflections = new Reflections("com.haulmont.cloudcontrol");
    Set<Class<? extends Action>> classes = reflections.getSubTypesOf(Action.class);
    echo classes

//    AWSS3Download awss3Download = new AWSS3Download()
//    Terraform terraform = new Terraform()
//    container(awss3Download.getContainerName()) {
//        awss3Download.action(this, structure[GlobalVars.ENV])
//    }
//    container(terraform.getContainerName()) {
//        terraform.action(this, structure[GlobalVars.ENV])
//    }
}