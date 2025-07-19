package com.group_3.healthlink;

import java.io.IOException;

import com.group_3.healthlink.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "loginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
  @Override
  public void init() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Could not load JDBC driver. Is it in the classpath?");
      e.printStackTrace();
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    User user = (User) request.getSession().getAttribute("user");
    if (user != null) {
      response.sendRedirect(request.getContextPath() + "/dashboard");
      return;
    }

    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    User user = AuthService.getUserByEmail(email);
    if (user != null) {
      String userId = String.valueOf(user.getId());
      System.out.println("User ID found in database: " + userId);

      String hashedPassword = user.getPasswordHashed();

      if (AuthService.verifyPassword(password, hashedPassword)) {
        System.out.println("Password matches for user ID: " + userId);

        request.getSession().setAttribute("user", user);

        jakarta.servlet.http.Cookie userCookie = new jakarta.servlet.http.Cookie("userId", userId);
        userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        userCookie.setPath(request.getContextPath());
        response.addCookie(userCookie);

        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
      }
    }

    System.out.println("Invalid credentials for email: " + email);
    request.setAttribute("loginError", "Invalid credentials.");
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }
}