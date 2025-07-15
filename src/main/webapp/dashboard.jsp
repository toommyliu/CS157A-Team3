<%@ page import="com.group_3.healthlink.User" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" />
  </head>
  <body>
    <% if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    } %>
    <%User user = (User) session.getAttribute("user"); %>
    <p>hello from dashboard!</p>
    <p>it seems you are logged in.</p>
    <h3>User Details:</h3>
    <ul>
      <li>ID: <%= user.getId() %></li>
      <li>Email: <%= user.getEmailAddress() %></li>
      <li>First Name: <%= user.getFirstName() %></li>
      <li>Last Name: <%= user.getLastName() %></li>
      <li>Role: <%= user.getRole() %></li>
      <li>Created At: <%= user.getCreatedAt() %></li>
      <li>Updated At: <%= user.getUpdatedAt() %></li>
    </ul>
  </body>
</html>
