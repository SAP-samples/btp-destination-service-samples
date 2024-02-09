package com.sap.migration.neo.cf.destination;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

import java.util.HashMap;
import java.util.Map;

import static com.sap.migration.neo.destination.enums.DestinationAuthentication.APP_TO_APP_SSO;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.BASIC_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.CLIENT_CERTIFICATE_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.NO_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.OAUTH2_CLIENT_CREDENTIALS;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.OAUTH2_SAML_BEARER_ASSERTION;
import static com.sap.migration.neo.destination.enums.DestinationProperty.AUTHENTICATION;

public class DestinationConfigurationMapper {

    private static final String CF_BASIC_AUTHENTICATION_TYPE = "BasicAuthentication";
    private static final String CF_OAUTH2_CLIENT_CREDENTIALS_TYPE = "OAuth2ClientCredentials";
    private static final String CF_OAUTH2_SAML_BEARER_ASSERTION = "OAuth2SAMLBearerAssertion";
    private static final String CF_NO_AUTHENTICATION = "NoAuthentication";
    private static final String CF_CLIENT_CERTIFICATE_AUTHENTICATION = "ClientCertificateAuthentication";


    public static Map<String, String> map(DestinationConfiguration destConfig) {
        String authType = destConfig.getProperty(AUTHENTICATION.getName());

        Map<String, String> newDestinationProperties = new HashMap<>();
        newDestinationProperties.put("Name", destConfig.getProperty("Name"));
        newDestinationProperties.put("URL", destConfig.getProperty("URL"));
        newDestinationProperties.put("Type", destConfig.getProperty("Type"));
        newDestinationProperties.put("ProxyType", destConfig.getProperty("ProxyType"));

        // currently, destinations migration support only basic destination configuration properties
        // additional properties will be added in the future if required
        if (authType.equals(BASIC_AUTHENTICATION.getType())) {
            newDestinationProperties.put("Authentication", CF_BASIC_AUTHENTICATION_TYPE);
            newDestinationProperties.put("User", destConfig.getProperty("User"));
            newDestinationProperties.put("Password", destConfig.getProperty("Password"));
        } else if (authType.equals(OAUTH2_CLIENT_CREDENTIALS.getType())) {
            newDestinationProperties.put("Authentication", CF_OAUTH2_CLIENT_CREDENTIALS_TYPE);
            newDestinationProperties.put("clientId", destConfig.getProperty("clientId"));
            newDestinationProperties.put("clientSecret", destConfig.getProperty("clientSecret"));
            newDestinationProperties.put("tokenServiceURL", destConfig.getProperty("tokenServiceURL"));
        } else if (authType.equals(OAUTH2_SAML_BEARER_ASSERTION.getType())) {
            newDestinationProperties.put("Authentication", CF_OAUTH2_SAML_BEARER_ASSERTION);
            newDestinationProperties.put("audience", destConfig.getProperty("audience"));
            newDestinationProperties.put("clientKey", destConfig.getProperty("clientKey"));
            newDestinationProperties.put("tokenServicePassword", destConfig.getProperty("tokenServicePassword"));
            newDestinationProperties.put("tokenServiceURL", destConfig.getProperty("tokenServiceURL"));
            newDestinationProperties.put("tokenServiceUser", destConfig.getProperty("tokenServiceUser"));
            newDestinationProperties.put("tokenServiceURLType", "Dedicated");
        } else if (authType.equals(NO_AUTHENTICATION.getType())) {
            newDestinationProperties.put("Authentication", CF_NO_AUTHENTICATION);
        } else if (authType.equals(APP_TO_APP_SSO.getType())) {
            newDestinationProperties.put("Authentication", CF_NO_AUTHENTICATION);
            newDestinationProperties.put("HTML5.ForwardAuthToken", "true");
        } else if (authType.equals(CLIENT_CERTIFICATE_AUTHENTICATION.getType())) {
            newDestinationProperties.put("Authentication", CF_CLIENT_CERTIFICATE_AUTHENTICATION);
            newDestinationProperties.put("KeyStoreLocation", destConfig.getProperty("KeyStoreLocation"));
            newDestinationProperties.put("KeyStorePassword", destConfig.getProperty("KeyStorePassword"));
        }

        return newDestinationProperties;
    }
}
