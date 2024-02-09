package com.sap.migration.neo.destination.service;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.cf.destination.CFDestinationService;
import com.sap.migration.neo.cf.destination.CFDestinationServiceImpl;
import com.sap.migration.neo.cf.destination.dto.DestinationServiceConfig;
import com.sap.migration.neo.destination.dto.DestinationMigrationMessage;
import com.sap.migration.neo.destination.dto.MigrationRequest;
import com.sap.migration.neo.destination.dto.NeoCertificate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.sap.migration.neo.destination.enums.DestinationAuthentication.APP_TO_APP_SSO;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.BASIC_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.CLIENT_CERTIFICATE_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.NO_AUTHENTICATION;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.OAUTH2_CLIENT_CREDENTIALS;
import static com.sap.migration.neo.destination.enums.DestinationAuthentication.OAUTH2_SAML_BEARER_ASSERTION;
import static com.sap.migration.neo.destination.enums.DestinationProperty.AUTHENTICATION;

public class MigrationServiceImpl implements MigrationService {

    private static final Set<String> ALLOWED_DESTINATION_AUTHENTICATION_TYPES = new HashSet<>();

    private final DestinationService destinationService;

    private final CFDestinationService cfDestinationService;

    public MigrationServiceImpl() {
        // such Set has been added at the initial phase of the destinations migration
        // as only a few destination authentication types were supported to be migrated
        // just left it here for the backward reference
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(BASIC_AUTHENTICATION.getType());
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(OAUTH2_CLIENT_CREDENTIALS.getType());
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(OAUTH2_SAML_BEARER_ASSERTION.getType());
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(NO_AUTHENTICATION.getType());
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(APP_TO_APP_SSO.getType());
        ALLOWED_DESTINATION_AUTHENTICATION_TYPES.add(CLIENT_CERTIFICATE_AUTHENTICATION.getType());

        destinationService = new DestinationServiceImpl();
        cfDestinationService = new CFDestinationServiceImpl();
    }

    @Override
    public List<DestinationMigrationMessage> migrationAccountDestinations(MigrationRequest migrationRequest) {
        DestinationServiceConfig destinationServiceConfig = new DestinationServiceConfig(
                migrationRequest.getDestinationUrl(),
                migrationRequest.getDestinationAuthUrl(),
                migrationRequest.getDestinationClientId(),
                migrationRequest.getDestinationClientSecret()
        );

        List<DestinationMigrationMessage> messages = new LinkedList<>();

        destinationService
                .getAccountDestinationConfiguration(migrationRequest.getAccountName())
                .forEach((destinationName, destinationConfiguration) -> {
                            String migrationResult;

                            if (isDestinationAllowedForMigration(destinationConfiguration)) {
                                try {
                                    Optional<NeoCertificate> certificate =
                                            getCertificate(destinationName, destinationConfiguration);

                                    if (certificate.isPresent()) {
                                        cfDestinationService.uploadCertificate(
                                                destinationServiceConfig, certificate.get()
                                        );
                                    }

                                    cfDestinationService.createDestination(
                                            destinationServiceConfig,
                                            destinationConfiguration
                                    );
                                    migrationResult = "Success";
                                } catch (Exception e) {
                                    migrationResult = e.getMessage();
                                }
                            } else {
                                migrationResult = "Destination authentication type is not supported";
                            }

                            messages.add(
                                    DestinationMigrationMessage.builder()
                                            .destinationName(destinationName)
                                            .result(migrationResult)
                                            .build()
                            );
                        }
                );

        return messages;
    }

    private Optional<NeoCertificate> getCertificate(String destinationName, DestinationConfiguration destConfig) {
        String keyStoreLocation = destConfig.getProperty("KeyStoreLocation");
        if (keyStoreLocation == null || keyStoreLocation.isEmpty()) {
            return Optional.empty();
        }
        return destinationService.getCertificate(destinationName, keyStoreLocation);
    }

    private boolean isDestinationAllowedForMigration(DestinationConfiguration destinationConfiguration) {
        return ALLOWED_DESTINATION_AUTHENTICATION_TYPES.contains(
                destinationConfiguration.getProperty(AUTHENTICATION.getName())
        );
    }
}
