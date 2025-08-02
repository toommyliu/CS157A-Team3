package com.group_3.healthlink.servlets.TestResults;

import com.group_3.healthlink.User;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.services.TestResultService;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/test-results/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class UploadTestResultServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Only patients can upload test results
        if (user.getRole() != UserRole.Patient) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Only patients can upload test results");
            return;
        }

        try {
            // Get form data
            String description = req.getParameter("description");
            String doctorIdStr = req.getParameter("doctorId");
            
            if (doctorIdStr == null || doctorIdStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Doctor ID is required");
                return;
            }

            int doctorId = Integer.parseInt(doctorIdStr);
            
            // Get the uploaded file
            Part filePart = req.getPart("file");
            if (filePart == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("No file uploaded");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            if (fileName == null || fileName.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid file name");
                return;
            }

            // Validate file type
            String fileType = getFileType(fileName);
            if (!isValidFileType(fileType)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid file type. Only PDF and image files are allowed.");
                return;
            }

            // Read file data
            byte[] fileData = new byte[(int) filePart.getSize()];
            try (InputStream inputStream = filePart.getInputStream()) {
                inputStream.read(fileData);
            }

            // Get patient ID
            Patient patient = PatientService.getByUserId(user.getUserId());
            if (patient == null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Patient not found");
                return;
            }

            // Verify that the doctor is assigned to this patient
            List<Integer> assignedDoctorIds = TestResultService.getAssignedDoctorIds(patient.getPatientId());
            if (!assignedDoctorIds.contains(doctorId)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("You can only upload test results for your assigned doctors");
                return;
            }

            // Upload the test result
            boolean success = TestResultService.uploadTestResult(
                patient.getPatientId(), 
                doctorId, 
                fileName, 
                fileData, 
                fileType, 
                description
            );

            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Test result uploaded successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Failed to upload test result");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid doctor ID");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error uploading test result: " + e.getMessage());
        }
    }

    private String getFileType(String fileName) {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1) return "";
        return fileName.substring(lastDot + 1).toLowerCase();
    }

    private boolean isValidFileType(String fileType) {
        return fileType.equals("pdf") || 
               fileType.equals("jpg") || 
               fileType.equals("jpeg") || 
               fileType.equals("png") || 
               fileType.equals("gif");
    }
} 