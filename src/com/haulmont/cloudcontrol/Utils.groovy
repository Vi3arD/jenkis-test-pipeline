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
