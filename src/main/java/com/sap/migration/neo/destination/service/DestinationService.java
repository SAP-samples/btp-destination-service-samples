package com.sap.migration.neo.destination.service;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.destination.dto.NeoCertificate;

import java.security.cert.Certificate;
import java.util.Map;
import java.util.Optional;

public interface DestinationService {
    Map<String, DestinationConfiguration> getAccountDestinationConfiguration(String accountName);

    Optional<DestinationConfiguration> getDestinationConfiguration(String destinationName);

    Optional<NeoCertificate> getCertificate(String accountName, String certificateName);
}
