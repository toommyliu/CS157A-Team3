package com.group_3.healthlink.servlets.notes;

import java.io.IOException;

import com.group_3.healthlink.User;
import com.group_3.healthlink.services.NotesService;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.util.HttpServletRequestUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "deleteNoteServlet", urlPatterns = { "/notes/delete" })
public class DeleteNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user == null || (!user.isPatient() && !user.isDoctor())) {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    String noteIdParam = request.getParameter("noteId");
    if (!HttpServletRequestUtil.validateParameter(noteIdParam, "noteId", response)) return;
    
    int noteId;
    try {
      noteId = Integer.parseInt(noteIdParam);
    } catch (NumberFormatException e) {
      JsonResponseUtil.sendErrorResponse(response, "Invalid noteId", 400);
      return;
    }

    System.out.println("noteId: " + noteId);

    boolean success = NotesService.deleteNote(noteId);
    System.out.println("Delete note: " + success);

    JsonResponseUtil.sendJsonResponse(
      response,
      success ? JsonResponseUtil.createSuccessResponse("Note deleted successfully") :
        JsonResponseUtil.createErrorResponse("Failed to delete note")
    );
  }
}
