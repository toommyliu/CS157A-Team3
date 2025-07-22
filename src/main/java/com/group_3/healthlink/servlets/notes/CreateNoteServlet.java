package com.group_3.healthlink.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;

import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.NotesService;
import com.group_3.healthlink.services.PatientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet(name = "createNoteServlet", urlPatterns = { "/notes/create" })
public class CreateNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("/notes/create POST request received");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      response.setStatus(401);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"unauthorized\"}");
      return;
    }

    Patient patient = PatientService.getByUserId(user.getUserId());

    String noteContent = request.getParameter("noteContent");
    String doctorId = request.getParameter("doctorId");

    boolean success = NotesService.createNote(
        noteContent,
        patient.getPatientId(),
        doctorId != null && !doctorId.isEmpty() ? Integer.parseInt(doctorId) : -1);
    System.out.println("Note created: " + success);

    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    JSONObject json = new JSONObject();

    if (success) {
      response.setStatus(200);
      json.put("success", true);
      out.print(json.toString());
      out.flush();
    } else {
      response.setStatus(500);
      json.put("success", false);
      out.flush();
    }
  }
}
