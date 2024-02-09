package com.sap.migration.neo.cf.destination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.cf.destination.dto.DestinationServiceConfig;
import com.sap.migration.neo.destination.dto.NeoCertificate;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

public interface CFDestinationService {

    void createDestination(DestinationServiceConfig config, DestinationConfiguration destConfig);

    void uploadCertificate(DestinationServiceConfig config, NeoCertificate certificate) throws CertificateEncodingException, JsonProcessingException;
}
