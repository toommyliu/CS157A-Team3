package com.group_3.healthlink.servlets.medication;

import java.io.IOException;
import java.io.PrintWriter;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
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
    System.out.println("POST /medication/create");

    User user = (User) request.getSession().getAttribute("user");
    PrintWriter out = response.getWriter();
    org.json.JSONObject json = new org.json.JSONObject();
    if (user == null) {
      response.setStatus(401);
      response.setContentType("application/json");
      json.put("error", "unauthorized");
      out.print(json);
      return;
    }

    int patientId = -1;
    int doctorId = -1;

    if (user.isDoctor()) {
      String patientIdParam = request.getParameter("patientId");
      if (patientIdParam != null && !patientIdParam.isEmpty()) {
        patientId = Integer.parseInt(patientIdParam);
      }

      Doctor doctor = DoctorService.getByUserId(user.getUserId());
      if (doctor != null) {
        doctorId = doctor.getDoctorId();
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
        doctorId,
        medicationName,
        dosage,
        frequency,
        noteContent);
    System.out.println("Medication created: " + success);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    if (success) {
      response.setStatus(200);
      json.put("success", true);
    } else {
      response.setStatus(500);
      json.put("success", false);
    }
    out.print(json);
  }
}
