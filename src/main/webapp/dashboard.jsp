<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="com.group_3.healthlink.services.AuthService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Healthlink - Dashboard</title>
    <link href="css/styles.css" rel="stylesheet" />
  </head>
  <body>
    <%
      User user = (User) session.getAttribute("user");
      User[] patients = null;
      if (user != null && user.isDoctor()) {
        com.group_3.healthlink.Doctor doctor = DoctorService.getByUserId(user.getUserId());
        if (doctor != null) {
          patients = DoctorService.getPatients(doctor.getDoctorId());
        }
      }
    %>

    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <div class="d-flex">
          <div class="d-flex border-bottom gap-4 mb-4">
            <h2 class="h2 fw-semibold">Dashboard</h2>
          </div>
        </div>
        
        <div class="alert alert-info" role="alert">
          <h5 class="alert-heading">Welcome back, <%= user.getFirstName() %>!</h5>
          <p class="mb-0">This is your personalized dashboard. Use the sidebar to navigate to different sections.</p>
        </div>

        <% if (user.isDoctor()) { %>
        <div class="mt-4">
          <h3>Doctor Dashboard</h3>
          <p>Welcome, Dr. <%= user.getFirstName() %> <%= user.getLastName() %>!</p>
          <div class="row g-3">
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">Manage Patients</h5>
                  <p class="card-text">View and manage your assigned patients</p>
                  <a href="<%= request.getContextPath() %>/patients" class="btn btn-primary">View Patients</a>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">Add Medications</h5>
                  <p class="card-text">Prescribe medications for your patients</p>
                  <a href="<%= request.getContextPath() %>/medications" class="btn btn-primary">Manage Medications</a>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">View Notes</h5>
                  <p class="card-text">Read and write patient notes</p>
                  <a href="<%= request.getContextPath() %>/notes" class="btn btn-primary">View Notes</a>
                </div>
              </div>
            </div>
          </div>
        </div>
        <% } else if (user.isPatient()) { %>
        <div class="mt-4">
          <h3>Patient Dashboard</h3>
          <p>Welcome, <%= user.getFirstName() %> <%= user.getLastName() %>!</p>
          <div class="row g-3">
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">My Doctors</h5>
                  <p class="card-text">View and manage your assigned doctors</p>
                  <a href="<%= request.getContextPath() %>/doctors" class="btn btn-primary">View Doctors</a>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">View Medications</h5>
                  <p class="card-text">Check your current medications</p>
                  <a href="<%= request.getContextPath() %>/medications" class="btn btn-primary">View Medications</a>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">View Notes</h5>
                  <p class="card-text">Read notes from your doctors</p>
                  <a href="<%= request.getContextPath() %>/notes" class="btn btn-primary">View Notes</a>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card">
                <div class="card-body text-center">
                  <h5 class="card-title">Send Messages</h5>
                  <p class="card-text">Communicate with your healthcare team</p>
                  <a href="<%= request.getContextPath() %>/messages" class="btn btn-primary">Send Message</a>
                </div>
              </div>
            </div>

          </div>
        </div>
        <% } %>


      </div>
    </div>



    <script src="js/bootstrap.min.js"></script>
    <script>

    </script>
  </body>
</html>
