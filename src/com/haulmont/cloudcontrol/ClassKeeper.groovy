package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.Terraform


class ClassKeeper {

    private static Map beans = [:]

    static Map getBeans() {
        init()
        return beans
    }

    private static void init() {
        if (beans.size() == 0) {
            beans.put("AWSS3Download", new AWSS3Download())
            beans.put("Terraform", new Terraform())
        }
    }

}
