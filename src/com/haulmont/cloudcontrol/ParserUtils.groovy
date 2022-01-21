package com.haulmont.cloudcontrol

class ParserUtils {

    public static void toEnv(def script, Map parameters) {
        parameters.each {
            entry ->
                script.sh "echo ${entry.key}"
                script.env["${entry.key}"] = entry.value
                script.env.$entry.key = entry.value
        }
    }

}
