package com.group_3.healthlink.util;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponseUtil {
  /**
   * Creates a JSON response indicating success.
   *
   * @param message the success message to include in the response
   * @return a JsonObject containing the success status and message
   */
  public static JsonObject createSuccessResponse(String message) {
    JsonObject json = new JsonObject();
    json.addProperty("success", true);

    if (message != null && !message.isEmpty())
      json.addProperty("message", message);

    return json;
  }

  /**
   * Creates a JSON response indicating an error.
   *
   * @param message the error message to include in the response
   * @return a JsonObject containing the success status and error message
   */
  public static JsonObject createErrorResponse(String message) {
    JsonObject json = new JsonObject();
    json.addProperty("success", false);

    if (message == null || message.isEmpty())
      message = "An error occurred";

    json.addProperty("error", message);
    return json;
  }

  /**
   * Sends a JSON response to the client.
   *
   * @param response the HttpServletResponse to send the response to
   * @param data     the JsonObject containing the data to send
   */
  public static void sendJsonResponse(HttpServletResponse response, JsonObject data) throws IOException {
    response.setContentType("application/json");

    try (PrintWriter out = response.getWriter()) {
      out.print(data.toString());
      out.flush();
    }
  }

  /**
   * Sends an error response to the client with a specific status code.
   *
   * @param response the HttpServletResponse to send the response to
   * @param message the error message to include in the response
   * @param status the HTTP status code to set for the response
   */
  public static void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");

    JsonObject error = new JsonObject();
    error.addProperty("success", false);
    error.addProperty("error", message);

    try (PrintWriter out = response.getWriter()) {
      out.print(error);
      out.flush();
    }
  }
}