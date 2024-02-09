package com.sap.migration.neo.destination.servlet;

import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.migration.neo.destination.service.DestinationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseError;
import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseResult;

@WebServlet(name = "DestinationServlet", urlPatterns = "/destinations")
public class DestinationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestAccountName = request.getParameter("accountName");

        if (requestAccountName != null && !requestAccountName.isEmpty()) {
            try {
                Map<String, DestinationConfiguration> destinationConfigurationMap =
                        new DestinationServiceImpl().getAccountDestinationConfiguration(requestAccountName);

                jsonResponseResult(response, destinationConfigurationMap);
            } catch (Exception e) {
                jsonResponseError(response, e.getMessage());
            }
        } else {
            jsonResponseError(response, "The accountName GET request parameter is required.");
        }
    }
}
