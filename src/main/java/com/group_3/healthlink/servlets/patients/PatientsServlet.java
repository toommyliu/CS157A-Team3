package com.group_3.healthlink.servlets.patients;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.AssignmentService;
import com.google.gson.JsonObject;

@WebServlet(name = "patientsServlet", urlPatterns = { "/patients", "/remove-patient" })
public class PatientsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher("/patients.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    String contextPath = request.getContextPath();
    String path = requestURI.substring(contextPath.length());
    
    if ("/remove-patient".equals(path)) {
      handleRemovePatient(request, response);
    } else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void handleRemovePatient(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    User user = (User) session.getAttribute("user");
    
    // Only doctors should access this
    if (!user.isDoctor()) {
      sendErrorResponse(response, "Forbidden", 403);
      return;
    }

    String patientIdStr = request.getParameter("patientId");
    if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
      sendErrorResponse(response, "Patient ID is required", 400);
      return;
    }

    try {
      int patientId = Integer.parseInt(patientIdStr);
      
      // Get the doctor object
      com.group_3.healthlink.Doctor doctor = DoctorService.getByUserId(user.getUserId());
      if (doctor == null) {
        sendErrorResponse(response, "Doctor not found", 404);
        return;
      }
      
      // Remove the patient assignment
      boolean success = AssignmentService.removeAssignment(patientId, doctor.getDoctorId());
      
      if (success) {
        JsonObject result = new JsonObject();
        result.addProperty("success", true);
        result.addProperty("message", "Patient removed successfully");
        sendJsonResponse(response, result);
      } else {
        sendErrorResponse(response, "Failed to remove patient", 500);
      }
      
    } catch (NumberFormatException e) {
      sendErrorResponse(response, "Invalid patient ID", 400);
    } catch (Exception e) {
      e.printStackTrace();
      sendErrorResponse(response, "Internal server error", 500);
    }
  }

  private void sendJsonResponse(HttpServletResponse response, JsonObject data) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    try (PrintWriter out = response.getWriter()) {
      out.print(data.toString());
      out.flush();
    }
  }

  private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    JsonObject error = new JsonObject();
    error.addProperty("success", false);
    error.addProperty("error", message);
    
    try (PrintWriter out = response.getWriter()) {
      out.print(error.toString());
      out.flush();
    }
  }
}