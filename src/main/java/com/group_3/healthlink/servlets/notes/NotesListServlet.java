package com.group_3.healthlink.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.group_3.healthlink.User;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.Note;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.NotesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "notesListServlet", urlPatterns = { "/notes/list" })
public class NotesListServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");

    if (user == null) {
      response.setStatus(401);
      json.put("error", "unauthorized");
      out.print(json);
      return;
    }

    Patient patient = PatientService.getByUserId(user.getUserId());
    int patientId = patient != null ? patient.getPatientId() : -1;
    Note[] notes = NotesService.getNotesByPatientId(patientId);
    JSONArray notesArr = new JSONArray();

    for (Note note : notes) {
      JSONObject n = new JSONObject();
      n.put("noteId", note.getId());
      n.put("content", note.getContent());
      n.put("createdAt", note.getCreatedAt());
      n.put("doctorId", note.getDoctorId());
      notesArr.put(n);
    }

    json.put("notes", notesArr);
    out.print(json);
  }
}
