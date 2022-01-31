package com.haulmont.cloudcontrol.actions

@Grab('org.keycloak:keycloak-admin-client:15.0.1')
//@Grab('org.keycloak:keycloak-core:15.0.1')
//@Grab('org.keycloak:keycloak-common:15.0.1')
//@Grab('org.jboss.resteasy:resteasy-jaxb-provider:3.13.2.Final')
//
//@Grab('org.jboss.resteasy:resteasy-multipart-provider:3.13.2.Final')
//@Grab('com.sun.activation:jakarta.activation:1.2.2')
//@Grab('com.sun.mail:jakarta.mail:1.6.7')
//@Grab('org.apache.james:apache-mime4j:0.6')
//@Grab('org.jboss.logging:jboss-logging:3.4.2.Final')
//@Grab('org.jboss.resteasy:resteasy-client:3.13.2.Final')
//@Grab('org.jboss.resteasy:resteasy-jaxb-provider:3.13.2.Final')
//@Grab('org.glassfish.jaxb:jaxb-runtime:2.3.4')
//@Grab('org.jboss.spec.javax.xml.bind:jboss-jaxb-api_2.3_spec:2.0.0.Final')
//@Grab('org.jboss.resteasy:resteasy-jaxrs:3.13.2.Final')
//@Grab('com.fasterxml.jackson.core:jackson-annotations:2.12.3')
//@Grab('com.fasterxml.jackson.core:jackson-core:2.12.3')
//@Grab('com.fasterxml.jackson.core:jackson-databind:2.12.3')
//@Grab('com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.12.3')
//@Grab('com.github.fge:json-patch:1.9')
//@Grab('com.github.fge:jackson-coreutils:1.6')
//@Grab('com.github.fge:msg-simple:1.1')
//@Grab('com.github.fge:btf:1.2')
//@Grab('com.google.guava:guava:28.1-jre')
//@Grab('com.google.code.findbugs:jsr305:3.0.2')
//@Grab('com.google.errorprone:error_prone_annotations:2.3.3')
//@Grab('com.google.guava:failureaccess:1.0.1')
//@Grab('com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava')
//@Grab('com.google.j2objc:j2objc-annotations:1.3')
//@Grab('org.checkerframework:checker-qual:2.8.1')
//@Grab('org.codehaus.mojo:animal-sniffer-annotations:1.18')
//@Grab('commons-codec:commons-codec:1.15')
//@Grab('commons-io:commons-io:2.5')
//@Grab('org.apache.httpcomponents:httpclient:4.5.13')
//@Grab('org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:2.0.1.Final')


import com.haulmont.cloudcontrol.GlobalVars
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
