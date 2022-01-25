package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.ClassKeeper
import com.haulmont.cloudcontrol.GlobalVars
import com.haulmont.cloudcontrol.Notifier

class Utils {

    static void toEnv(def script, def parameters) {
        parameters.each {
            entry -> script.env["${entry.key}"] = entry.value
        }
    }

    static ArrayList getContainers(def actions) {
        def result = []
        Map beans = ClassKeeper.getBeans()
        for (int i = 0; i < actions.size(); i++) {
            def executor = beans.get(actions[i][GlobalVars.EXECUTOR])
            result.add(containerTemplate(name: executor.getContainerName(), image: executor.getImage(), command: 'sleep', args: '99d'))
        }
    }

    static void make(def script, def action, boolean isRollback = false) {
        Map beans = ClassKeeper.getBeans()
        def executor = beans.get(action[GlobalVars.EXECUTOR])
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
