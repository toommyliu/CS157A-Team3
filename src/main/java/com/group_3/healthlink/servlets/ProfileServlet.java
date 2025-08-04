package com.group_3.healthlink.servlets;

import com.group_3.healthlink.User;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.DoctorService;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.util.JsonResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "profileServlet", urlPatterns = { "/profile" })
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userIdParam = request.getParameter("userId");
        User viewedUser = currentUser;

        if (currentUser.isAdmin() && userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);
                User user = AuthService.getUserById(userId);
                if (user != null)
                    viewedUser = user;
            } catch (NumberFormatException e) {
            }
        }
        request.setAttribute("viewedUser", viewedUser);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            JsonResponseUtil.sendErrorResponse(response, "User not authenticated", 401);
            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            JsonResponseUtil.sendErrorResponse(response, "action parameter is required", 400);
            return;
        }

        if (!action.equals("personal") &&
                !action.equals("patient") &&
                !action.equals("doctor")) {
            JsonResponseUtil.sendErrorResponse(response, "Invalid action parameter", 400);
            return;
        }

        System.out.println("action = " + action);

        if (action.equals("personal")) {
            String userIdStr = request.getParameter("userId");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (userIdStr == null || userIdStr.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "userId is required", 400);
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                JsonResponseUtil.sendErrorResponse(response, "userId must be a valid number", 400);
                return;
            }

            if (fullName == null || fullName.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "fullName is required", 400);
                return;
            }

            String[] parts = fullName.split(" ");
            if (parts.length < 2) {
                JsonResponseUtil.sendErrorResponse(response, "fullName must contain at least first and last name", 400);
                return;
            }

            if (email == null || email.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "email is required", 400);
                return;
            }

            System.out.println("userId = " + userId);
            System.out.println("fullName = " + fullName);
            System.out.println("email = " + email);

            String firstName = parts[0];
            String lastName = parts[1];

            boolean success = AuthService.updateUserProfile(userId, firstName, lastName, email);
            JsonResponseUtil.sendJsonResponse(response,
                    success ? JsonResponseUtil.createSuccessResponse("Profile updated successfully")
                            : JsonResponseUtil.createErrorResponse("Failed to update profile"));

            // Need to update the session user object to reflect changes, before redirecting
            if (success) {
                User updatedUser = AuthService.getUserById(userId);
                if (updatedUser != null)
                    request.getSession().setAttribute("user", updatedUser);
            }
        } else if (action.equals("patient")) {
            String patientIdStr = request.getParameter("patientId");
            String dateOfBirth = request.getParameter("dob");
            String phoneNumber = request.getParameter("phone");
            String address = request.getParameter("address");
            String emergencyContactName = request.getParameter("emergencyContact");
            String emergencyContactPhoneNumber = request.getParameter("emergencyContactPhone");

            if (patientIdStr == null || patientIdStr.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "patientId is required", 400);
                return;
            }

            int patientId;
            try {
                patientId = Integer.parseInt(patientIdStr);
            } catch (NumberFormatException e) {
                JsonResponseUtil.sendErrorResponse(response, "patientId must be a valid number", 400);
                return;
            }

            if (dateOfBirth == null || dateOfBirth.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "dob is required", 400);
                return;
            }

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "phone is required", 400);
                return;
            }

            if (address == null || address.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "address is required", 400);
                return;
            }

            if (emergencyContactName == null || emergencyContactName.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "emergencyContact is required", 400);
                return;
            }

            String[] parts = emergencyContactName.split(" ");
            if (parts.length < 2) {
                JsonResponseUtil.sendErrorResponse(response,
                        "emergencyContact must contain at least first and last name", 400);
                return;
            }

            if (emergencyContactPhoneNumber == null || emergencyContactPhoneNumber.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "emergencyContactPhone is required", 400);
                return;
            }

            System.out.println("patientId = " + patientIdStr);
            System.out.println("dateOfBirth = " + dateOfBirth);
            System.out.println("phoneNumber = " + phoneNumber);
            System.out.println("address = " + address);
            System.out.println("emergencyContactName = " + emergencyContactName);
            System.out.println("emergencyContactPhoneNumber = " + emergencyContactPhoneNumber);

            boolean success = PatientService.updatePatient(
                    patientId, dateOfBirth, phoneNumber, address,
                    emergencyContactName, emergencyContactPhoneNumber);

            JsonResponseUtil.sendJsonResponse(response,
                    success ? JsonResponseUtil.createSuccessResponse("Patient profile updated successfully")
                            : JsonResponseUtil.createErrorResponse("Failed to update patient profile"));
        } else if (action.equals("doctor")) {
            String doctorIdStr = request.getParameter("doctorId");
            String department = request.getParameter("department");

            if (doctorIdStr == null || doctorIdStr.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "doctorId is required", 400);
                return;
            }

            int doctorId;
            try {
                doctorId = Integer.parseInt(doctorIdStr);
            } catch (NumberFormatException e) {
                JsonResponseUtil.sendErrorResponse(response, "doctorId must be a valid number", 400);
                return;
            }

            if (department == null || department.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "department is required", 400);
                return;
            }

            System.out.println("doctorId = " + doctorIdStr);
            System.out.println("department = " + department);

            boolean success = DoctorService.updateDoctor(doctorId, department);
            JsonResponseUtil.sendJsonResponse(response,
                    success ? JsonResponseUtil.createSuccessResponse("Doctor profile updated successfully")
                            : JsonResponseUtil.createErrorResponse("Failed to update doctor profile"));
        }

        doGet(request, response);
    }
}