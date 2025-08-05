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

@WebServlet(name = "updateNoteServlet", urlPatterns = { "/notes/update" })
public class UpdateNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user == null || (!user.isPatient() && !user.isDoctor())) {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    String noteIdStr = request.getParameter("noteId");
    String noteContent = request.getParameter("noteContent");

    if (!HttpServletRequestUtil.validateParameter(noteIdStr, "noteId", response))
      return;
    if (!HttpServletRequestUtil.validateParameter(noteContent, "noteContent", response))
      return;

    int noteId;
    try {
      noteId = Integer.parseInt(noteIdStr);
    } catch (NumberFormatException e) {
      JsonResponseUtil.sendErrorResponse(response, "Invalid noteId", 400);
      return;
    }

    boolean success = NotesService.updateNote(noteId, noteContent);
    System.out.println("Update note: " + success);

    JsonResponseUtil.sendJsonResponse(
      response,
      success ? JsonResponseUtil.createSuccessResponse("Success") :
        JsonResponseUtil.createErrorResponse("Failed to update note")
    );
  }
}
