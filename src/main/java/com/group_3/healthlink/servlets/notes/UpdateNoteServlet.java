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

@WebServlet(name = "updateNoteServlet", urlPatterns = { "/notes/update" })
public class UpdateNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JSONObject json = new JSONObject();
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");

    String noteIdStr = request.getParameter("noteId");
    String noteContent = request.getParameter("noteContent");

    if (noteIdStr == null || noteIdStr.isEmpty()) {
      response.setStatus(400);
      json.put("error", "noteId is required");
      out.print(json);
      return;
    }

    if (noteContent == null || noteContent.isEmpty()) {
      response.setStatus(400);
      json.put("error", "noteContent is required");
      out.print(json);
      return;
    }

    int noteId;
    try {
      noteId = Integer.parseInt(noteIdStr);
    } catch (NumberFormatException e) {
      response.setStatus(400);
      json.put("error", "invalid noteId");
      out.print(json);
      return;
    }

    boolean success = NotesService.updateNote(noteId, noteContent);
    System.out.println("Update note: " + success);

    if (success) {
      response.setStatus(200);
    } else {
      response.setStatus(500);
    }

    json.put("success", success);
    out.print(json);
  }
}
