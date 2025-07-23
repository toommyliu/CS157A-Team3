package com.group_3.healthlink.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.NotesService;
import com.group_3.healthlink.services.PatientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "createNoteServlet", urlPatterns = { "/notes/create" })
public class CreateNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("POST /notes/create");

    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      response.setStatus(401);
      json.put("error", "unauthorized");
      out.print(json);
      return;
    }

    Patient patient = PatientService.getByUserId(user.getUserId());

    if (patient == null) {
      response.setStatus(404);
      json.put("error", "patient not found");
      out.print(json);
      return;
    }

    String noteContent = request.getParameter("noteContent");
    String doctorId = request.getParameter("doctorId");

    if (noteContent == null || noteContent.isEmpty()) {
      response.setStatus(400);
      json.put("error", "noteContent is required");
      out.print(json);
      return;
    }

    if (doctorId == null || doctorId.isEmpty()) {
      response.setStatus(400);
      json.put("error", "doctorId is required");
      out.print(json);
      return;
    }

    boolean success = NotesService.createNote(
        noteContent,
        patient.getPatientId(),
        doctorId != null && !doctorId.isEmpty() ? Integer.parseInt(doctorId) : -1);
    System.out.println("Note created: " + success);

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
