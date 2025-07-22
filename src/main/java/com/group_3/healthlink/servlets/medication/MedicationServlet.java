package com.group_3.healthlink.servlets.medication;

import java.io.IOException;
import java.util.List;

import com.group_3.healthlink.Medication;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.MedicationService;
import com.group_3.healthlink.services.PatientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "medicationServlet", urlPatterns = { "/medications" })
public class MedicationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            Patient patient = PatientService.getByUserId(user.getUserId());
            if (patient != null) {
                int patientId = patient.getPatientId();
                List<Medication> medications = MedicationService.getMedicationsByPatientId(patientId);
                request.setAttribute("medications", medications);
            }
        }
        request.getRequestDispatcher("/medications.jsp").forward(request, response);
    }
}