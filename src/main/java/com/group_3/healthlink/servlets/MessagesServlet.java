package com.group_3.healthlink.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "messagesServlet", urlPatterns = { "/messages", "/messages/*" })
public class MessagesServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            // Extract user ID from URL like /messages/123
            String userIdStr = pathInfo.substring(1); // Remove the leading "/"
            try {
                int userId = Integer.parseInt(userIdStr);
                request.setAttribute("selectedUserId", userId);
            } catch (NumberFormatException e) {
                // Invalid user ID, ignore and show default messages page
            }
        }

        request.getRequestDispatcher("/messages.jsp").forward(request, response);
    }
}