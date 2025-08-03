package com.group_3.healthlink.servlets.admin;

import com.group_3.healthlink.User;
import com.group_3.healthlink.util.JsonResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "systemLogServlet", urlPatterns = { "/admin/system-log" })
public class SystemLogServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");

    if (user == null || !user.isAdmin()) {
      JsonResponseUtil.sendErrorResponse(response, "Unauthorized", 401);
      return;
    }

    request.getRequestDispatcher("/admin/system-log.jsp").forward(request, response);
  }
}
