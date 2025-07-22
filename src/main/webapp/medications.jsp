<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.group_3.healthlink.Medication" %>
<html>
<head>
  <title>Healthlink - Medications</title>
  <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
<div class="app-container">
    <jsp:include page="layouts/sidebar.jsp" />

    <div class="main-content">
      <h2>Your Medications</h2>
      <%
        List<Medication> medications = (List<Medication>) request.getAttribute("medications");
        if (medications != null && !medications.isEmpty()) {
      %>
        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Dosage</th>
              <th>Frequency</th>
              <th>Notes</th>
              <th>Prescribed By</th>
            </tr>
          </thead>
          <tbody>
            <% Map<Integer, String> doctorNames = (Map<Integer, String>) request.getAttribute("doctorNames"); %>
            <% for (Medication med : medications) { %>
            <tr>
              <td><%= med.getName() %></td>
              <td><%= med.getDosage() %></td>
              <td><%= med.getFrequency() %></td>
              <td><%= med.getNotes() != null ? med.getNotes() : "" %></td>
              <td><%= doctorNames != null ? doctorNames.get(med.getDoctorId()) : "Unknown" %></td>
            </tr>
            <% } %>
          </tbody>
        </table>
      <% } else { %>
        <p>No medications found.</p>
      <% } %>
    </div>
  </div>
  <script src="js/bootstrap.min.js"></script>
</body>
</html>
