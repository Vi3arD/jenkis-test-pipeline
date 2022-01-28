package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class Terraform implements Action, Serializable {

    private static String CONTAINER = "terraform"
    private static String IMAGE = "hashicorp/terraform:1.0.6"

    @Override
    void action(def script) {
        script.dir("/shared/terraform") {
            script.sh("""
                        terraform init \
                            -backend-config="bucket=${script.env[GlobalVars.BUCKET_NAME]}" \
                            -reconfigure \
                            -input=false
            """)
            script.sh("""
                        terraform plan \
                            -var="region=${script.env[GlobalVars.AWS_REGION]}" \
                            -input=false \
                            -out=terraform.tfplan
            """)
            script.sh("terraform apply -auto-approve -input=false terraform.tfplan")

            script.env[GlobalVars.INSTANCE_IP] = script.sh(
                    script: "terraform output -raw instanceIp",
                    returnStdout: true
            ).trim()

            script.env[GlobalVars.INSTANCE_ID] = script.sh(
                    script: "terraform output -raw instanceId",
                    returnStdout: true
            ).trim()

            script.sh 'terraform output -raw openssh_key > ../ansible/key.pem'
        }
    }

    @Override
    void rollback(def script) {
        script.sh "echo TERRAFORM ROLLBACK"
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
