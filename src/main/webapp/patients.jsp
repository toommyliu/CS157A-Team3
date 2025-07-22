<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<html>
<head>
  <title>Healthlink - Patients</title>
  <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
  <% User user = (User)session.getAttribute("user"); %>
  <% if (user.isPatient()) { %>}
    <script>
      window.location.href = '<%= request.getContextPath() %>/dashboard';
    </script>
  <% } else if (user.isDoctor()) { %>
    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <p>hello from patients</p>
      </div>
  </div>
  <% } %>
  <script src="js/bootstrap.min.js"></script>
  </body>
</html>
