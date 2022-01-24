package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.actions.AWSS3Download
import com.haulmont.cloudcontrol.actions.Terraform
import com.haulmont.cloudcontrol.actions.Ansible

class ClassKeeper {

    private static Map beans = [:]

    static Map getBeans() {
        Class.forName("GlobalVars")
        init()
        return beans
    }

    private static void init() {
        if (beans.size() == 0) {
            beans.put("AWSS3Download", new AWSS3Download())
            beans.put("Terraform", new Terraform())
            beans.put("Ansible", new Ansible())
        }
    }

}
