package com.group_3.healthlink.servlets.MedicationLog;

import java.io.IOException;

import java.sql.Timestamp;

import com.group_3.healthlink.User;
import com.group_3.healthlink.services.MedicationLogService;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.util.HttpServletRequestUtil;
import com.group_3.healthlink.SystemLogAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "medicationLogServlet", urlPatterns = { "/medication-log" })
public class MedicationLogServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    response.setContentType("application/json");

    if (user == null || !user.isPatient()) {
      response.setStatus(401);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Unauthorized")
      );
      return;
    }

    String medicationIdStr = request.getParameter("medicationId");
    String dosageTaken = request.getParameter("dosageTaken");
    String takenAt = request.getParameter("takenAt");
    String patientIdStr = request.getParameter("patientId");

    if (!HttpServletRequestUtil.validateParameter(medicationIdStr, "medicationId", response)) return;
    if (!HttpServletRequestUtil.validateParameter(dosageTaken, "dosageTaken", response)) return;
    if (!HttpServletRequestUtil.validateParameter(takenAt, "takenAt", response)) return;
    if (!HttpServletRequestUtil.validateParameter(patientIdStr, "patientId", response)) return;

    String note = request.getParameter("note");
    String takenAtClean = takenAt.replace("T", " ");

    Timestamp takenAtTimestamp;
    try {
      takenAtTimestamp = Timestamp.valueOf(takenAtClean);
    } catch (IllegalArgumentException e) {
      response.setStatus(400);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Invalid takenAt format")
      );
      return;
    }

    int medicationId;
    int patientId;

    try {
      medicationId = Integer.parseInt(medicationIdStr);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Invalid medicationId")
      );
      return;
    }

    try {
      patientId = Integer.parseInt(patientIdStr);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Invalid patientId")
      );
      return;
    }

    boolean success = MedicationLogService.createEntry(medicationId, patientId, dosageTaken, note, takenAtTimestamp);

    if (success) {
      SystemLogService.createNew(
        user.getUserId(),
        SystemLogAction.CREATE_MEDICATION_LOG,
        "Patient ID: " + patientId + ", Medication ID: " + medicationId
      );

      response.setStatus(200);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createSuccessResponse("Medication log entry created successfully")
      );
    } else {
      response.setStatus(500);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Failed to create medication log entry")
      );
    }
  }
}