package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars

class Ansible implements Action, Serializable {

    private static String CONTAINER = 'ansible'
    private static String IMAGE = 'ansible/ansible-runner:1.4.7'

    @Override
    void action(def script) {
        script.dir('/shared/ansible') {
            script.sh("""echo "[gitlab_aws]\ngitlab ansible_host=${script.env[GlobalVars.INSTANCE_IP]} ansible_user=ubuntu" > inventory""")
            script.sh("echo ${script.env[GlobalVars.OPENSSH_KEY]} > k1_temp.pem")
            script.sh('sed -e "s/-----BEGIN RSA PRIVATE KEY----- //g" -i k1_temp.pem')
            script.sh('sed -e "s/ -----END RSA PRIVATE KEY-----//g" -i k1_temp.pem')
            script.sh('cat k1_temp.pem | tr " " "\\n" > k2_temp.pem')
            script.sh('echo "-----BEGIN RSA PRIVATE KEY-----" > key.pem')
            script.sh('cat k2_temp.pem >> key.pem ')
            script.sh('echo "-----END RSA PRIVATE KEY-----" >> key.pem')
            script.sh('chmod 400 key.pem')
            script.sh("""
                    ansible-playbook -i inventory infra.yml -e \
                    "workspace=${script.env[GlobalVars.WORKSPACE_ID]}\n 
                    keycloak_url=${script.env[GlobalVars.KEYCLOAK_URL]}\n 
                    keycloak_realm=${script.env[GlobalVars.WORKSPACE_ID]}\n 
                    keycloak_client=gitlab-${script.env[GlobalVars.WORKSPACE_ID]}\n 
                    keycloak_client_secret=${script.env[GlobalVars.CLIENT_SECRET]}\n 
                    admin_password=${script.env[GlobalVars.ADMIN_PASSWORD]}\n 
                    registration_token=${script.env[GlobalVars.REGISTRATION_TOKEN]}\n 
                    gitlab_url=${script.env[GlobalVars.INSTANCE_URL]}\n 
                    registry_url=${script.env[GlobalVars.REGISTRY_URL]}\n 
                    ip_address=${script.env[GlobalVars.INSTANCE_IP]}\n 
                    cloud_control_user_password=password" 
            """)
        }
    }

    @Override
    void rollback(def script) {
        script.sh "echo Ansible rollback not implemented"
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
