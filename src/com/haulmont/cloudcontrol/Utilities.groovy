package com.haulmont.cloudcontrol

class Utilities {
    static def mvn(script, args) {
        script.sh "echo something"
        script.sh "echo ${script}"
        script.sh "aws --version"
        script.sh "${script.tool 'aws'} -version"
    }
}
