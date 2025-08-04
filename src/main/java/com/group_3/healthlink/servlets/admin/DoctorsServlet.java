package com.group_3.healthlink.servlets.admin;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.util.JsonResponseUtil;

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

    User user = (User) request.getSession().getAttribute("user");

    if (user != null && user.isAdmin()) {
      String action = request.getParameter("action");
      
      if ("update".equals(action)) {
        handleUpdate(request, response);
      } else {
        handleCreate(request, response);
      }
    } else {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
}
  }

  private boolean validateParameter(String value, String fieldName, HttpServletResponse response) throws IOException {
    if (value == null || value.isEmpty()) {
      JsonResponseUtil.sendErrorResponse(response, fieldName + " is required", 400);
      return false;
    }

    return true;
  }

  private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String department = request.getParameter("department");

    if (!validateParameter(firstName, "firstName", response)) return;
    if (!validateParameter(lastName, "lastName", response)) return;
    if (!validateParameter(email, "email", response)) return;
    if (!validateParameter(password, "password", response)) return;
    if (!validateParameter(department, "department", response)) return;

    int userId = AuthService.registerUser(firstName, lastName, email, password, "doctor");
    if (userId != -1) {
      boolean doctorSuccess = DoctorService.createNew(userId, department);
      if (doctorSuccess) {
        User authedUser = (User) request.getSession().getAttribute("user");
        SystemLogService.createNew(authedUser.getUserId(), SystemLogAction.CREATE_DOCTOR, "Dr. " + firstName + " " + lastName + " (ID: " + userId + ")");
        response.setStatus(200);
        response.sendRedirect(request.getContextPath() + "/admin/doctors");
        return;
      }
    }

    JsonResponseUtil.sendErrorResponse(response, "Failed to create doctor", 500);
  }

  private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String doctorIdStr = request.getParameter("doctorId");
    String userIdStr = request.getParameter("userId");

    if (!validateParameter(doctorIdStr, "doctorId", response)) return;
    if (!validateParameter(userIdStr, "userId", response)) return;
    int doctorId;
    int userId;

    try {
      doctorId = Integer.parseInt(doctorIdStr);
    } catch (NumberFormatException e) {
      JsonResponseUtil.sendErrorResponse(response, "Invalid doctorId format", 400);
      return;
    }
    try {
      userId = Integer.parseInt(userIdStr);
    } catch (NumberFormatException e) {
      JsonResponseUtil.sendErrorResponse(response, "Invalid userId format", 400);
      return;
    }

    // In the query, they are checked for null or empty and conditionally added to
    // the update statement as needed
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String department = request.getParameter("department");

    boolean userSuccess = AuthService.updateUser(userId, firstName, lastName, email, password);
    boolean doctorSuccess = DoctorService.updateDoctor(doctorId, department);

    if (userSuccess && doctorSuccess) {
      User authedUser = (User) request.getSession().getAttribute("user");
      SystemLogService.createNew(authedUser.getUserId(), SystemLogAction.UPDATE_DOCTOR, "Doctor ID: " + doctorId);

      response.setStatus(200);
      response.sendRedirect(request.getContextPath() + "/admin/doctors");
    } else {
      JsonResponseUtil.sendErrorResponse(response, "Failed to update doctor", 500);
    }
  }
}