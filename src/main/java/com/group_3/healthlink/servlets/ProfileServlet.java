package com.group_3.healthlink.servlets;

import com.group_3.healthlink.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.json.JSONObject;

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
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            sendErrorResponse(response, "User not authenticated");
            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            sendErrorResponse(response, "action parameter is required");
            return;
        }

        if (!action.equals("personal") &&
            !action.equals("patient") &&
            !action.equals("doctor")
        ) {
            sendErrorResponse(response, "Invalid action parameter");
            return;
        }

        System.out.println("action = " + action);

        if (action.equals("personal")) {
            String userId = request.getParameter("userId");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (userId == null || userId.isEmpty()) {
                sendErrorResponse(response, "userId is required");
                return;
            }

            if (fullName == null || fullName.isEmpty()) {
                sendErrorResponse(response, "fullName is required");
                return;
            }

            if (email == null || email.isEmpty()) {
                sendErrorResponse(response, "email is required");
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
                sendErrorResponse(response, "dob is required");
                return;
            }

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                sendErrorResponse(response, "phone is required");
                return;
            }

            if (emergencyContactName == null || emergencyContactName.isEmpty()) {
                sendErrorResponse(response, "emergencyContact is required");
                return;
            }

            if (address == null || address.isEmpty()) {
                sendErrorResponse(response, "address is required");
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
                sendErrorResponse(response, "doctorId is required");
                return;
            }

            if (department == null || department.isEmpty()) {
                sendErrorResponse(response, "department is required");
                return;
            }

            System.out.println("doctorId = " + doctorId);
            System.out.println("department = " + department);
        }

        doGet(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(400);
        JSONObject json = new JSONObject();
        json.put("error", message);
        response.getWriter().write(json.toString());
    }
} 