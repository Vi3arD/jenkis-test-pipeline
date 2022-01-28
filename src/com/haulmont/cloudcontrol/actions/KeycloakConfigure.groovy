package com.haulmont.cloudcontrol.actions

import com.haulmont.cloudcontrol.GlobalVars
@Grab('org.keycloak:keycloak-admin-client:3.2.1.Final')
@Grab('org.jboss.resteasy:resteasy-client:3.0.14.Final')
@Grab('org.jboss.resteasy:resteasy-multipart-provider:3.0.14.Final')
@Grab('org.jboss.resteasy:resteasy-jackson2-provider:3.0.14.Final')
@Grab('org.jboss.resteasy:resteasy-jaxb-provider:3.0.14.Final')
@Grab('org.jboss.resteasy:resteasy-jettison-provider:3.0.14.Final')
@Grab('org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.0_spec:1.0.0.Final')


import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.ProtocolMapperRepresentation
import java.io.Serializable;

class KeycloakConfigure implements Action, Serializable {

    private static String CONTAINER = "alpine"
    private static String IMAGE = "alpine:3.15.0"

    @Override
    void action(def script) {
        Keycloak keycloak = Keycloak.getInstance(
                "${script.env[GlobalVars.KEYCLOAK_URL]}/auth",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_REALM]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_USERNAME]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_PASSWORD]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_CLIENT_ID]}")

        ProtocolMapperRepresentation protocolMapperRepresentation = new ProtocolMapperRepresentation()
        protocolMapperRepresentation.setProtocol('openid-connect')
        protocolMapperRepresentation.setName('roles')
        protocolMapperRepresentation.setProtocolMapper('oidc-usermodel-realm-role-mapper')
        protocolMapperRepresentation.setConfig([
                'claim.name'          : 'roles',
                'jsonType.label'      : 'String',
                'access.token.claim'  : 'true',
                'id.token.claim'      : 'true',
                'userinfo.token.claim': 'true'
        ])

        ClientRepresentation clientRepresentation = new ClientRepresentation()
        clientRepresentation.setClientId("gitlab-${script.env[GlobalVars.WORKSPACE_ID]}")
        clientRepresentation.setRedirectUris(["${script.env[GlobalVars.INSTANCE_URL]}*"])
        clientRepresentation.setSecret("${script.env[GlobalVars.KEYCLOAK_GITLAB_CLIENT_SECRET]}")
        clientRepresentation.setProtocolMappers([protocolMapperRepresentation])

        keycloak.realm("${script.env[GlobalVars.WORKSPACE_ID]}").clients().create(clientRepresentation)
    }

    @Override
    void rollback(def script) {
        Keycloak keycloak = getKeycloak(script)

        def clients = keycloak
                .realm("${script.env[GlobalVars.WORKSPACE_ID]}")
                .clients()
                .findByClientId("gitlab-${script.env[GlobalVars.WORKSPACE_ID]}")

        String id = clients.get(0).getId()
        if (id != null) {
            keycloak.realm(workspaceId).clients().get(id).remove();
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

    private Keycloak getKeycloak(def script) {
        return Keycloak.getInstance(
                "${script.env[GlobalVars.KEYCLOAK_URL]}/auth",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_REALM]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_USERNAME]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_PASSWORD]}",
                "${script.env[GlobalVars.KEYCLOAK_ADMIN_CLIENT_ID]}")
    }

}
