package com.haulmont.cloudcontrol

class Utilities {
    static def mvn(script, args) {
        script.sh "echo something"
        script.sh "echo ${script.tool}"
        script.sh "terraform -version"
    }
}
