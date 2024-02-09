package com.sap.migration.neo.destination.service;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.destination.dto.NeoCertificate;
import lombok.extern.slf4j.Slf4j;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public class DestinationServiceImpl implements DestinationService {

    @Override
    public Map<String, DestinationConfiguration> getAccountDestinationConfiguration(String accountName) {
        return getConnectivityConfiguration()
                .map(config -> config.getConfigurations(accountName))
                .orElse(new HashMap<>());
    }

    @Override
    public Optional<DestinationConfiguration> getDestinationConfiguration(String destinationName) {
        return getConnectivityConfiguration()
                .map(config -> config.getConfiguration(destinationName));
    }

    @Override
    public Optional<NeoCertificate> getCertificate(String destinationName, String certificateName) {
        if (certificateName == null || certificateName.isEmpty()) {
            log.info("A certificate could not be retrieved due to invalid certificate name (empty or null)");
            return Optional.empty();
        }

        Optional<DestinationConfiguration> destConfigOptional = getDestinationConfiguration(destinationName);

        if (destConfigOptional.isPresent()) {
            DestinationConfiguration destConfig = destConfigOptional.get();
            try {
                Certificate certificate = destConfig.getTrustStore().getCertificate(certificateName);
                if (certificate != null) {
                    log.info(format("The %s certificate type is %s", certificateName, certificate.getType()));
                    return Optional.of(new NeoCertificate(certificateName, certificate));
                }
                throw new RuntimeException(format("Certificate with name %s not found", certificateName));
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    /**
     * Get the connectivity configuration from the environment.
     *
     * @return the connectivity configuration
     */
    private Optional<ConnectivityConfiguration> getConnectivityConfiguration() {
        try {
            Context ctx = new InitialContext();
            ConnectivityConfiguration configuration =
                    (ConnectivityConfiguration) ctx.lookup("java:comp/env/connectivityConfiguration");
            return Optional.ofNullable(configuration);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
