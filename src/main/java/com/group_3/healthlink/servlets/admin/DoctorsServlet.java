package com.group_3.healthlink.servlets.admin;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.DoctorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "adminDoctorServlet", urlPatterns = { "/admin/doctors" })
public class DoctorsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user != null && user.isAdmin()) {
      Doctor[] doctors = DoctorService.getAll();
      request.setAttribute("doctors", doctors);

      request.getRequestDispatcher("/admin/doctors.jsp").forward(request, response);
      return;
    }

    request.getRequestDispatcher(request.getContextPath() + "/dashboard");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");

    PrintWriter out = response.getWriter();
    JSONObject json = new JSONObject();

    User user = (User) request.getSession().getAttribute("user");

    if (user != null && user.isAdmin()) {

      String firstName = request.getParameter("firstName");
      String lastName = request.getParameter("lastName");
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      String department = request.getParameter("department");

      if (firstName == null || firstName.isEmpty()) {
        json.put("error", "firstName is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      if (lastName == null || lastName.isEmpty()) {
        json.put("error", "lastName is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      if (email == null || email.isEmpty()) {
        json.put("error", "email is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      if (password == null || password.isEmpty()) {
        json.put("error", "password is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      if (department == null || department.isEmpty()) {
        json.put("error", "department is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      System.out.println("firstName: " + firstName);
      System.out.println("lastName: " + lastName);
      System.out.println("email: " + email);
      System.out.println("password: " + password);
      System.out.println("department: " + department);

      int userId = AuthService.registerUser(firstName, password, email, password, "doctor");
      if (userId != -1) {
        boolean doctorSuccess = DoctorService.createNew(userId, department);
        json.put("success", doctorSuccess);

        if (doctorSuccess) {
          response.setStatus(200);
          response.sendRedirect(request.getContextPath() + "/admin/doctors");
          return;
        }
      }

      response.setStatus(500);
      out.print(json);
    } else {
      response.setStatus(401);
      json.put("error", "Unauthorized");
      out.print(json);
    }
  }
}