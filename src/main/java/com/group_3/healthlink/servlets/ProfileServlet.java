package com.group_3.healthlink.servlets;

import com.group_3.healthlink.User;
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
            String userId = request.getParameter("userId");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (userId == null || userId.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "userId is required", 400);
                return;
            }

            if (fullName == null || fullName.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "fullName is required", 400);
                return;
            }

            if (email == null || email.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "email is required", 400);
                return;
            }

            System.out.println("userId = " + userId);
            System.out.println("fullName = " + fullName);
            System.out.println("email = " + email);
        } else if (action.equals("patient")) {
            String patientId = request.getParameter("patientId");
            String dateOfBirth = request.getParameter("dob");
            String phoneNumber = request.getParameter("phone");
            String emergencyContactName = request.getParameter("emergencyContact");
            String address = request.getParameter("address");

            if (dateOfBirth == null || dateOfBirth.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "dob is required", 400);
                return;
            }

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "phone is required", 400);
                return;
            }

            if (emergencyContactName == null || emergencyContactName.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "emergencyContact is required", 400);
                return;
            }

            if (address == null || address.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "address is required", 400);
                return;
            }

            System.out.println("patientId = " + patientId);
            System.out.println("dateOfBirth = " + dateOfBirth);
            System.out.println("phoneNumber = " + phoneNumber);
            System.out.println("emergencyContactName = " + emergencyContactName);
            System.out.println("address = " + address);
        } else if (action.equals("doctor")) {
            String doctorId = request.getParameter("doctorId");
            String department = request.getParameter("department");

            if (doctorId == null || doctorId.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "doctorId is required", 400);
                return;
            }

            if (department == null || department.isEmpty()) {
                JsonResponseUtil.sendErrorResponse(response, "department is required", 400);
                return;
            }

            System.out.println("doctorId = " + doctorId);
            System.out.println("department = " + department);
        }

        doGet(request, response);
    }
}