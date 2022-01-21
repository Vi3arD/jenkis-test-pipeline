package com.haulmont.cloudcontrol

class ParserUtils {

    public static void toEnv(def script, Map parameters) {
        parameters.each {
            script.env["${entry.key}"] = entry.value
        }
    }

}
