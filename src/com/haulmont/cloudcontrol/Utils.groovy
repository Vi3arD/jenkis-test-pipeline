package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

class Utils {

    static def getActionInstanceByClassName(String name) {
        def cls = Class.forName("com.haulmont.cloudcontrol.actions.${name}", true, Thread.currentThread().getContextClassLoader())
        return cls.getDeclaredConstructor().newInstance()
    }

    static void toEnv(def script, def parameters) {
        parameters.each {
            entry -> script.env["${entry.key}"] = entry.value
        }
    }

    static ArrayList getContainers(def script, def actions) {
        def result = []
        def containers = []
        for (int i = 0; i < actions.size(); i++) {
            def executor = getActionInstanceByClassName(actions[i][GlobalVars.EXECUTOR] as String)
            if (!containers.contains(executor.getContainerName())) {
                result.add(script.containerTemplate(name: executor.getContainerName(), image: executor.getImage(), command: 'sleep', args: '99d'))
                containers.add(executor.getContainerName())
            }
        }
        return result
    }

    static void make(def script, def action, boolean isRollback = false) {
        def executor = getActionInstanceByClassName(action[GlobalVars.EXECUTOR] as String)
        script.container(executor.getContainerName()) {
            if (isRollback) {
                executor.rollback(script)
            } else {
                executor.action(script)
            }
        }
        Notifier.send(this, "status", action[GlobalVars.NAME] as String)
    }

}
