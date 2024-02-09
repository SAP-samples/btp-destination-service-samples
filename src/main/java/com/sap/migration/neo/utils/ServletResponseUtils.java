package com.sap.migration.neo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletResponseUtils {

    private ServletResponseUtils() {
    }

    public static void jsonResponseResult(HttpServletResponse response, Object result) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }

    public static void jsonResponseError(HttpServletResponse response, String message) throws IOException {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
