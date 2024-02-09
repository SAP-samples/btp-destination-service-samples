package com.sap.migration.neo.destination.service;

import java.util.Map;
import java.util.Set;

public interface DestinationJsonService {
    Map<String, Object> getAccountDestinationsJsonConfig(String accountName,
                                                         Set<String> subaccountDestinationNames,
                                                         Set<String> instanceDestinationNames);
}
