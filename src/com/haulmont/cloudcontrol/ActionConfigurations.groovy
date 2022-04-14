package com.haulmont.cloudcontrol

class ActionConfigurations {

    static def configurations = [
            "AWSTerraform": [
                    "AWSS3Download",
                    "Terraform"
            ],
            "AWSAnsible"  : [
                    "AWSS3Download",
                    "Ansible"
            ]
    ]

    static ArrayList getConfiguration(String configuration) {
        return configurations[configuration]
    }

}
