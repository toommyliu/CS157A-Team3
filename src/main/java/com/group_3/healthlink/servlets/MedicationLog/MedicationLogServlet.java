package com.group_3.healthlink.servlets.MedicationLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import com.group_3.healthlink.User;

import com.group_3.healthlink.services.MedicationLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet(name = "medicationLogServlet", urlPatterns = { "/medication-log" })
public class MedicationLogServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");

    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();

    response.setContentType("application/json");

    if (user == null || !user.isPatient()) {
      response.setStatus(400);
      json.put("error", "Unauthorized");
      out.print(json);
      return;
    }

    String medicationIdStr = request.getParameter("medicationId");
    if (medicationIdStr == null || medicationIdStr.isEmpty()) {
      response.setStatus(400);
      json.put("error", "medicationIdStr is required");
      out.print(json);
      return;
    }

    String dosageTaken = request.getParameter("dosageTaken");
    if (dosageTaken == null || dosageTaken.isEmpty()) {
      response.setStatus(400);
      json.put("error", "dosageTaken is required");
      out.print(json);
      return;
    }

    String takenAt = request.getParameter("takenAt");
    if (takenAt == null || takenAt.isEmpty()) {
      response.setStatus(400);
      json.put("error", "takenAt is required");
      out.print(json);
      return;
    }

    String patientIdStr = request.getParameter("patientId");
    if (patientIdStr == null || patientIdStr.isEmpty()) {
      response.setStatus(400);
      json.put("error", "patientId is required");
      out.print(json);
      return;
    }

    String note = request.getParameter("note");
    String takenAtClean = takenAt.replace("T", " " );

    java.sql.Timestamp takenAtTimestamp = Timestamp.valueOf(takenAtClean);

    int medicationId = Integer.parseInt(medicationIdStr);
    int patientId = Integer.parseInt(patientIdStr);

    System.out.println("medicationId: " + medicationId);
    System.out.println("patientId: " + patientId);
    System.out.println("dosageTaken: " + dosageTaken);
    System.out.println("note: " + (note != null && !note.isEmpty() ? note : "no note provided"));
    System.out.println("takenAt: " + (takenAt != null && !takenAt.isEmpty() ? takenAt : "no taken at provided"));

    boolean success = MedicationLogService.createEntry(medicationId, patientId, dosageTaken, note, takenAtTimestamp);
    if (success) {
      response.setStatus(200);
      json.put("message", "Success");
      out.print(json);
    } else {
      response.setStatus(400);
      json.put("message", "Error");
      out.print(json);
    }
  }
}