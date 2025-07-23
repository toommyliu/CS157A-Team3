package com.group_3.healthlink.servlets;

import java.io.IOException;

import com.group_3.healthlink.User;

import com.group_3.healthlink.services.PatientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "OnboardingServlet", urlPatterns = {"/onboarding"})
public class OnboardingServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher("/onboarding.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("POST /onboarding");

    User user = (User) request.getSession().getAttribute("user");

    String dateOfBirth = request.getParameter("dateOfBirth");
    String phoneNumber = request.getParameter("phoneNumber");
    String address = request.getParameter("address");
    String emergencyContactName = request.getParameter("emergencyContactName");
    String emergencyContactPhone = request.getParameter("emergencyContactPhone");

    boolean success = PatientService.createNew(user.getUserId(),
            dateOfBirth, phoneNumber, address,
            emergencyContactName, emergencyContactPhone);
    System.out.println("Onboarding success: " + success);

    if (success) {
      response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
      request.setAttribute("error", true);
      request.getRequestDispatcher("/onboarding.jsp").forward(request, response);
    }
  }
}