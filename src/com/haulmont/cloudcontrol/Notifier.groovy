package com.haulmont.cloudcontrol

import com.haulmont.cloudcontrol.GlobalVars

class Notifier {

    static void send(def script, String type, String description = "") {
        def response = httpRequest consoleLogResponseBody: true,
                httpMode: 'GET',
                acceptType: 'APPLICATION_JSON',
                contentType: 'APPLICATION_JSON',
                customHeaders: [[name: 'X-Jenkins-Token', value: "${script.env[GlobalVars.CLOUD_CONTROL_M2M_TOKEN]}"]],
                requestBody: script.env[GlobalVars.CLOUD_CONTROL_CONTEXT],
                url: "${script.env[GlobalVars.CALLBACK_URL]}/rest/jenkins/${type}",
                validResponseCodes: '200'
    }

}
