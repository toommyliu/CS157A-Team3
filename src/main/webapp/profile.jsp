<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<html>
<head>
  <title>Healthlink - Profile</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>
<body>
  <% User user = (User)session.getAttribute("user"); %>
  <% if (user == null) { %>
    <script>
      window.location.href = '<%= request.getContextPath() %>/login';
    </script>
  <% } else { %>
    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <div class="d-flex">
          <div class="d-flex border-bottom gap-4 mb-4">
            <h2 class="h2 fw-semibold">My Profile</h2>
          </div>
        </div>

        <div class="row">
          <div class="col-md-8">
            <div class="card">
              <div class="card-header">
                <h5 class="card-title mb-0">
                  <i class="bi bi-person-circle me-2"></i>
                  Personal Information
                </h5>
              </div>
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">Full Name</label>
                      <p class="form-control-plaintext fs-5">
                        <%= user.getFirstName() %> <%= user.getLastName() %>
                      </p>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">Email Address</label>
                      <p class="form-control-plaintext fs-5">
                        <%= user.getEmailAddress() %>
                      </p>
                    </div>
                  </div>
                </div>
                
                <div class="row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">User ID</label>
                      <p class="form-control-plaintext fs-5">
                        <%= user.getUserId() %>
                      </p>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">Role</label>
                      <p class="form-control-plaintext fs-5">
                        <span class="badge bg-primary fs-6">
                          <%= user.getRole() %>
                        </span>
                      </p>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">Account Created</label>
                      <p class="form-control-plaintext fs-5">
                        <%= user.getCreatedAt() %>
                      </p>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold text-muted">Last Updated</label>
                      <p class="form-control-plaintext fs-5">
                        <%= user.getUpdatedAt() %>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Role-specific information -->
            <% if (user.isDoctor()) { %>
              <div class="card mt-4">
                <div class="card-header">
                  <h5 class="card-title mb-0">
                    <i class="bi bi-stethoscope me-2"></i>
                    Doctor Information
                  </h5>
                </div>
                <div class="card-body">
                  <%
                    com.group_3.healthlink.Doctor doctor = com.group_3.healthlink.services.DoctorService.getByUserId(user.getUserId());
                    if (doctor != null) {
                  %>
                    <div class="row">
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Department</label>
                          <p class="form-control-plaintext fs-5">
                            <span class="badge bg-info fs-6">
                              <%= doctor.getDepartment() %>
                            </span>
                          </p>
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Doctor ID</label>
                          <p class="form-control-plaintext fs-5">
                            <%= doctor.getDoctorId() %>
                          </p>
                        </div>
                      </div>
                    </div>
                  <% } else { %>
                    <p class="text-muted">Doctor information not available.</p>
                  <% } %>
                </div>
              </div>
            <% } else if (user.isPatient()) { %>
              <div class="card mt-4">
                <div class="card-header">
                  <h5 class="card-title mb-0">
                    <i class="bi bi-heart-pulse me-2"></i>
                    Patient Information
                  </h5>
                </div>
                <div class="card-body">
                  <%
                    com.group_3.healthlink.Patient patient = com.group_3.healthlink.services.PatientService.getByUserId(user.getUserId());
                    if (patient != null) {
                  %>
                    <div class="row">
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Patient ID</label>
                          <p class="form-control-plaintext fs-5">
                            <%= patient.getPatientId() %>
                          </p>
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Date of Birth</label>
                          <p class="form-control-plaintext fs-5">
                            <%= patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "Not provided" %>
                          </p>
                        </div>
                      </div>
                    </div>
                    
                    <div class="row">
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Phone Number</label>
                          <p class="form-control-plaintext fs-5">
                            <%= patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "Not provided" %>
                          </p>
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Emergency Contact</label>
                          <p class="form-control-plaintext fs-5">
                            <%= patient.getEmergencyContactName() != null ? patient.getEmergencyContactName() : "Not provided" %>
                          </p>
                        </div>
                      </div>
                    </div>
                    
                    <div class="row">
                      <div class="col-12">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Address</label>
                          <p class="form-control-plaintext fs-5">
                            <%= patient.getAddress() != null ? patient.getAddress() : "Not provided" %>
                          </p>
                        </div>
                      </div>
                    </div>
                  <% } else { %>
                    <p class="text-muted">Patient information not available.</p>
                  <% } %>
                </div>
              </div>
            <% } %>
          </div>

          <div class="col-md-4">
            <div class="card">
              <div class="card-header">
                <h5 class="card-title mb-0">
                  <i class="bi bi-gear me-2"></i>
                  Account Actions
                </h5>
              </div>
              <div class="card-body">
                <div class="d-grid gap-2">
                  <button class="btn btn-outline-primary" disabled>
                    <i class="bi bi-pencil me-2"></i>
                    Edit Profile
                  </button>
                  <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger">
                    <i class="bi bi-box-arrow-right me-2"></i>
                    Logout
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  <% } %>

  <script src="js/bootstrap.min.js"></script>
</body>
</html> 