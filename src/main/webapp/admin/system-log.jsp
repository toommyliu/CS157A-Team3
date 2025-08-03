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
        <% List<SystemLog> logs = (List<SystemLog>) request.getAttribute("systemLogs"); %>
        <h2 class="fw-semibold h2">System Log</h2>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Log ID</th>
              <th>User ID</th>
              <th>Action</th>
              <th>Detail</th>
              <th>Timestamp</th>
            </tr>
          </thead>
          <tbody>
            <% if (logs != null && !logs.isEmpty()) { %>
              <% for (SystemLog log : logs) { %>
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
                </tr>
              <% } %>
            <% } else { %>
              <tr>
                <td colspan="5" class="text-center">No logs available.</td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    </div>
    <script src="../js/bootstrap.min.js"></script>
    </body>
</html>
