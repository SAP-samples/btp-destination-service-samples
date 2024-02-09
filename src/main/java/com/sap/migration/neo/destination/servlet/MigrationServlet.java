package com.sap.migration.neo.destination.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.migration.neo.destination.dto.DestinationMigrationMessage;
import com.sap.migration.neo.destination.dto.MigrationRequest;
import com.sap.migration.neo.destination.service.MigrationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseError;
import static com.sap.migration.neo.utils.ServletResponseUtils.jsonResponseResult;

@WebServlet(name = "DestinationMigrationServlet", urlPatterns = "/destinations/migrate")
public class MigrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            MigrationRequest migrationRequest =
                    new ObjectMapper().readValue(request.getReader(), MigrationRequest.class);

            List<DestinationMigrationMessage> migrationMessages =
                    new MigrationServiceImpl().migrationAccountDestinations(migrationRequest);

            Map<String, Object> result = new HashMap<>();
            result.put("result", "Migration completed");
            result.put("details", migrationMessages);

            jsonResponseResult(response, result);
        } catch (Exception e) {
            jsonResponseError(response, e.getMessage());
        }
    }
}
