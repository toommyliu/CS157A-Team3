package com.group_3.healthlink;

import java.io.IOException;
import java.io.PrintWriter;

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
    String userId = null;
    if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("userId")) {
          userId = cookie.getValue();
          break;
        }
      }
    }

    if (userId != null) {
      try {
        int id = Integer.parseInt(userId);
        User user = AuthService.getUserById(id);
        if (user != null) {
          request.getSession().setAttribute("user", user);
          response.sendRedirect(request.getContextPath() + "/dashboard");
          return;
        }
      } catch (NumberFormatException e) {
        // TODO:
      }
    }

    request.getRequestDispatcher("/login.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String userId = null;
    boolean foundUserIdCookie = false;

    if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("userId")) {
          userId = cookie.getValue();
          foundUserIdCookie = true;
          break;
        }
      }
    }

    if (foundUserIdCookie && userId != null) {
      System.out.println("User ID found in cookies: " + userId);

      User user = null;
      try {
        int id = Integer.parseInt(userId);
        user = AuthService.getUserById(id);
      } catch (NumberFormatException e) {
        // TODO:
      }

      if (user != null) {
        request.getSession().setAttribute("user", user);
        response.addCookie(new jakarta.servlet.http.Cookie("userId", userId));
        response.sendRedirect(request.getContextPath() + "/dashboard");
      } else {
        // TODO:
      }
    } else {
      String email = request.getParameter("email");
      String password = request.getParameter("password");

      User user = AuthService.getUserByEmail(request.getParameter("email"));
      if (user != null) {
        userId = String.valueOf(user.getId());
        System.out.println("User ID found in database: " + userId);

        String hashedPassword = user.getPasswordHashed();

        if (AuthService.verifyPassword(password, hashedPassword)) {
          System.out.println("Password matches for user ID: " + userId);

          // PrintWriter out = response.getWriter();
          // out.println("<html><body>");
          // out.println("<h1>Login Successful!</h1>");
          // out.println("<p>Email: " + email + "</p>");
          // out.println("<p>Password: " + password + "</p>");
          // out.println("<p>Hashed Password: " + AuthService.hashPassword(password) +
          // "</p>");
          // out.println("<p>Does user exist? " + AuthService.doesUserExist(email) +
          // "</p>");
          // out.println("<table border=\"1\">");
          // out.println("<tr><td>SJSU ID</td><td>Name</td><td>Major</td></tr>");
          //
          // String dbName = "Tommy_Liu";
          // String dbUser = "root";
          // String dbPassword = "password";
          //
          // try {
          // java.sql.Connection con = DriverManager.getConnection(
          // String.format("jdbc:mysql://localhost:3306/%s?autoReconnect=true&useSSL=false",
          // dbName), dbUser,
          // dbPassword);
          // out.println("Initial entries in table \"Student\": <br/>");
          // Statement stmt = con.createStatement();
          // ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
          // while (rs.next()) {
          // out.println("<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" +
          // rs.getString(2) + "</td>" + "<td>"
          // + rs.getString(3) + "</td>" + "</tr>");
          // }
          // rs.close();
          // stmt.close();
          // con.close();
          // out.println("</table>");
          // out.println("</body></html>");
          // } catch (SQLException e) {
          // out.println("SQLException caught: " + e.getMessage());
          // } catch (Exception e) {
          // out.println("Exception caught: " + e.getMessage());
          // }

          request.getSession().setAttribute("user", user);
          response.addCookie(new jakarta.servlet.http.Cookie("userId", userId));
          response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
          System.out.println("Password does not match for user ID: " + userId);

          PrintWriter out = response.getWriter();
          out.println("<html><body>");
          out.println("<h1>Login Failed!</h1>");
          out.println("</body></html>");
        }
      } else {
        System.out.println("User not found in database for email: " + request.getParameter("email"));

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Login Failed!</h1>");
        out.println("<p>User not found.</p>");
        out.println("</body></html>");
      }
    }
  }
}