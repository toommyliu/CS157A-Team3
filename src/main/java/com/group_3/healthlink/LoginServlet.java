package com.group_3.healthlink;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "loginServlet", value = "/login-servlet")
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
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>Login Successful!</h1>");
    out.println("<p>Email: " + email + "</p>");
    out.println("<p>Password: " + password + "</p>");
    out.println("<table border=\"1\">");
    out.println("<tr><td>SJSU ID</td><td>Name</td><td>Major</td></tr>");

    String dbName = "Liu";
    String dbUser = "root";
    String dbPassword = "password";

    try {
      java.sql.Connection con = DriverManager.getConnection(
          String.format("jdbc:mysql://localhost:3306/%s?autoReconnect=true&useSSL=false", dbName), dbUser, dbPassword);
      out.println("Initial entries in table \"Student\": <br/>");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
      while (rs.next()) {
        out.println("<tr>" + "<td>" + rs.getInt(1) + "</td>" + "<td>" + rs.getString(2) + "</td>" + "<td>"
            + rs.getString(3) + "</td>" + "</tr>");
      }
      rs.close();
      stmt.close();
      con.close();
      out.println("</table>");
      out.println("</body></html>");
    } catch (SQLException e) {
      out.println("SQLException caught: " + e.getMessage());
    } catch (Exception e) {
      out.println("Exception caught: " + e.getMessage());
    }
  }

  // public void destroy() {
  // }
}