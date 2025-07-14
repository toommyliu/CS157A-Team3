package com.group_3.healthlink;

import java.io.IOException;

import com.group_3.healthlink.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "registerServlet", urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/register.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
     throws IOException {

    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String emailAddress = request.getParameter("email");
    String password = request.getParameter("password");
    String role = request.getParameter("role");

    System.out.println("First Name: " + firstName);
    System.out.println("Last Name: " + lastName);
    System.out.println("Email Address: " + emailAddress);
    System.out.println("Password: " + password);
    System.out.println("Role: " + role);

    boolean success = AuthService.registerUser(
        firstName,
        lastName,
        emailAddress,
        password,
        role
    );

    if (success) {
      System.out.println("User registered successfully.");
    } else {
      System.out.println("Failed to register user.");
    }

    response.sendRedirect(request.getContextPath() + "/");
  }
}
