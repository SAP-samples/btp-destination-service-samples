package com.sap.migration.neo.destination.enums;

import lombok.Getter;

@Getter
public enum DestinationAuthentication {
    BASIC_AUTHENTICATION("BasicAuthentication"),
    OAUTH2_CLIENT_CREDENTIALS("OAuth2ClientCredentials"),
    OAUTH2_SAML_BEARER_ASSERTION("OAuth2SAMLBearerAssertion"),
    NO_AUTHENTICATION("NoAuthentication"),
    APP_TO_APP_SSO("AppToAppSSO"),
    CLIENT_CERTIFICATE_AUTHENTICATION("ClientCertificateAuthentication");

    private final String type;

    DestinationAuthentication(String type) {
        this.type = type;
    }
}
