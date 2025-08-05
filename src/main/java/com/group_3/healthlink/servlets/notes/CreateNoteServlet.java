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

@WebServlet(name = "createNoteServlet", urlPatterns = { "/notes/create" })
public class CreateNoteServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user == null || (!user.isPatient() && !user.isDoctor())) {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    String noteContent = request.getParameter("noteContent");
    if (!HttpServletRequestUtil.validateParameter(noteContent, "noteContent", response))
      return;

    if (noteContent.length() > 255)
      noteContent = noteContent.substring(0, 255);

    boolean success = NotesService.createNote(
        noteContent,
        user.getUserId()
    );

    System.out.println("Note created: " + success);

    JsonResponseUtil.sendJsonResponse(
      response,
      success ? JsonResponseUtil.createSuccessResponse("Success") :
        JsonResponseUtil.createErrorResponse("Failed to create note")
    );
  }
}
