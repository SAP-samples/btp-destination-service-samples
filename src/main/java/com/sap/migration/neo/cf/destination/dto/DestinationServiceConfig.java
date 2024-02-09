package com.sap.migration.neo.cf.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationServiceConfig {

    private String url;

    private String authUrl;

    private String clientId;

    private String clientSecret;
}
