package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars

class Notifier {

    static void send(def script, String type, String description = "") {

        def structure = script.readJSON text: script.env[GlobalVars.CLOUD_CONTROL_CONTEXT], returnPojo: true
        structure[GlobalVars.PARAMETERS][GlobalVars.INSTANCE_ID.toLowerCase()] = "77.7.7.7.7.7.7.7.4"
        echo "structure -> ${structure}"

        def response = script.httpRequest consoleLogResponseBody: true,
                httpMode: 'GET',
                validResponseCodes: '200',
                acceptType: 'APPLICATION_JSON',
                contentType: 'APPLICATION_JSON',
                customHeaders: [[name: 'X-Jenkins-Token', value: "${script.env[GlobalVars.CLOUD_CONTROL_M2M_TOKEN]}"]],
                requestBody: script.env[GlobalVars.CLOUD_CONTROL_CONTEXT],
                url: "${script.env[GlobalVars.CALLBACK_URL]}/rest/jenkins/${type}"
    }

}
