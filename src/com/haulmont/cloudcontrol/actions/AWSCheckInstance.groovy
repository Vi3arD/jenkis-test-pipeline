package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class AWSCheckInstance implements Action, Serializable {

    private static String CONTAINER = "aws-cli"
    private static String IMAGE = "amazon/aws-cli:2.4.12"

    @Override
    void action(def script) {
        script.waitUntil(initialRecurrencePeriod: 15000) {
            String status = script.sh(
                    script: """
                                aws ec2 describe-instance-status \
                                    --region "${script.env[GlobalVars.AWS_REGION]}" \
                                    --instance-ids "${script.env[GlobalVars.INSTANCE_ID]}" \
                                    --output text \
                                    --query InstanceStatuses[0].SystemStatus.Details[*].Status
                            """,
                    returnStdout: true
            ).trim()
            if (status != "passed") {
                throw new RuntimeException("Instance isn't ready to next installation")
            }
        }
    }

    @Override
    void rollback(def script) {
        script.sh "echo AWSCheckInstance rollback not implemented"
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
