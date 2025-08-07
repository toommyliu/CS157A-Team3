package com.group_3.healthlink.servlets.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.group_3.healthlink.User;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.services.DoctorService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "doctorsApiServlet", urlPatterns = { "/api/doctors" })
public class DoctorsApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendErrorResponse(response, "Unauthorized", 401);
            return;
        }

        User user = (User) session.getAttribute("user");

        // Only patients should access this API
        if (!user.isPatient()) {
            sendErrorResponse(response, "Forbidden", 403);
            return;
        }

        String department = request.getParameter("department");
        if (department == null || department.trim().isEmpty()) {
            sendErrorResponse(response, "Department parameter is required", 400);
            return;
        }

        try {
            Doctor[] doctors = DoctorService.getByDepartment(department);

            JsonObject result = new JsonObject();
            result.addProperty("success", true);
            result.add("doctors", gson.toJsonTree(doctors));

            sendJsonResponse(response, result);

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