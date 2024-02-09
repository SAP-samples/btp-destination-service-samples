package com.sap.migration.neo.cf.destination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.cf.auth.AuthClient;
import com.sap.migration.neo.cf.auth.AuthClientImpl;
import com.sap.migration.neo.cf.destination.dto.DestinationServiceConfig;
import com.sap.migration.neo.destination.dto.NeoCertificate;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Map;

public class CFDestinationServiceImpl implements CFDestinationService {

    private final AuthClient authClient;

    public CFDestinationServiceImpl() {
        authClient = new AuthClientImpl();
    }

    @Override
    public void createDestination(DestinationServiceConfig config, DestinationConfiguration destConfig) {
        CFDestinationClient destinationClient = prepareDestinationClient(config);
        Map<String, String> newDestinationConfig = DestinationConfigurationMapper.map(destConfig);

        try {
            String newDestinationConfigString = new ObjectMapper().writeValueAsString(newDestinationConfig);
            destinationClient.createDestination(destConfig.getProperty("Name"), newDestinationConfigString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadCertificate(DestinationServiceConfig config, NeoCertificate certificate)
            throws CertificateEncodingException, JsonProcessingException {
        CFDestinationClient destinationClient = prepareDestinationClient(config);
        destinationClient.uploadCertificate(certificate);
    }

    private CFDestinationClient prepareDestinationClient(DestinationServiceConfig config) {
        String accessToken = this.authClient.getAccessToken(
                config.getAuthUrl(),
                config.getClientId(),
                config.getClientSecret()
        ).orElseThrow(() -> new RuntimeException("Failed to get access token"));

        return new CFDestinationClientImpl(accessToken, config.getUrl());
    }
}
