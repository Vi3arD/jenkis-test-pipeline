package com.haulmont.cloudcontrol

class ParserUtils {

    public static void toEnv(def script, def parameters) {
        script.sh "echo ${parameters}"
        parameters[0].each {
            entry -> script.env["${entry.key}"] = entry.value
        }
    }

}
