package com.group_3.healthlink.servlets.notes;

import java.io.IOException;
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
    response.sendRedirect(request.getContextPath() + "/notes");
  }
}
