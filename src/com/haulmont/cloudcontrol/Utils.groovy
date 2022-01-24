package com.haulmont.cloudcontrol

class Utils {

    static void toEnv(def script, def parameters) {
        parameters.each {
            entry -> script.env["${entry.key}"] = entry.value
        }
    }

}
