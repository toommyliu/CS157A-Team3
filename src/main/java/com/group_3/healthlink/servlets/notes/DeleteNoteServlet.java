package com.group_3.healthlink.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.group_3.healthlink.services.NotesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "deleteNoteServlet", urlPatterns = { "/notes/delete" })
public class DeleteNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");

    String noteIdParam = request.getParameter("noteId");
    if (noteIdParam == null || noteIdParam.isEmpty()) {
      response.setStatus(400);
      json.put("error", "noteId is required");
      out.print(json);
      return;
    }

    int noteId = -1;
    try {
      noteId = Integer.parseInt(noteIdParam);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      json.put("error", "invalid noteId");
      out.print(json);
      return;
    }

    System.out.println("noteId: " + noteId);

    boolean success = NotesService.deleteNote(noteId);
    System.out.println("Delete note: " + success);

    if (success) {
      response.setStatus(200);
    } else {
      response.setStatus(500);
    }

    json.put("success", success);
    out.print(json);
  }
}
