package com.group_3.healthlink.servlets.medication;

import java.io.IOException;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.util.HttpServletRequestUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "createMedicationServlet", urlPatterns = { "/medication/create" })
public class CreateMedicationServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("POST /medication/create");

    User user = (User) request.getSession().getAttribute("user");

    if (user == null || !user.isDoctor()) {
      response.setStatus(401);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Unauthorized")
      );

      return;
    }

    int patientId = -1;
    int doctorId = -1;

    if (user.isDoctor()) {
      String patientIdParam = request.getParameter("patientId");
      if (patientIdParam != null && !patientIdParam.isEmpty()) {
        try {
          patientId = Integer.parseInt(patientIdParam);
        } catch (NumberFormatException e) {
          response.setStatus(400);
          JsonResponseUtil.sendJsonResponse(
            response,
            JsonResponseUtil.createErrorResponse("Invalid patientId")
          );

          return;
        }
      }

      Doctor doctor = DoctorService.getByUserId(user.getUserId());
      if (doctor != null)
        doctorId = doctor.getDoctorId();
    } else {
      Patient patient = PatientService.getByUserId(user.getUserId());
      if (patient != null)
        patientId = patient.getPatientId();
    }

    String medicationName = request.getParameter("medicationName");
    String dosage = request.getParameter("dosage");
    String frequency = request.getParameter("frequency");
    String noteContent = request.getParameter("noteContent"); // Optional so we don't validate it

    if (!HttpServletRequestUtil.validateParameter(medicationName, "medicationName", response)) return;
    if (!HttpServletRequestUtil.validateParameter(dosage, "dosage", response)) return;
    if (!HttpServletRequestUtil.validateParameter(frequency, "frequency", response)) return;

    boolean success = MedicationService.createMedication(
        patientId,
        doctorId,
        medicationName,
        dosage,
        frequency,
        noteContent
    );
    System.out.println("Medication created: " + success);

    if (success) {
      SystemLogService.createNew(user.getUserId(), SystemLogAction.CREATE_MEDICATION, "Patient ID: " + patientId);

      response.setStatus(200);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createSuccessResponse("Medication created successfully")
      );
    } else {
      response.setStatus(500);
      JsonResponseUtil.sendJsonResponse(
        response,
        JsonResponseUtil.createErrorResponse("Failed to create medication")
      );
    }
  }
}
