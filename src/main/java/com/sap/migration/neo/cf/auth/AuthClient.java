package com.sap.migration.neo.cf.auth;

import java.util.Optional;

public interface AuthClient {

    Optional<String> getAccessToken(String authUrl, String clientId, String clientSecret);
}
