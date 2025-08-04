package com.group_3.healthlink.servlets;

import java.io.IOException;

import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.services.SystemLogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "logoutServlet", urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    handleLogout(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    handleLogout(request, response);
  }

  private void handleLogout(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    User user = (User) request.getSession().getAttribute("user");
    SystemLogService.createNew(user.getUserId(), SystemLogAction.LOGOUT, null);

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    Cookie userCookie = new Cookie("userId", "");
    userCookie.setMaxAge(0);
    userCookie.setPath(request.getContextPath());
    response.addCookie(userCookie);
    response.sendRedirect(request.getContextPath() + "/");
  }
}
