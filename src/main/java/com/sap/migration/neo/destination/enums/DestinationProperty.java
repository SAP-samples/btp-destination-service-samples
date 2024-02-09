package com.sap.migration.neo.destination.enums;

import lombok.Getter;

@Getter
public enum DestinationProperty {

    AUTHENTICATION("Authentication"),
    URL("URL"),
    NAME("Name");

    private final String name;

    DestinationProperty(String name) {
        this.name = name;
    }
}
