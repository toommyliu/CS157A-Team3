package com.group_3.healthlink.servlets;

import com.group_3.healthlink.Notification;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.AuthService;
import com.group_3.healthlink.services.NotificationService;
import com.group_3.healthlink.util.JsonResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "notificationServlet", urlPatterns = { "/notifications" })
public class NotificationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User user = AuthService.ensureAuthenticated(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get all notifications for the current user
        List<Notification> notifications = NotificationService.getNotificationsByReceiverId(user.getUserId());
        
        request.setAttribute("notifications", notifications);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User user = AuthService.ensureAuthenticated(request);
        if (user == null) {
            JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("markAsRead".equals(action)) {
            handleMarkAsRead(request, response, user);
        } else if ("markAllAsRead".equals(action)) {
            handleMarkAllAsRead(request, response, user);
        } else if ("delete".equals(action)) {
            handleDeleteNotification(request, response, user);
        } else {
            JsonResponseUtil.sendErrorResponse(response, "Invalid action", 400);
        }
    }
    
    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        
        try {
            int notificationId = Integer.parseInt(request.getParameter("notificationId"));
            
            // Verify the notification belongs to the current user
            Notification notification = NotificationService.getNotificationById(notificationId);
            if (notification == null || notification.getReceiverId() != user.getUserId()) {
                JsonResponseUtil.sendErrorResponse(response, "Notification not found or access denied", 404);
                return;
            }
            
            boolean success = NotificationService.markAsRead(notificationId);
            
            if (success) {
                JsonResponseUtil.sendJsonResponse(response, JsonResponseUtil.createSuccessResponse("Notification marked as read"));
            } else {
                JsonResponseUtil.sendErrorResponse(response, "Failed to mark notification as read", 500);
            }
            
        } catch (NumberFormatException e) {
            JsonResponseUtil.sendErrorResponse(response, "Invalid notification ID", 400);
        }
    }
    
    private void handleMarkAllAsRead(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        
        boolean success = NotificationService.markAllAsRead(user.getUserId());
        
        if (success) {
            JsonResponseUtil.sendJsonResponse(response, JsonResponseUtil.createSuccessResponse("All notifications marked as read"));
        } else {
            JsonResponseUtil.sendErrorResponse(response, "Failed to mark notifications as read", 500);
        }
    }
    
    private void handleDeleteNotification(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        
        try {
            int notificationId = Integer.parseInt(request.getParameter("notificationId"));
            
            // Verify the notification belongs to the current user
            Notification notification = NotificationService.getNotificationById(notificationId);
            if (notification == null || notification.getReceiverId() != user.getUserId()) {
                JsonResponseUtil.sendErrorResponse(response, "Notification not found or access denied", 404);
                return;
            }
            
            boolean success = NotificationService.deleteNotification(notificationId);
            
            if (success) {
                JsonResponseUtil.sendJsonResponse(response, JsonResponseUtil.createSuccessResponse("Notification deleted"));
            } else {
                JsonResponseUtil.sendErrorResponse(response, "Failed to delete notification", 500);
            }
            
        } catch (NumberFormatException e) {
            JsonResponseUtil.sendErrorResponse(response, "Invalid notification ID", 400);
        }
    }
} 