package com.group_3.healthlink.servlets.admin;

import com.group_3.healthlink.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminDoctorServlet", urlPatterns = { "/admin/doctors" })
public class DoctorsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user != null && user.isAdmin()) {
      request.getRequestDispatcher("/admin/doctors.jsp").forward(request, response);
      return;
    }

    request.getRequestDispatcher(request.getContextPath() + "/dashboard");
  }

}