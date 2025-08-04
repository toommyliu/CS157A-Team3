package com.group_3.healthlink.servlets.medication;

import java.io.IOException;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Medication;
import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.util.HttpServletRequestUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "deleteMedicationServlet", urlPatterns = { "/medication/delete" })
public class DeleteMedicationServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("POST /medication/delete");

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

    boolean success = MedicationService.deleteMedication(medicationId);
    System.out.println("Medication deleted: " + success);

    if (success) {
      SystemLogService.createNew(
        doctor.getUserId(),
        SystemLogAction.DELETE_MEDICATION,
        "Patient ID: " + existingMedication.getPatientId() + ", Medication ID: " + medicationId
      );

      response.setStatus(200);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createSuccessResponse("Medication deleted successfully")
      );
    } else {
      response.setStatus(500);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Failed to delete medication")
      );
    }
  }
}
