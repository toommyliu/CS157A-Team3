package com.group_3.healthlink.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HttpServletRequestUtil {
  /**
   * Validates a required parameter from the request.
   *
   * @param value the value of the parameter to validate
   * @param fieldName the name of the parameter field and for error messages
   * @param response the HttpServletResponse to send error messages
   * @return true if the parameter is valid, false otherwise
   */
  public static boolean validateParameter(String value, String fieldName, HttpServletResponse response) throws IOException {
    if (value == null || value.isEmpty()) {
      JsonResponseUtil.sendErrorResponse(response, fieldName + " is required", 400);
      return false;
    }

    return true;
  }
}
