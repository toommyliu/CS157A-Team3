package com.group_3.healthlink.servlets.medication;

import java.io.IOException;
import java.io.PrintWriter;

import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.PatientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "createMedicationServlet", urlPatterns = { "/medication/create" })
public class CreateMedicationServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("/medication/create POST request received");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      response.setStatus(401);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"unauthorized\"}");
      return;
    }

    // Determine patientId: for doctor, from parameter; for patient, from session
    int patientId = -1;
    if (user.isDoctor()) {
      String patientIdParam = request.getParameter("patientId");
      if (patientIdParam != null && !patientIdParam.isEmpty()) {
        patientId = Integer.parseInt(patientIdParam);
      }
    } else {
      Patient patient = PatientService.getByUserId(user.getUserId());
      if (patient != null) {
        patientId = patient.getPatientId();
      }
    }

    String medicationName = request.getParameter("medicationName");
    String dosage = request.getParameter("dosage");
    String frequency = request.getParameter("frequency");
    String noteContent = request.getParameter("noteContent");

    boolean success = MedicationService.createMedication(
        patientId,
        user.getUserId(),
        medicationName,
        dosage,
        frequency,
        noteContent);
    System.out.println("Medication created: " + success);

    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    if (success) {
      response.setStatus(200);
      out.print("{\"success\": true}");
      out.flush();
    } else {
      response.setStatus(500);
      out.print("{\"success\": false}");
      out.flush();
    }
  }
}
