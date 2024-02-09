package com.sap.migration.neo.destination.service;

import com.sap.migration.neo.destination.dto.DestinationMigrationMessage;
import com.sap.migration.neo.destination.dto.MigrationRequest;

import java.util.List;

public interface MigrationService {

    List<DestinationMigrationMessage> migrationAccountDestinations(MigrationRequest migrationRequest);
}
