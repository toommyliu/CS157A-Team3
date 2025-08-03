<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="com.group_3.healthlink.services.PatientService" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<%@ page import="com.group_3.healthlink.Patient" %>
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
      <%
        Doctor doctor = null;
        Patient patient = null;
        if (user.isDoctor()) {
          doctor = DoctorService.getByUserId(user.getUserId());
        } else if (user.isPatient()) {
          patient = PatientService.getByUserId(user.getUserId());
        }
      %>
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
                <div class="card-header d-flex justify-content-between align-items-center">
                  <h5 class="card-title mb-0">
                    <i class="bi bi-person-circle me-2"></i>
                    Personal Information
                  </h5>
                  <button class="btn btn-sm btn-outline-primary" id="editPersonalBtn">
                    <i class="bi bi-pencil"></i> Edit
                  </button>
                </div>
                <div class="card-body" id="personalInfo">
                  <form id="personalEditForm">
                    <div class="row">
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Full Name</label>
                          <input type="text" class="form-control" name="fullName" value="<%= user.getFirstName() %> <%= user.getLastName() %>" disabled readonly />
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Email Address</label>
                          <input type="email" class="form-control" name="email" value="<%= user.getEmailAddress() %>" disabled readonly />
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">User ID</label>
                          <input type="text" class="form-control" name="userId" value="<%= user.getUserId() %>" disabled readonly />
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
                          <input type="text" class="form-control" name="createdAt" value="<%= user.getCreatedAt() %>" disabled readonly />
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="mb-3">
                          <label class="form-label fw-bold text-muted">Last Updated</label>
                          <input type="text" class="form-control" name="updatedAt" value="<%= user.getUpdatedAt() %>" disabled readonly />
                        </div>
                      </div>
                    </div>
                    <button type="submit" class="btn btn-primary" id="savePersonalBtn" style="display:none;">Save</button>
                  </form>
                </div>
              </div>

              <!-- Role-specific information -->
              <% if (user.isDoctor()) { %>
                <div class="card mt-4">
                  <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-clipboard-pulse me-2"></i>
                      Doctor Information
                    </h5>
                    <button class="btn btn-sm btn-outline-primary" id="editDoctorBtn">
                      <i class="bi bi-pencil"></i> Edit
                    </button>
                  </div>
                  <div class="card-body" id="doctorInfo">
                    <% if (doctor != null) { %>
                      <form id="doctorEditForm">
                        <div class="row">
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label for="departmentSelect" class="form-label fw-bold text-muted">Department</label>
                              <select class="form-control" id="departmentSelect" name="department" disabled readonly>
                                <option value="Cardiology" <%= doctor != null && "Cardiology".equals(doctor.getDepartment()) ? "selected" : "" %>>Cardiology</option>
                                <option value="Dermatology" <%= doctor != null && "Dermatology".equals(doctor.getDepartment()) ? "selected" : "" %>>Dermatology</option>
                                <option value="Endocrinology" <%= doctor != null && "Endocrinology".equals(doctor.getDepartment()) ? "selected" : "" %>>Endocrinology</option>
                                <option value="Gastroenterology" <%= doctor != null && "Gastroenterology".equals(doctor.getDepartment()) ? "selected" : "" %>>Gastroenterology</option>
                                <option value="Pediatrics" <%= doctor != null && "Pediatrics".equals(doctor.getDepartment()) ? "selected" : "" %>>Pediatrics</option>
                                <option value="General Medicine" <%= doctor != null && "General Medicine".equals(doctor.getDepartment()) ? "selected" : "" %>>General Medicine</option>
                                <option value="Oncology" <%= doctor != null && "Oncology".equals(doctor.getDepartment()) ? "selected" : "" %>>Oncology</option>
                                <option value="Nephrology" <%= doctor != null && "Nephrology".equals(doctor.getDepartment()) ? "selected" : "" %>>Nephrology</option>
                                <option value="Urology" <%= doctor != null && "Urology".equals(doctor.getDepartment()) ? "selected" : "" %>>Urology</option>
                                <option value="Ophthalmology" <%= doctor != null && "Ophthalmology".equals(doctor.getDepartment()) ? "selected" : "" %>>Ophthalmology</option>
                                <option value="ENT" <%= doctor != null && "ENT".equals(doctor.getDepartment()) ? "selected" : "" %>>ENT</option>
                              </select>
                            </div>
                          </div>
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label class="form-label fw-bold text-muted">Doctor ID</label>
                              <input type="text" class="form-control" name="doctorId" value="<%= doctor.getDoctorId() %>" disabled readonly />
                            </div>
                          </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="saveDoctorBtn" style="display:none;">Save</button>
                      </form>
                    <% } else { %>
                      <p class="text-muted">Doctor information not available.</p>
                    <% } %>
                  </div>
                </div>
              <% } else if (user.isPatient()) { %>
                <div class="card mt-4">
                  <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-heart-pulse me-2"></i>
                      Patient Information
                    </h5>
                    <button class="btn btn-sm btn-outline-primary d-flex align-items-center gap-2" id="editPatientBtn">
                      <i class="bi bi-pencil"></i> Edit
                    </button>
                  </div>
                  <div class="card-body" id="patientInfo">
                    <% if (patient != null) { %>
                      <form id="patientEditForm">
                        <div class="row">
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label for="dobInput" class="form-label fw-bold text-muted">Date of Birth</label>
                              <input type="date" class="form-control" id="dobInput" name="dob" value="<%= patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "" %>" disabled readonly />
                            </div>
                          </div>
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label class="form-label fw-bold text-muted">Patient ID</label>
                              <input type="text" class="form-control" name="patientId" value="<%= patient.getPatientId() %>" disabled readonly />
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label for="phoneInput" class="form-label fw-bold text-muted">Phone Number</label>
                              <input type="text" class="form-control" id="phoneInput" name="phone"
                                     value="<%= patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "" %>"
                                     disabled readonly pattern="^\(\d{3}\) \d{3}-\d{4}$" placeholder="(408) 456-7890"
                              />
                            </div>
                          </div>
                          <div class="col-md-6">
                            <div class="mb-3">
                              <label for="emergencyContactInput" class="form-label fw-bold text-muted">Emergency Contact</label>
                              <input type="text" class="form-control" id="emergencyContactInput" name="emergencyContact" value="<%= patient.getEmergencyContactName() != null ? patient.getEmergencyContactName() : "" %>" disabled readonly />
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-12">
                            <div class="mb-3">
                              <label for="addressInput" class="form-label fw-bold text-muted">Address</label>
                              <input type="text" class="form-control" id="addressInput" name="address" value="<%= patient.getAddress() != null ? patient.getAddress() : "" %>" disabled readonly />
                            </div>
                          </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="savePatientBtn" style="display:none;">Save</button>
                      </form>
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
                    <a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">
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
    <script>
      const IGNORE_FIELDS = ['doctorId', 'patientId', 'userId', 'createdAt', 'updatedAt'];

      function setDefaultDataAttributes(form) {
        if (!form) return;
        const inputs = form.querySelectorAll('input, select');
        for (const input of inputs) {
          if (IGNORE_FIELDS.includes(input.name)) continue;
          input.setAttribute('data-default', input.value);
        }
      }

      function restoreDefaultValues(form, inputs) {
        const allElements = [
          ...inputs,
          ...form.querySelectorAll('select')
        ];

        for (const el of allElements) {
          if (IGNORE_FIELDS.includes(el.name)) continue;
          if (el.hasAttribute('data-default')) el.value = el.getAttribute('data-default');
        }
      }

      function toggleEdit(section) {
        let form;
        let inputs;
        let saveBtn;
        let editBtn;

        if (section === 'personal') {
          form = document.querySelector('#personalEditForm');
          inputs = form.querySelectorAll('input');
          saveBtn = document.querySelector('#savePersonalBtn');
          editBtn = document.querySelector('#editPersonalBtn');
        } else if (section === 'patient') {
          form = document.querySelector('#patientEditForm');
          inputs = form.querySelectorAll('input');
          saveBtn = document.querySelector('#savePatientBtn');
          editBtn = document.querySelector('#editPatientBtn');
        } else if (section === 'doctor') {
          form = document.querySelector('#doctorEditForm');
          inputs = form.querySelectorAll('input');
          saveBtn = document.querySelector('#saveDoctorBtn');
          editBtn = document.querySelector('#editDoctorBtn');
        }

        const isEditing = editBtn.textContent.includes('Edit');

        for (const input of inputs) {
          if (IGNORE_FIELDS.includes(input.name)) continue;
          input.disabled = !isEditing;
          input.readOnly = !isEditing;
        }

        if (section === 'doctor') {
          const deptSelect = document.querySelector('#departmentSelect');
          if (deptSelect)
            deptSelect.disabled = !isEditing;
        }

        saveBtn.style.display = isEditing ? 'inline-block' : 'none';
        editBtn.textContent = isEditing ? ' Cancel' : 'Edit';

        // Reset the form fields to their default values if not editing anymore
        if (!isEditing) restoreDefaultValues(form, inputs);
      }

      document.addEventListener('DOMContentLoaded', () => {
        // Preserve default form values if the user cancels editing
        setDefaultDataAttributes(document.querySelector('#personalEditForm'));
        setDefaultDataAttributes(document.querySelector('#patientEditForm'));
        setDefaultDataAttributes(document.querySelector('#doctorEditForm'));

        document.querySelector('#editPersonalBtn')?.addEventListener('click', () => toggleEdit('personal'));
        document.querySelector('#editPatientBtn')?.addEventListener('click', () => toggleEdit('patient'));
        document.querySelector('#editDoctorBtn')?.addEventListener('click', () => toggleEdit('doctor'));
      });
    </script>
  </body>
</html>
