package com.group_3.healthlink.util;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponseUtil {
  public static JsonObject createSuccessResponse(String message) {
    JsonObject json = new JsonObject();
    json.addProperty("success", true);

    if (message != null && !message.isEmpty())
      json.addProperty("message", message);
    
    return json;
  }

  public static JsonObject createErrorResponse(String message) {
    JsonObject json = new JsonObject();
    json.addProperty("success", false);

    if (message == null || message.isEmpty())
      message = "An error occurred";

    json.addProperty("error", message);
    return json;
  }

  public static void sendJsonResponse(HttpServletResponse response, JsonObject data) throws IOException {
    response.setContentType("application/json");
    try (PrintWriter out = response.getWriter()) {
      out.print(data.toString());
      out.flush();
    }
  }

  public static void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    JsonObject error = new JsonObject();
    error.addProperty("success", false);
    error.addProperty("error", message);
    try (PrintWriter out = response.getWriter()) {
      out.print(error.toString());
      out.flush();
    }
  }
}