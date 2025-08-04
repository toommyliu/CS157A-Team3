package com.group_3.healthlink.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.group_3.healthlink.User;
import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.AssignmentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "doctorManagementServlet", urlPatterns = { "/doctors", "/assign-doctor", "/remove-doctor" })
public class DoctorManagementServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        if ("/doctors".equals(path)) {
            handleDoctorsPage(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        if ("/assign-doctor".equals(path)) {
            handleAssignDoctor(request, response);
        } else if ("/remove-doctor".equals(path)) {
            handleRemoveDoctor(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void handleDoctorsPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Only patients should access this page
        if (!user.isPatient()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Forward to the doctors.jsp page
        request.getRequestDispatcher("/doctors.jsp").forward(request, response);
    }
    
    private void handleAssignDoctor(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendErrorResponse(response, "Unauthorized", 401);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Only patients should access this
        if (!user.isPatient()) {
            sendErrorResponse(response, "Forbidden", 403);
            return;
        }
        
        String doctorIdStr = request.getParameter("doctorId");
        if (doctorIdStr == null || doctorIdStr.trim().isEmpty()) {
            sendErrorResponse(response, "Doctor ID is required", 400);
            return;
        }
        
        try {
            int doctorId = Integer.parseInt(doctorIdStr);
            
            // Get the patient object
            com.group_3.healthlink.Patient patient = PatientService.getByUserId(user.getUserId());
            if (patient == null) {
                sendErrorResponse(response, "Patient not found", 404);
                return;
            }
            
            // Check if already assigned
            if (AssignmentService.isAssigned(patient.getPatientId(), doctorId)) {
                sendErrorResponse(response, "Doctor is already assigned to this patient", 400);
                return;
            }
            
            // Assign the doctor
            boolean success = AssignmentService.assignPatientToDoctor(patient.getPatientId(), doctorId);
            
            if (success) {
                SystemLogService.createNew(user.getUserId(), SystemLogAction.ASSIGN_PATIENT_TO_DOCTOR, "Doctor ID: " + doctorId);

                JsonObject result = new JsonObject();
                result.addProperty("success", true);
                result.addProperty("message", "Doctor assigned successfully");
                sendJsonResponse(response, result);
            } else {
                sendErrorResponse(response, "Failed to assign doctor", 500);
            }
            
        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Invalid doctor ID", 400);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Internal server error", 500);
        }
    }
    
    private void handleRemoveDoctor(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendErrorResponse(response, "Unauthorized", 401);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Only patients should access this
        if (!user.isPatient()) {
            sendErrorResponse(response, "Forbidden", 403);
            return;
        }
        
        String doctorIdStr = request.getParameter("doctorId");
        if (doctorIdStr == null || doctorIdStr.trim().isEmpty()) {
            sendErrorResponse(response, "Doctor ID is required", 400);
            return;
        }
        
        try {
            int doctorId = Integer.parseInt(doctorIdStr);
            
            // Get the patient object
            com.group_3.healthlink.Patient patient = PatientService.getByUserId(user.getUserId());
            if (patient == null) {
                sendErrorResponse(response, "Patient not found", 404);
                return;
            }
            
            // Remove the doctor assignment
            boolean success = AssignmentService.removeAssignment(patient.getPatientId(), doctorId);
            
            if (success) {
                SystemLogService.createNew(
                    user.getUserId(),
                    SystemLogAction.REMOVE_PATIENT_FROM_DOCTOR,
                    "Doctor ID: " + doctorId
                );

                JsonObject result = new JsonObject();
                result.addProperty("success", true);
                result.addProperty("message", "Doctor removed successfully");
                sendJsonResponse(response, result);
            } else {
                sendErrorResponse(response, "Failed to remove doctor", 500);
            }
            
        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Invalid doctor ID", 400);
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