package com.group_3.patientportal;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
  public void init() {
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>Login Successful!</h1>");
    out.println("<p>Email: " + email + "</p>");
    out.println("<p>Password: " + password + "</p>");
    out.println("</body></html>");
  }

  public void destroy() {
  }
}