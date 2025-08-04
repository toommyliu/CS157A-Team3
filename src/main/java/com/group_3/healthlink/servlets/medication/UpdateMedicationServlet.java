package com.group_3.healthlink.servlets.medication;

import java.io.IOException;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Medication;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.util.HttpServletRequestUtil;
import com.group_3.healthlink.SystemLogAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "updateMedicationServlet", urlPatterns = { "/medication/update" })
public class UpdateMedicationServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("POST /medication/update");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null || !user.isDoctor()) {
      response.setStatus(401);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Unauthorized")
      );
      return;
    }

    String medicationIdParam = request.getParameter("medicationId");
    if (!HttpServletRequestUtil.validateParameter(medicationIdParam, "medicationId", response)) return;

    int medicationId;
    try {
      medicationId = Integer.parseInt(medicationIdParam);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Invalid medicationId")
      );
      return;
    }

    Medication existingMedication = MedicationService.getMedicationById(medicationId);
    if (existingMedication == null) {
      response.setStatus(404);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Medication not found")
      );
      return;
    }

    Doctor doctor = DoctorService.getByUserId(user.getUserId());
    if (doctor == null || existingMedication.getDoctorId() != doctor.getDoctorId()) {
      response.setStatus(403);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Unauthorized")
      );
      return;
    }

    String medicationName = request.getParameter("medicationName");
    String dosage = request.getParameter("dosage");
    String frequency = request.getParameter("frequency");
    String noteContent = request.getParameter("noteContent"); // Optional

    if (!HttpServletRequestUtil.validateParameter(medicationName, "medicationName", response)) return;
    if (!HttpServletRequestUtil.validateParameter(dosage, "dosage", response)) return;
    if (!HttpServletRequestUtil.validateParameter(frequency, "frequency", response)) return;

    boolean success = MedicationService.updateMedication(
        medicationId,
        medicationName,
        dosage,
        frequency,
        noteContent
    );
    System.out.println("Medication updated: " + success);

    if (success) {
      SystemLogService.createNew(
        user.getUserId(),
        SystemLogAction.UPDATE_MEDICATION,
        "Patient ID: " + existingMedication.getPatientId() + ", Medication ID: " + medicationId
      );

      response.setStatus(200);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createSuccessResponse("Medication updated successfully")
      );
    } else {
      response.setStatus(500);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Failed to update medication")
      );
    }
  }
}