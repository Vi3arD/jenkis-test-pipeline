package com.haulmont.cloudcontrol

class ParserUtils {

    static void toEnv(def script, def parameters) {
        parameters[0].each {
            entry -> script.env["${entry.key}"] = entry.value
        }
    }

}
