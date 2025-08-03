package com.group_3.healthlink.servlets.admin;

import com.group_3.healthlink.SystemLog;
import com.group_3.healthlink.User;
import com.group_3.healthlink.util.JsonResponseUtil;
import com.group_3.healthlink.services.SystemLogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.List;

@WebServlet(name = "systemLogServlet", urlPatterns = { "/admin/system-log" })
public class SystemLogServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");

    if (user == null || !user.isAdmin()) {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    List<SystemLog> systemLogs = SystemLogService.getAll();
    request.setAttribute("systemLogs", systemLogs);

    request.getRequestDispatcher("/admin/system-log.jsp").forward(request, response);
  }
}
