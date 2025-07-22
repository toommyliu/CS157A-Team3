package com.group_3.healthlink.servlets.notes;

import java.io.IOException;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
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
    System.out.println("/notes/create POST request received");

    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      response.setStatus(401);
      response.getWriter().write("{\"error\": \"unauthorized\"}");
      return;
    }

    Patient patient = PatientService.getByUserId(user.getUserId());
    Doctor[] doctors = PatientService.getDoctors(patient.getPatientId());

    String noteContent = request.getParameter("noteContent");
    System.out.println("Note content: " + noteContent);
    String doctorId = request.getParameter("doctorId");
    System.out.println("Selected doctor ID: " + doctorId);

    response.sendRedirect(request.getContextPath() + "/notes");
  }
}
