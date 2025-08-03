<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.services.SystemLogService" %>
<%@ page import="com.group_3.healthlink.SystemLog" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
  <head>
    <title>System Log - Healthlink</title>
    <link href="../css/styles.css" rel="stylesheet" />
  </head>
  <body>

  <div class="app-container">
      <% User user = (User) session.getAttribute("user"); %>
      <% if (user == null || !user.isAdmin()) { %>
        <script>
          window.location.href = '<%= request.getContextPath() %>/login';
        </script>
      <% } %>

      <jsp:include page="../layouts/sidebar.jsp" />

      <div class="main-content">
        <h2 class="fw-semibold h2">System Log</h2>
        <form method="get" class="mb-3 d-flex align-items-center" style="gap: 0.5rem;">
          <label for="userId" class="form-label mb-0">Filter by User ID:</label>
          <input type="text" id="userId" name="userId" class="form-control form-control-sm" style="max-width: 150px;" value="<%= request.getParameter("userId") != null ? request.getParameter("userId") : "" %>" />
          <button type="submit" class="btn btn-primary btn-sm">Filter</button>
          <% if (request.getAttribute("selectedUserId") != null) { %>
            <a href="system-log" class="btn btn-secondary btn-sm">Clear</a>
          <% } %>
        </form>

        <table class="table table-striped">
          <thead>
            <tr>
              <th>Log ID</th>
              <th>User ID</th>
              <th>Action</th>
              <th>Detail</th>
              <th>Timestamp</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <% List<SystemLog> systemLogs = (List<SystemLog>) request.getAttribute("systemLogs"); %>
            <% if (systemLogs != null && !systemLogs.isEmpty()) {
              for (SystemLog log : systemLogs) { %>
                <tr>
                  <td>
                    <%= log.getLogId() %>
                  </td>
                  <td>
                    <%= log.getUserId() %>
                  </td>
                  <td>
                    <%= log.getAction() %>
                  </td>
                  <td>
                    <%= log.getDetail() %>
                  </td>
                  <td>
                    <% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); %>
                    <%= sdf.format(log.getTimestamp()) %>
                  </td>
                  <td>
                    <form action="<%= request.getContextPath() %>/profile" method="get" style="display:inline;">
                      <input type="hidden" name="userId" value="<%= log.getUserId() %>" />
                      <button type="submit" class="btn btn-sm btn-outline-primary">View Profile</button>
                    </form>
                  </td>
                </tr>
              <% } %>
            <% } else { %>
              <tr>
                <td colspan="6" class="text-center">No logs available.</td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    </div>
    <script src="../js/bootstrap.min.js"></script>
    </body>
</html>
