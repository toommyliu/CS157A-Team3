<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<html>
<head>
  <title>Healthlink - Patients</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>
<body>
  <% User user = (User)session.getAttribute("user"); %>
  <% if (user.isPatient()) { %>
    <script>
      window.location.href = '<%= request.getContextPath() %>/dashboard';
    </script>
  <% } else if (user.isDoctor()) { %>
    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <% String patientIdStr = (String) request.getAttribute("patientId"); %>

      <% if (patientIdStr == null) { %>
        <div class="main-content">
          <p>hello from all patients</p>
        </div>
        <% } else { %>
          <%
            Integer patientId = null;
            try {
              patientId = Integer.parseInt(patientIdStr);
            } catch (NumberFormatException e) {}
            if (patientId == null) { %>
              <div class="main-content">
                <p>Invalid patientId</p>
              </div>
            <% } else { %>
              <div class="main-content">
                <p>hello from patients</p>
                <p>patient id is = <%= patientIdStr %></p>
              </div>
          <% } %>
        <% } %>
  </div>
  <% } %>
  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>
