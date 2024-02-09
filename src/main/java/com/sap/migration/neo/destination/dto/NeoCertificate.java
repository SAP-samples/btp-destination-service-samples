package com.sap.migration.neo.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.cert.Certificate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeoCertificate {

    private String name;

    private Certificate certificate;
}
