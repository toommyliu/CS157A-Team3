package com.group_3.healthlink.servlets.admin;

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

@WebServlet(name = "adminUpdateDoctorServlet", urlPatterns = { "/admin/doctors/update" })
public class UpdateDoctorServlet extends HttpServlet {
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
      String userIdStr = request.getParameter("userId");
      String doctorIdStr = request.getParameter("doctorId");

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

      if (userIdStr == null || userIdStr.isEmpty()) {
        json.put("error", "userId is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      if (doctorIdStr == null || doctorIdStr.isEmpty()) {
        json.put("error", "doctorId is required");
        response.setStatus(400);
        out.print(json);
        return;
      }

      int userId = Integer.parseInt(userIdStr);
      int doctorId = Integer.parseInt(doctorIdStr);

      System.out.println("firstName: " + firstName);
      System.out.println("lastName: " + lastName);
      System.out.println("email: " + email);
      System.out.println("password: " + password);
      System.out.println("department: " + department);
      System.out.println("userId: " + userId);
      System.out.println("doctorId: " + doctorId);

      boolean userUpdateSuccess = AuthService.updateUser(userId, firstName, lastName, email, password);
      if (!userUpdateSuccess) {
        json.put("error", "Failed to update user");
        response.setStatus(500);
        out.print(json);
        return;
      }

      boolean doctorUpdateSuccess = DoctorService.updateDoctor(doctorId, department);
      json.put("success", doctorUpdateSuccess);
      if (doctorUpdateSuccess) {
        response.setStatus(200);
        response.sendRedirect(request.getContextPath() + "/admin/doctors");
        return;
      }

      out.print(json);
    } else {
      response.setStatus(401);
      json.put("error", "Unauthorized");
      out.print(json);
    }
  }
}
