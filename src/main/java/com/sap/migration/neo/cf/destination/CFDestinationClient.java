package com.sap.migration.neo.cf.destination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.migration.neo.destination.dto.NeoCertificate;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

public interface CFDestinationClient {

    void createDestination(String destinationName, String destinationConfiguration);

    void uploadCertificate(NeoCertificate certificate) throws CertificateEncodingException, JsonProcessingException;
}
