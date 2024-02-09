package com.sap.migration.neo.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MigrationRequest {

    @NotBlank
    private String accountName;

    @NotBlank
    private String destinationUrl;

    @NotBlank
    private String destinationAuthUrl;

    @NotBlank
    private String destinationClientId;

    @NotBlank
    private String destinationClientSecret;
}
