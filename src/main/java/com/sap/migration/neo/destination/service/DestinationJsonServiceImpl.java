package com.sap.migration.neo.destination.service;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DestinationJsonServiceImpl implements DestinationJsonService {
    @Override
    public Map<String, Object> getAccountDestinationsJsonConfig(String accountName,
                                                                Set<String> subaccountDestinationNames,
                                                                Set<String> instanceDestinationNames) {
        Map<String, DestinationConfiguration> destinationConfigurationMap =
                new DestinationServiceImpl().getAccountDestinationConfiguration(accountName);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> initData = new HashMap<>();

        result.put("HTML5Runtime_enabled", true);
        result.put("init_data", initData);

        List<Object> subaccountDestinations = new ArrayList<>();
        List<Object> instanceDestinations = new ArrayList<>();

        destinationConfigurationMap.forEach((key, value) -> {
            if (subaccountDestinationNames.contains(key)) {
                subaccountDestinations.add(value.getAllProperties());
            }
            if (instanceDestinationNames.contains(key)) {
                instanceDestinations.add(value.getAllProperties());
            }
        });

        if (subaccountDestinations.size() > 0) {
            putDestinationProperties(initData, "subaccount", subaccountDestinations);
        }
        if (instanceDestinations.size() > 0) {
            putDestinationProperties(initData, "instance", instanceDestinations);
        }

        return result;
    }

    private void putDestinationProperties(Map<String, Object> initData, String level, List<Object> destinations) {
        Map<String, Object> initDataLevelStructure = new HashMap<>();
        initDataLevelStructure.put("existing_destinations_policy", "ignore");
        initDataLevelStructure.put("existing_certificates_policy", "ignore");
        initDataLevelStructure.put("destinations", destinations);
        initData.put(level, initDataLevelStructure);
    }
}
