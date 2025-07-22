<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Patient" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
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

        <%
          java.util.List<Patient> filteredPatients = null;
          if (patientIdStr == null) { %>
            <div class="main-content">
              <div class="d-flex">
                <div class="d-flex border-bottom gap-4 mb-2">
                  <h2 class="h2 fw-semibold">My Patients</h2>
                </div>
              </div>

              <% String filter = request.getParameter("filter"); %>
              <% String query = request.getParameter("query"); %>
              <% Patient[] allPatients = DoctorService.getPatients(user.getUserId()); %>
              <% filteredPatients = new java.util.ArrayList<>(); %>

              <form class="flex w-100 mb-3" style="max-width: 50%" id="searchForm" method="get">
                <div class="input-group">
                  <select class="form-select" id="searchFilter" name="filter" style="max-width: 120px; min-width: 90px;">
                    <option value="name" <%="name".equals(filter) ? "selected" : ""%>>Name</option>
                    <option value="id" <%="id".equals(filter) ? "selected" : ""%>>ID</option>
                  </select>
                  <input type="text" class="form-control flex-grow-1"
                         id="searchQuery" name="query"
                         placeholder="Enter patient name or ID"
                         value="<%=query != null ? query : ""%>">
                  <button class="btn btn-primary" type="submit">Search</button>
                </div>
              </form>

              <% if (query == null || query.trim().isEmpty()) {
                  for (Patient p : allPatients) filteredPatients.add(p);
              } else if (filter != null && filter.equals("id")) {
                  try {
                      int idQuery = Integer.parseInt(query.trim());
                      for (Patient p : allPatients) {
                          if (p.getUserId() == idQuery) filteredPatients.add(p);
                      }
                  } catch (NumberFormatException e) {
                  }
              } else {
                  String q = query.trim().toLowerCase();
                  for (Patient p : allPatients) {
                      if (p.getFullName() != null &&
                              p.getFullName().toLowerCase().contains(q)
                      ) {
                          filteredPatients.add(p);
                      }
                  }
              }
              %>
                <% if (filteredPatients.isEmpty()) { %>
                <div>No patients found.</div>
              <% } else { %>
                <table class="table table-striped">
                  <thead>
                    <tr><th>ID</th><th>Name</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    <% for (Patient p : filteredPatients) { %>
                      <tr>
                        <td><%=p.getUserId()%></td>
                        <td><%=p.getFullName()%></td>
                        <td>
                          <a href="${pageContext.request.contextPath}/patients/<%=p.getUserId()%>"
                             class="btn btn-primary btn-sm">View</a>
                        </td>
                      </tr>
                    <% } %>
                  </tbody>
                </table>
              <% } %>
            </div>
        <% } else { %>
          <%
            Integer patientId = null;
            try {
              patientId = Integer.parseInt(patientIdStr);
            } catch (NumberFormatException e) {};

            if (patientId == null || request.getAttribute("invalidPatientId") != null) { %>
              <div class="main-content">
                <div class="alert alert-danger">
                  <h4>Error</h4>
                  <p>Invalid patient ID provided.</p>
                  <a href="${pageContext.request.contextPath}/patients" class="btn btn-primary">Back to Patients</a>
                </div>
              </div>
            <% } else if (request.getAttribute("patientNotFound") != null) { %>
              <div class="main-content">
                <div class="alert alert-warning">
                  <h4>Patient Not Found</h4>
                  <p>No patient found with ID: <%= patientId %></p>
                  <a href="${pageContext.request.contextPath}/patients" class="btn btn-primary">Back to Patients</a>
                </div>
              </div>
            <% } else {
                Patient patient = (Patient) request.getAttribute("patient");
                Doctor[] assignedDoctors = (Doctor[]) request.getAttribute("assignedDoctors");
                if (patient != null) { %>
              <div class="main-content">
                <div class="d-flex justify-content-between align-items-center border-bottom mb-4 pb-2">
                  <h2 class="h2 fw-semibold mb-0">Patient Details</h2>
                  <a href="${pageContext.request.contextPath}/patients" class="btn btn-secondary">Back to Patients</a>
                </div>

                <div class="row">
                  <div class="col-md-8">
                    <div class="card mb-4">
                      <div class="card-header">
                        <h5 class="card-title mb-0">Personal Information</h5>
                      </div>
                      <div class="card-body">
                        <div class="row">
                          <div class="col-md-6">
                            <p><strong>Patient ID:</strong> <%= patient.getPatientId() %></p>
                            <p><strong>User ID:</strong> <%= patient.getUserId() %></p>
                            <p><strong>Full Name:</strong> <%= patient.getFullName() != null ? patient.getFullName() : "N/A" %></p>
                            <p><strong>Email:</strong> <%= patient.getEmailAddress() != null ? patient.getEmailAddress() : "N/A" %></p>
                            <p><strong>Date of Birth:</strong> <%= patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "N/A" %></p>
                          </div>
                          <div class="col-md-6">
                            <p><strong>Phone Number:</strong> <%= patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "N/A" %></p>
                            <p><strong>Address:</strong> <%= patient.getAddress() != null ? patient.getAddress() : "N/A" %></p>
                            <p><strong>Member Since:</strong> <%= patient.getCreatedAt() != null ? patient.getCreatedAt() : "N/A" %></p>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="card mb-4">
                      <div class="card-header">
                        <h5 class="card-title mb-0">Emergency Contact</h5>
                      </div>
                      <div class="card-body">
                        <div class="row">
                          <div class="col-md-6">
                            <p><strong>Name:</strong> <%= patient.getEmergencyContactName() != null ? patient.getEmergencyContactName() : "N/A" %></p>
                          </div>
                          <div class="col-md-6">
                            <p><strong>Phone:</strong> <%= patient.getEmergencyContactPhoneNumber() != null ? patient.getEmergencyContactPhoneNumber() : "N/A" %></p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="card">
                      <div class="card-header">
                        <h5 class="card-title mb-0">Assigned Doctors</h5>
                      </div>
                      <div class="card-body">
                        <% if (assignedDoctors != null && assignedDoctors.length > 0) { %>
                          <% for (Doctor doctor : assignedDoctors) { %>
                            <div class="border-bottom pb-2 mb-2">
                              <p class="mb-1"><strong><%= doctor.getFullName() != null ? doctor.getFullName() : "Unknown Doctor" %></strong></p>
                              <p class="text-muted small mb-0">Specialization: <%= doctor.getDepartment() != null ? doctor.getDepartment() : "N/A" %></p>
                            </div>
                          <% } %>
                        <% } else { %>
                          <p class="text-muted">No doctors assigned to this patient.</p>
                        <% } %>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            <% } else { %>
              <div class="main-content">
                <div class="alert alert-danger">
                  <h4>Error</h4>
                  <p>Unable to load patient details.</p>
                  <a href="${pageContext.request.contextPath}/patients" class="btn btn-primary">Back to Patients</a>
                </div>
              </div>
            <% } } %>
        <% } %>
    </div>
  <% } %>
  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </body>
</html>
