package com.group_3.healthlink.servlets;

import java.io.IOException;

import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.SystemLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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

    System.out.println("First Name: " + firstName);
    System.out.println("Last Name: " + lastName);
    System.out.println("Email Address: " + emailAddress);
    System.out.println("Password: " + password);
    System.out.println("Role: patient");

    if (AuthService.isEmailRegistered(emailAddress)) {
      System.out.println("Email address already registered: " + emailAddress);
      request.getSession().setAttribute("registerError", "Email address is already registered.");
      response.sendRedirect(request.getContextPath() + "/register");
      return;
    }

    int userId = AuthService.registerUser(
        firstName,
        lastName,
        emailAddress,
        password,
        UserRole.Patient.toString().toLowerCase()
    );

    if (userId != -1) {
      SystemLogService.createNew(
        userId,
        SystemLogAction.NEW_USER,
        null
      );

      System.out.println("User registered successfully with userId: " + userId);

      User newUser = AuthService.getUserById(userId);
      if (newUser != null) {
        request.getSession().setAttribute("user", newUser);
      }

      Cookie userCookie = new Cookie("userId", String.valueOf(userId));
      userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
      userCookie.setPath("/");
      response.addCookie(userCookie);
      response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
      System.out.println("Failed to register user.");
      response.sendRedirect(request.getContextPath() + "/register");
    }
  }
}
