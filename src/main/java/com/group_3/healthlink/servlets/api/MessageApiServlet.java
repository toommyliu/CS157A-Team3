package com.group_3.healthlink.servlets.api;

import com.group_3.healthlink.Message;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.MessageService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;

@WebServlet(name = "messageApiServlet", urlPatterns = { "/messages/api/*" })
public class MessageApiServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.equals("/threads")) {
                // GET /messages/api/threads - Get chat threads
                handleGetThreads(request, response, currentUser);
            } else if (pathInfo.startsWith("/history/")) {
                // GET /messages/api/history/{userId} - Get chat history
                String userIdStr = pathInfo.substring(9); // Remove "/history/"
                int otherUserId = Integer.parseInt(userIdStr);
                handleGetHistory(request, response, currentUser, otherUserId);
            } else if (pathInfo.equals("/unread-count")) {
                // GET /messages/api/unread-count - Get total unread count
                handleGetUnreadCount(request, response, currentUser);
            } else if (pathInfo.equals("/unread-conversations")) {
                // GET /messages/api/unread-conversations - Get unread conversation count
                handleGetUnreadConversations(request, response, currentUser);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Invalid user ID");
            response.getWriter().write(gson.toJson(error));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Internal server error");
            response.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.equals("/send")) {
                // POST /messages/api/send - Send a message
                handleSendMessage(request, response, currentUser);
            } else if (pathInfo.startsWith("/mark-read/")) {
                // POST /messages/api/mark-read/{userId} - Mark messages as read
                String userIdStr = pathInfo.substring(11); // Remove "/mark-read/"
                int otherUserId = Integer.parseInt(userIdStr);
                handleMarkAsRead(request, response, currentUser, otherUserId);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Internal server error");
            response.getWriter().write(gson.toJson(error));
        }
    }

    private void handleGetThreads(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        List<User> threads = MessageService.getChatThreads(currentUser.getUserId(), currentUser.getRole());

        // Add latest message preview to each thread
        for (User thread : threads) {
            Message latestMessage = MessageService.getLatestMessage(currentUser.getUserId(), thread.getUserId());
            if (latestMessage != null) {
                // Note: We can't modify the User object directly, so we'll handle this in the
                // frontend
            }
        }

        response.getWriter().write(gson.toJson(threads));
    }

    private void handleGetHistory(HttpServletRequest request, HttpServletResponse response,
            User currentUser, int otherUserId) throws IOException {

        List<Message> messages = MessageService.getChatHistory(currentUser.getUserId(), otherUserId);
        response.getWriter().write(gson.toJson(messages));
    }

    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        // Read JSON from request body
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JsonObject requestJson = gson.fromJson(sb.toString(), JsonObject.class);
        int receiverId = requestJson.get("receiverId").getAsInt();
        String content = requestJson.get("content").getAsString();

        if (content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Message content cannot be empty");
            response.getWriter().write(gson.toJson(error));
            return;
        }

        boolean success = MessageService.sendMessage(currentUser.getUserId(), receiverId, content);

        JsonObject result = new JsonObject();
        if (success) {
            result.addProperty("success", true);
            result.addProperty("message", "Message sent successfully");
        } else {
            result.addProperty("success", false);
            result.addProperty("message", "Failed to send message");
        }

        response.getWriter().write(gson.toJson(result));
    }

    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response,
            User currentUser, int otherUserId) throws IOException {

        boolean success = MessageService.markMessagesAsRead(currentUser.getUserId(), otherUserId);

        JsonObject result = new JsonObject();
        if (success) {
            result.addProperty("success", true);
            result.addProperty("message", "Messages marked as read");
        } else {
            result.addProperty("success", false);
            result.addProperty("message", "Failed to mark messages as read");
        }

        response.getWriter().write(gson.toJson(result));
    }

    private void handleGetUnreadCount(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        int unreadCount = MessageService.getUnreadCount(currentUser.getUserId());

        JsonObject result = new JsonObject();
        result.addProperty("unreadCount", unreadCount);

        response.getWriter().write(gson.toJson(result));
    }

    private void handleGetUnreadConversations(HttpServletRequest request, HttpServletResponse response,
            User currentUser)
            throws IOException {

        int unreadConversations = MessageService.getUnreadConversationCount(currentUser.getUserId());

        JsonObject result = new JsonObject();
        result.addProperty("unreadConversations", unreadConversations);

        response.getWriter().write(gson.toJson(result));
    }
}