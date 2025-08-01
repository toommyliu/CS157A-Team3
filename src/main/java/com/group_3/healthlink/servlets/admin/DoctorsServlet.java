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

    response.sendRedirect(request.getContextPath() + "/dashboard");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");

    PrintWriter out = response.getWriter();
    JSONObject json = new JSONObject();

    User user = (User) request.getSession().getAttribute("user");

    if (user != null && user.isAdmin()) {
      String action = request.getParameter("action");
      
      if ("update".equals(action)) {
        handleUpdate(request, response, json, out);
      } else {
        handleCreate(request, response, json, out);
      }
    } else {
      response.setStatus(401);
      json.put("error", "Unauthorized");
      out.print(json);
    }
  }

  private void handleCreate(HttpServletRequest request, HttpServletResponse response, JSONObject json, PrintWriter out) throws IOException {
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

    System.out.println("Creating doctor - firstName: " + firstName);
    System.out.println("Creating doctor - lastName: " + lastName);
    System.out.println("Creating doctor - email: " + email);
    System.out.println("Creating doctor - department: " + department);

    int userId = AuthService.registerUser(firstName, lastName, email, password, "doctor");
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
  }

  private void handleUpdate(HttpServletRequest request, HttpServletResponse response, JSONObject json, PrintWriter out) throws IOException {
    String doctorIdStr = request.getParameter("doctorId");
    String userIdStr = request.getParameter("userId");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String department = request.getParameter("department");

    if (doctorIdStr == null || doctorIdStr.isEmpty()) {
      json.put("error", "doctorId is required");
      response.setStatus(400);
      out.print(json);
      return;
    }

    if (userIdStr == null || userIdStr.isEmpty()) {
      json.put("error", "userId is required");
      response.setStatus(400);
      out.print(json);
      return;
    }

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

    if (department == null || department.isEmpty()) {
      json.put("error", "department is required");
      response.setStatus(400);
      out.print(json);
      return;
    }

    try {
      int doctorId = Integer.parseInt(doctorIdStr);
      int userId = Integer.parseInt(userIdStr);

      System.out.println("Updating doctor - doctorId: " + doctorId);
      System.out.println("Updating doctor - userId: " + userId);
      System.out.println("Updating doctor - firstName: " + firstName);
      System.out.println("Updating doctor - lastName: " + lastName);
      System.out.println("Updating doctor - email: " + email);
      System.out.println("Updating doctor - department: " + department);

      // Update user information
      boolean userSuccess = DoctorService.updateUser(userId, firstName, lastName, email, password);
      
      // Update doctor information
      boolean doctorSuccess = DoctorService.updateDoctor(doctorId, department);

      if (userSuccess && doctorSuccess) {
        json.put("success", true);
        response.setStatus(200);
        response.sendRedirect(request.getContextPath() + "/admin/doctors");
        return;
      } else {
        json.put("error", "Failed to update doctor");
        response.setStatus(500);
        out.print(json);
      }
    } catch (NumberFormatException e) {
      json.put("error", "Invalid doctorId or userId");
      response.setStatus(400);
      out.print(json);
    }
  }


}