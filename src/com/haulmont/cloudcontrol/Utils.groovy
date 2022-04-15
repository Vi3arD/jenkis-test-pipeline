package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

class Utils {

    static def getActionInstanceByClassName(String name) {
        def cls = Class.forName("com.haulmont.cloudcontrol.actions.${name}", true, Thread.currentThread().getContextClassLoader())
        return cls.getDeclaredConstructor().newInstance()
    }

    static boolean booleanFromString(String value) {
        return GlobalVars.TRUE == value
    }

    static void toEnv(def script, def parameters) {
        parameters.each {
            entry ->
                if (GlobalVars.PARAMETERS == entry.key) {
                    toEnv(script, entry.value)
                }
                script.env["${entry.key.toUpperCase()}"] = entry.value
        }
    }

    static ArrayList getContainers(def script, ArrayList actions) {
        def result = []
        def containers = []
        for (int i = 0; i < actions.size(); i++) {
            def executor = getActionInstanceByClassName(actions[i] as String)
            if (!containers.contains(executor.getContainerName())) {
                result.add(script.containerTemplate(name: executor.getContainerName(), image: executor.getImage(), command: 'sleep', args: '99d'))
                containers.add(executor.getContainerName())
            }
        }
        return result
    }

    static void make(def script, String action, boolean isRollback = false) {
        def executor = getActionInstanceByClassName(action)
        script.container(executor.getContainerName()) {
            if (isRollback) {
                executor.rollback(script)
            } else {
                executor.action(script)
            }
        }
//        Notifier.send(this, "status", action[GlobalVars.NAME] as String)
    }

}
