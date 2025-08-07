package com.group_3.healthlink.servlets.TestResults;

import com.group_3.healthlink.User;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.TestResult;
import com.group_3.healthlink.services.TestResultService;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.DoctorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/test-results")
public class TestResultsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserRole userRole = user.getRole();
        List<TestResult> testResults = null;

        try {
            // Check if a specific patient is requested (for doctors)
            String patientIdParam = req.getParameter("patientId");
            Integer selectedPatientId = null;

            if (patientIdParam != null && !patientIdParam.trim().isEmpty()) {
                try {
                    selectedPatientId = Integer.parseInt(patientIdParam);
                    req.setAttribute("selectedPatientId", selectedPatientId);
                } catch (NumberFormatException e) {
                    // Invalid patient ID, ignore
                }
            }

            // Check if a specific doctor is requested (for patients)
            String doctorIdParam = req.getParameter("doctorId");
            Integer selectedDoctorId = null;

            if (doctorIdParam != null && !doctorIdParam.trim().isEmpty()) {
                try {
                    selectedDoctorId = Integer.parseInt(doctorIdParam);
                    req.setAttribute("selectedDoctorId", selectedDoctorId);
                } catch (NumberFormatException e) {
                    // Invalid doctor ID, ignore
                }
            }

            if (userRole == UserRole.Patient) {
                // Get patient's own test results
                int patientId = PatientService.getByUserId(user.getUserId()).getPatientId();

                if (selectedDoctorId != null) {
                    // Get test results for specific doctor
                    testResults = TestResultService.getTestResultsByPatientAndDoctor(patientId, selectedDoctorId);
                } else {
                    // Get all test results
                    testResults = TestResultService.getTestResultsByPatientId(patientId);
                }
            } else if (userRole == UserRole.Doctor) {
                int doctorId = DoctorService.getByUserId(user.getUserId()).getDoctorId();

                if (selectedPatientId != null) {
                    // Get test results for specific patient
                    testResults = TestResultService.getTestResultsByPatientId(selectedPatientId);
                } else {
                    // Get all test results for doctor's patients
                    testResults = TestResultService.getTestResultsByDoctorId(doctorId);
                }

                // Get patient summary for doctor overview
                List<TestResultService.PatientTestSummary> patientSummaries = TestResultService.getPatientTestSummaryForDoctor(doctorId);
                req.setAttribute("patientSummaries", patientSummaries);
            } else if (userRole == UserRole.Admin) {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            req.setAttribute("testResults", testResults);
            req.setAttribute("userRole", userRole);
            req.getRequestDispatcher("/test-results.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error loading test results: " + e.getMessage());
            req.getRequestDispatcher("/test-results.jsp").forward(req, resp);
        }
    }
}