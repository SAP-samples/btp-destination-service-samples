package com.sap.migration.neo.cf.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.migration.neo.cf.auth.dto.AuthResponse;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

public class AuthClientImpl implements AuthClient {

    @Override
    public Optional<String> getAccessToken(String authUrl, String clientId, String clientSecret) {
        Request request = Request.post(format("%s/oauth/token", authUrl));
        request.bodyForm(
                new BasicNameValuePair("grant_type", "client_credentials"),
                new BasicNameValuePair("client_id", clientId),
                new BasicNameValuePair("client_secret", clientSecret)
        );

        String token = null;

        try {
            String result = request.execute().returnContent().asString();
            AuthResponse authResponse = new ObjectMapper().readValue(result, AuthResponse.class);

            if (authResponse != null) {
                token = authResponse.getAccessToken();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(token);
    }
}
