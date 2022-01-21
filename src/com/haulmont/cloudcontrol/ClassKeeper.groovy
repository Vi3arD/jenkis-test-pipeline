package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.AWSS3Download
import com.haulmont.cloudcontrol.Terraform


class ClassKeeper {

    private def beans = [:]

    public def getBeans() {
        return beans
    }

    private void init() {
        beans.put("AWSS3Download", new AWSS3Download())
        beans.put("Terraform", new Terraform())
    }
}
