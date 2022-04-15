package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class Terraform implements Action, Serializable {

    private static String CONTAINER = "terraform"
    private static String IMAGE = "hashicorp/terraform:1.0.6"

    @Override
    void action(def script) {
        script.dir("/shared/terraform") {
            script.sh("""
                        export HTTP_PROXY=http://jmix:jmix@54.215.128.225:8888
                        export HTTPS_PROXY=http://jmix:jmix@54.215.128.225:8888
                        export NO_PROXY=10.5.44.21,10.5.44.21:32206
            """)
            script.sh("""
                        terraform init \
                            -backend-config="bucket=${script.env[GlobalVars.BUCKET_NAME]}" \
                            -reconfigure \
                            -input=false
            """)
            script.sh("""
                        unset NO_PROXY
                        unset HTTP_PROXY
                        unset HTTPS_PROXY
            """)
            script.sh("""
                        terraform plan \
                            -var="region=${script.env[GlobalVars.AWS_REGION]}" \
                            -input=false \
                            -out=terraform.tfplan
            """)
            script.sh("terraform apply -auto-approve -input=false terraform.tfplan")

            def structure = script.readJSON text: script.env[GlobalVars.CLOUD_CONTROL_CONTEXT], returnPojo: true

            structure[GlobalVars.PARAMETERS][GlobalVars.INSTANCE_IP.toLowerCase()] =
                    script.sh(
                            script: "terraform output -raw instanceIp",
                            returnStdout: true
                    ).trim()

            structure[GlobalVars.PARAMETERS][GlobalVars.INSTANCE_ID.toLowerCase()] =
                    script.sh(
                            script: "terraform output -raw instanceId",
                            returnStdout: true
                    ).trim()

            structure[GlobalVars.PARAMETERS][GlobalVars.SUBNET_ID.toLowerCase()] =
                    script.sh(
                            script: "terraform output -raw subnetId",
                            returnStdout: true
                    ).trim()

            structure[GlobalVars.PARAMETERS][GlobalVars.VPC_ID.toLowerCase()] =
                    script.sh(
                            script: "terraform output -raw vpcId",
                            returnStdout: true
                    ).trim()

            structure[GlobalVars.OPENSSH_KEY.toLowerCase()] =
                    script.sh(
                            script: "terraform output -raw opensshKey",
                            returnStdout: true
                    ).trim()

            script.env[GlobalVars.CLOUD_CONTROL_CONTEXT] = structure
        }
    }

    @Override
    void rollback(def script) {
        script.dir('/shared/terraform') {
            script.sh("""
                    terraform init \
                    -backend-config="bucket=${script.env[GlobalVars.BUCKET_NAME]}" \
                    -reconfigure \
                    -input=false
            """)
            script.sh("""terraform destroy -auto-approve -input=false -var="region=${script.env[GlobalVars.AWS_REGION]}" """)
        }
    }

    @Override
    String getContainerName() {
        return CONTAINER
    }

    @Override
    String getImage() {
        return IMAGE
    }

}
