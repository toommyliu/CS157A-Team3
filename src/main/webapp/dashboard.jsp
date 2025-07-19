<%@ page import="com.group_3.healthlink.User" %><%@ page
import="com.group_3.healthlink.services.DoctorService" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Healthlink - Dashboard</title>
    <link href="css/styles.css" rel="stylesheet" />
  </head>
  <body>
    <% User user = (User)session.getAttribute("user"); %>

    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
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

        <% if ("doctor".equalsIgnoreCase(user.getRole())) { %>
        <div class="mt-4">
          <h3>Your Patients:</h3>
          <% User[] patients = DoctorService.getPatients(user.getId()); if
          (patients.length > 0) { %>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Patient ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Created At</th>
                </tr>
              </thead>
              <tbody>
                <% for (User patient : patients) { %>
                <tr>
                  <td><%= patient.getId() %></td>
                  <td><%= patient.getFullName() %></td>
                  <td><%= patient.getEmailAddress() %></td>
                  <td><%= patient.getCreatedAt() %></td>
                </tr>
                <% } %>
              </tbody>
            </table>
          </div>
          <% } else { %>
          <p class="text-muted">No patients assigned yet.</p>
          <% } %>
        </div>
        <% } %>

        <div class="mt-4">
          <a
            href="<%= request.getContextPath() %>/logout"
            class="btn btn-danger"
            >Logout</a
          >
        </div>
      </div>
    </div>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
