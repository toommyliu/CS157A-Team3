package com.group_3.healthlink.servlets.patients;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Medication;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.TestResultService;
import com.group_3.healthlink.TestResult;

@WebServlet(name = "patientDetailServlet", urlPatterns = { "/patients/*" })
public class PatientDetailServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
      request.getRequestDispatcher("/patients.jsp").forward(request, response);
    } else {
      String patientIdStr = pathInfo.substring(1);
      try {
        int patientId = Integer.parseInt(patientIdStr);

        Patient patient = PatientService.getByUserId(patientId);
        if (patient != null) {
          User user = AuthService.getUserById(patient.getUserId());
          if (user != null) {
            patient.setUser(user);
          }

          Doctor[] assignedDoctors = PatientService.getDoctors(patient.getPatientId());
          List<Medication> medications = MedicationService.getMedicationsByPatientId(patient.getPatientId());
          List<TestResult> testResults = TestResultService.getTestResultsByPatientId(patient.getPatientId());

          request.setAttribute("patient", patient);
          request.setAttribute("assignedDoctors", assignedDoctors);
          request.setAttribute("medications", medications);
          request.setAttribute("testResults", testResults);
          request.setAttribute("patientId", patientIdStr);
        } else {
          request.setAttribute("patientId", patientIdStr);
          request.setAttribute("patientNotFound", true);
        }
      } catch (NumberFormatException e) {
        request.setAttribute("patientId", patientIdStr);
        request.setAttribute("invalidPatientId", true);
      }

      request.getRequestDispatcher("/patients.jsp").forward(request, response);
    }
  }
}
