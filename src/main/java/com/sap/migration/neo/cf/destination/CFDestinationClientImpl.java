package com.sap.migration.neo.cf.destination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.migration.neo.destination.dto.NeoCertificate;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.hc.core5.http.ContentType.APPLICATION_JSON;

public class CFDestinationClientImpl implements CFDestinationClient {

    private final String accessToken;

    private final String destinationServiceUrl;

    public CFDestinationClientImpl(String accessToken, String destinationServiceUrl) {
        this.accessToken = accessToken;
        this.destinationServiceUrl = destinationServiceUrl;
    }

    @Override
    public void createDestination(String destinationName, String destinationConfiguration) {
        Request request = Request.post(
                format("%s/destination-configuration/v1/subaccountDestinations", destinationServiceUrl)
        );
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        request.bodyString(destinationConfiguration, APPLICATION_JSON);

        executeRequestAndHandle(request);
    }

    @Override
    public void uploadCertificate(NeoCertificate certificate)
            throws CertificateEncodingException, JsonProcessingException {
        Request request = Request.post(
                format("%s/destination-configuration/v1/subaccountCertificates", destinationServiceUrl)
        );
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        String base64CertificateContent = Base64.getEncoder()
                .encodeToString(certificate.getCertificate().getEncoded());
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Name", certificate.getName());
        bodyMap.put("Type", certificate.getCertificate().getType());
        bodyMap.put("Content", base64CertificateContent);
        String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);

        request.bodyString(jsonBody, APPLICATION_JSON);
        executeRequestAndHandle(request);
    }

    private HttpResponse executeRequestAndHandle(Request request) {
        try {
            HttpResponse response = request.execute().returnResponse();
            switch (response.getCode()) {
                case 400:
                    throw new RuntimeException("Bad request, something is missing");
                case 401:
                    throw new RuntimeException("Unauthorized");
                case 403:
                    throw new RuntimeException("Forbidden");
                case 404:
                    throw new RuntimeException("Destination service not found");
                case 409:
                    throw new RuntimeException("Conflict");
                case 500:
                    throw new RuntimeException("Internal server error");
                case 503:
                    throw new RuntimeException("Service unavailable");
            }

            return response;
        } catch (IOException e) {
            throw new RuntimeException(format("Error while creating destination %s", e.getMessage()));
        }
    }
}
