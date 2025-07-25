package com.group_3.healthlink.servlets.medication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Medication;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.DoctorService;
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

                Map<Integer, String> doctorNames = new HashMap<>();
                for (Medication med : medications) {
                    int doctorId = med.getDoctorId();
                    System.out.println(doctorId);
                    if (!doctorNames.containsKey(doctorId)) {
                        Doctor doctor = DoctorService.getByDoctorId(doctorId);
                        if (doctor != null) {
                            doctorNames.put(doctorId, doctor.getFullName());
                        } else {
                            doctorNames.put(doctorId, "Unknown");
                        }
                    }
                }
                request.setAttribute("doctorNames", doctorNames);
            }
        }
        request.getRequestDispatcher("/medications.jsp").forward(request, response);
    }
}