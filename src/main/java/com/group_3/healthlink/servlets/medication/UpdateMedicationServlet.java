package com.group_3.healthlink.servlets.medication;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Medication;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.MedicationService;

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

    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null || !user.isDoctor()) {
      response.setStatus(401);
      json.put("error", "unauthorized");
      out.print(json);
      return;
    }

    String medicationIdParam = request.getParameter("medicationId");
    if (medicationIdParam == null || medicationIdParam.isEmpty()) {
      response.setStatus(400);
      json.put("error", "medicationId is required");
      out.print(json);
      return;
    }

    int medicationId;
    try {
      medicationId = Integer.parseInt(medicationIdParam);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      json.put("error", "invalid medicationId");
      out.print(json);
      return;
    }

    Medication existingMedication = MedicationService.getMedicationById(medicationId);
    if (existingMedication == null) {
      response.setStatus(404);
      json.put("error", "medication not found");
      out.print(json);
      return;
    }

    Doctor doctor = DoctorService.getByUserId(user.getUserId());
    if (doctor == null || existingMedication.getDoctorId() != doctor.getDoctorId()) {
      response.setStatus(403);
      json.put("error", "unauthorized");
      out.print(json);
      return;
    }

    String medicationName = request.getParameter("medicationName");
    String dosage = request.getParameter("dosage");
    String frequency = request.getParameter("frequency");
    String noteContent = request.getParameter("noteContent");

    if (medicationName == null || medicationName.isEmpty()) {
      response.setStatus(400);
      json.put("error", "medicationName is required");
      out.print(json);
      return;
    }

    if (dosage == null || dosage.isEmpty()) {
      response.setStatus(400);
      json.put("error", "dosage is required");
      out.print(json);
      return;
    }

    if (frequency == null || frequency.isEmpty()) {
      response.setStatus(400);
      json.put("error", "frequency is required");
      out.print(json);
      return;
    }

    if (noteContent == null || noteContent.isEmpty()) {
      response.setStatus(400);
      json.put("error", "noteContent is required");
      out.print(json);
      return;
    }

    boolean success = MedicationService.updateMedication(
        medicationId,
        medicationName,
        dosage,
        frequency,
        noteContent);
    System.out.println("Medication updated: " + success);

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