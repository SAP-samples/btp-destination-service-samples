package com.sap.migration.neo.destination.servlet;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.destination.service.DestinationJsonServiceImpl;
import com.sap.migration.neo.destination.service.DestinationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseError;
import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseResult;

@WebServlet(name = "DestinationJsonServlet", urlPatterns = "/destinations/json")
public class DestinationJsonServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String subaccountDestinationsHeader = request.getHeader("X-Subaccount-Destinations");
        String instanceDestinationsHeader = request.getHeader("X-Instance-Destinations");
        String accountName = request.getHeader("X-Account-Name");

        if (accountName != null && !accountName.isEmpty()) {
            try {
                Set<String> subaccountDestinationNames =
                        Arrays.stream(subaccountDestinationsHeader.split(",")).collect(Collectors.toSet());
                Set<String> instanceDestinationNames =
                        Arrays.stream(instanceDestinationsHeader.split(",")).collect(Collectors.toSet());

                Map<String, Object> destinationConfigJson =
                        new DestinationJsonServiceImpl().getAccountDestinationsJsonConfig(
                                accountName, subaccountDestinationNames, instanceDestinationNames
                        );

                jsonResponseResult(response, destinationConfigJson);
            } catch (Exception e) {
                jsonResponseError(response, e.getMessage());
            }
        } else {
            jsonResponseError(response, "The accountName GET request parameter is required.");
        }
    }
}
