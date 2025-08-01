<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<%@ page import="com.group_3.healthlink.services.PatientService" %>
<%@ page import="com.group_3.healthlink.Patient" %>
<html>
<head>
  <title>Healthlink - My Doctors</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>
<body>
  <% User user = (User)session.getAttribute("user"); %>
  <% if (user.isDoctor()) { %>
    <script>
      window.location.href = '<%= request.getContextPath() %>/dashboard';
    </script>
  <% } else if (user.isPatient()) { %>
    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <div class="d-flex">
          <div class="d-flex border-bottom gap-4 mb-2">
            <h2 class="h2 fw-semibold">My Doctors</h2>
          </div>
        </div>

        <% String filter = request.getParameter("filter"); %>
        <% String query = request.getParameter("query"); %>
        <%
          com.group_3.healthlink.Patient patient = PatientService.getByUserId(user.getUserId());
          com.group_3.healthlink.Doctor[] assignedDoctors = null;
          if (patient != null) {
            assignedDoctors = PatientService.getDoctors(patient.getPatientId());
          } else {
            assignedDoctors = new com.group_3.healthlink.Doctor[0];
          }
        %>
        <% java.util.List<com.group_3.healthlink.Doctor> filteredDoctors = new java.util.ArrayList<>(); %>

        <form class="flex w-100 mb-3" style="max-width: 50%" id="searchForm" method="get">
          <div class="input-group">
            <select class="form-select" id="searchFilter" name="filter" style="max-width: 120px; min-width: 90px;">
              <option value="name" <%="name".equals(filter) ? "selected" : ""%>>Name</option>
              <option value="department" <%="department".equals(filter) ? "selected" : ""%>>Department</option>
            </select>
            <input type="text" class="form-control flex-grow-1"
                   id="searchQuery" name="query"
                   placeholder="Enter doctor name or department"
                   value="<%=query != null ? query : ""%>">
            <button class="btn btn-primary" type="submit">Search</button>
          </div>
        </form>

        <% if (query == null || query.trim().isEmpty()) {
            for (com.group_3.healthlink.Doctor d : assignedDoctors) filteredDoctors.add(d);
        } else if (filter != null && filter.equals("department")) {
            String q = query.trim().toLowerCase();
            for (com.group_3.healthlink.Doctor d : assignedDoctors) {
                if (d.getDepartment() != null &&
                        d.getDepartment().toLowerCase().contains(q)
                ) {
                    filteredDoctors.add(d);
                }
            }
        } else {
            String q = query.trim().toLowerCase();
            for (com.group_3.healthlink.Doctor d : assignedDoctors) {
                if (d.getFirstName() != null && d.getLastName() != null &&
                        (d.getFirstName().toLowerCase().contains(q) ||
                         d.getLastName().toLowerCase().contains(q) ||
                         (d.getFirstName() + " " + d.getLastName()).toLowerCase().contains(q))
                ) {
                    filteredDoctors.add(d);
                }
            }
        }
        %>

        <% if (filteredDoctors.isEmpty()) { %>
          <div class="alert alert-info" role="alert">
            <h6>No doctors found</h6>
            <% if (assignedDoctors.length == 0) { %>
              <p class="mb-2">You haven't been assigned to any doctors yet. You can:</p>
              <a href="<%= request.getContextPath() %>/department-selection" class="btn btn-primary">
                Choose a Department & Doctor
              </a>
            <% } else { %>
              <p class="mb-2">No doctors match your search criteria.</p>
            <% } %>
          </div>
        <% } else { %>
          <table class="table table-striped">
            <thead>
              <tr>
                <th>Doctor Name</th>
                <th>Department</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <% for (com.group_3.healthlink.Doctor doctor : filteredDoctors) { %>
                <tr>
                  <td>Dr. <%= doctor.getFirstName() %> <%= doctor.getLastName() %></td>
                  <td><span class="badge bg-primary"><%= doctor.getDepartment() %></span></td>
                  <td><%= doctor.getEmailAddress() %></td>
                  <td>
                    <div class="btn-group" role="group">
                                              <a href="<%= request.getContextPath() %>/messages/<%= doctor.getUserId() %>" class="btn btn-outline-primary btn-sm">
                          <i class="bi bi-chat"></i> Message
                        </a>
                      <button type="button" class="btn btn-outline-danger btn-sm remove-doctor-btn"
                              data-doctor-id="<%= doctor.getDoctorId() %>"
                              data-doctor-name="<%= doctor.getFirstName() %> <%= doctor.getLastName() %>">
                        <i class="bi bi-trash"></i> Remove
                      </button>
                    </div>
                  </td>
                </tr>
              <% } %>
            </tbody>
          </table>
        <% } %>

        <!-- Add Another Doctor Button -->
        <div class="mt-3">
          <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addDoctorModal">
            <i class="bi bi-plus-circle"></i> Add Another Doctor
          </button>
        </div>
      </div>
    </div>

    <!-- Add Doctor Modal -->
    <div class="modal fade" id="addDoctorModal" tabindex="-1" aria-labelledby="addDoctorModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addDoctorModalLabel">Add a New Doctor</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <!-- Step 1: Department Selection -->
            <div id="departmentStep">
              <h6 class="mb-3">Step 1: Choose a Department</h6>
              <p class="text-muted mb-4">Select the department that best matches your health concern.</p>
              
              <div class="row g-3">
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="cardiology" value="Cardiology" required>
                    <label class="form-check-label" for="cardiology">
                      <strong>Cardiology</strong><br>
                      <small class="text-muted">Heart and cardiovascular health</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="dermatology" value="Dermatology" required>
                    <label class="form-check-label" for="dermatology">
                      <strong>Dermatology</strong><br>
                      <small class="text-muted">Skin, hair, and nail conditions</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="endocrinology" value="Endocrinology" required>
                    <label class="form-check-label" for="endocrinology">
                      <strong>Endocrinology</strong><br>
                      <small class="text-muted">Diabetes, thyroid, and hormone disorders</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="gastroenterology" value="Gastroenterology" required>
                    <label class="form-check-label" for="gastroenterology">
                      <strong>Gastroenterology</strong><br>
                      <small class="text-muted">Digestive system and gut health</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="pediatrics" value="Pediatrics" required>
                    <label class="form-check-label" for="pediatrics">
                      <strong>Pediatrics</strong><br>
                      <small class="text-muted">Children's health and development</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="general" value="General Medicine" required>
                    <label class="form-check-label" for="general">
                      <strong>General Medicine</strong><br>
                      <small class="text-muted">Primary care and general health</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="oncology" value="Oncology" required>
                    <label class="form-check-label" for="oncology">
                      <strong>Oncology</strong><br>
                      <small class="text-muted">Cancer treatment and care</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="nephrology" value="Nephrology" required>
                    <label class="form-check-label" for="nephrology">
                      <strong>Nephrology</strong><br>
                      <small class="text-muted">Kidney health and diseases</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="urology" value="Urology" required>
                    <label class="form-check-label" for="urology">
                      <strong>Urology</strong><br>
                      <small class="text-muted">Urinary system and male reproductive health</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="ophthalmology" value="Ophthalmology" required>
                    <label class="form-check-label" for="ophthalmology">
                      <strong>Ophthalmology</strong><br>
                      <small class="text-muted">Eye health and vision care</small>
                    </label>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-check p-3 border rounded">
                    <input class="form-check-input" type="radio" name="department" id="ent" value="ENT" required>
                    <label class="form-check-label" for="ent">
                      <strong>ENT</strong><br>
                      <small class="text-muted">Ear, nose, and throat conditions</small>
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <!-- Step 2: Doctor Selection -->
            <div id="doctorStep" style="display: none;">
              <h6 class="mb-3">Step 2: Choose a Doctor</h6>
              <p class="text-muted mb-4">Select a doctor from the <span id="selectedDepartment"></span> department.</p>
              
              <div id="doctorsList">
                <!-- Doctors will be loaded here -->
              </div>
            </div>

            <!-- Loading State -->
            <div id="loadingState" style="display: none;">
              <div class="text-center">
                <div class="spinner-border" role="status">
                  <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">Loading doctors...</p>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="backBtn" style="display: none;">Back</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-primary" id="nextBtn">Continue</button>
            <button type="button" class="btn btn-primary" id="selectDoctorBtn" style="display: none;">Select Doctor</button>
          </div>
        </div>
      </div>
    </div>
      </div>
    </div>

    <script src="js/bootstrap.min.js"></script>
    <script>
      function removeDoctor(doctorId, doctorName) {
        if (confirm('Are you sure you want to remove Dr. ' + doctorName + ' from your care team?')) {
          fetch('<%= request.getContextPath() %>/remove-doctor', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'doctorId=' + doctorId
          })
          .then(response => response.json())
          .then(data => {
            if (data.success) {
              location.reload();
            } else {
              alert('Failed to remove doctor: ' + data.error);
            }
          })
          .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while removing the doctor.');
          });
        }
      }

      document.addEventListener('DOMContentLoaded', () => {
        // Handle remove doctor buttons
        for (const btn of document.querySelectorAll('.remove-doctor-btn')) {
          btn.addEventListener('click', () => {
            const doctorId = btn.getAttribute('data-doctor-id');
            const doctorName = btn.getAttribute('data-doctor-name');
            removeDoctor(doctorId, doctorName);
          });
        }

        // Modal functionality
        const modal = document.getElementById('addDoctorModal');
        const departmentStep = document.getElementById('departmentStep');
        const doctorStep = document.getElementById('doctorStep');
        const loadingState = document.getElementById('loadingState');
        const nextBtn = document.getElementById('nextBtn');
        const backBtn = document.getElementById('backBtn');
        const selectDoctorBtn = document.getElementById('selectDoctorBtn');
        const selectedDepartmentSpan = document.getElementById('selectedDepartment');
        const doctorsList = document.getElementById('doctorsList');
        let selectedDepartment = '';
        let selectedDoctorId = '';

        // Reset modal when it's closed
        modal.addEventListener('hidden.bs.modal', () => {
          resetModal();
        });

        // Next button (department selection)
        nextBtn.addEventListener('click', () => {
          const selectedDept = document.querySelector('input[name="department"]:checked');
          if (!selectedDept) {
            alert('Please select a department first.');
            return;
          }
          
          selectedDepartment = selectedDept.value;
          selectedDepartmentSpan.textContent = selectedDepartment;
          
          // Show loading state
          departmentStep.style.display = 'none';
          loadingState.style.display = 'block';
          nextBtn.style.display = 'none';
          
          // Load doctors for the selected department
          loadDoctors(selectedDepartment);
        });

        // Back button
        backBtn.addEventListener('click', () => {
          doctorStep.style.display = 'none';
          selectDoctorBtn.style.display = 'none';
          backBtn.style.display = 'none';
          departmentStep.style.display = 'block';
          nextBtn.style.display = 'inline-block';
        });

        // Select doctor button
        selectDoctorBtn.addEventListener('click', () => {
          if (!selectedDoctorId) {
            alert('Please select a doctor first.');
            return;
          }
          
          assignDoctor(selectedDoctorId);
        });

        function loadDoctors(department) {
          fetch('<%= request.getContextPath() %>/api/doctors?department=' + encodeURIComponent(department))
            .then(response => response.json())
            .then(data => {
              console.log('API Response:', data); // Debug log
              loadingState.style.display = 'none';
              
              if (data.success && data.doctors && data.doctors.length > 0) {
                console.log('Doctors found:', data.doctors); // Debug log
                displayDoctors(data.doctors);
                doctorStep.style.display = 'block';
                selectDoctorBtn.style.display = 'inline-block';
                backBtn.style.display = 'inline-block';
              } else {
                console.log('No doctors found for department:', department); // Debug log
                alert('No doctors available in the ' + department + ' department. Please try a different department.');
                resetModal();
              }
            })
            .catch(error => {
              console.error('Error loading doctors:', error);
              loadingState.style.display = 'none';
              alert('Error loading doctors. Please try again.');
              resetModal();
            });
        }

        function displayDoctors(doctors) {
          doctorsList.innerHTML = '';
          
          doctors.forEach(doctor => {
            console.log('Processing doctor:', doctor); // Debug each doctor object
            
            const doctorDiv = document.createElement('div');
            doctorDiv.className = 'col-md-6 mb-3';
            
            // Create the HTML structure manually to avoid template literal issues
            const radioId = 'doctor' + doctor.doctorId;
            const doctorName = 'Dr. ' + doctor.firstName + ' ' + doctor.lastName;
            const department = doctor.department;
            const doctorIdText = 'Doctor ID: ' + doctor.doctorId;
            
            doctorDiv.innerHTML = 
              '<div class="form-check p-3 border rounded h-100">' +
                '<input class="form-check-input" type="radio" name="doctorId" ' +
                       'id="' + radioId + '" ' +
                       'value="' + doctor.doctorId + '" required>' +
                '<label class="form-check-label" for="' + radioId + '">' +
                  '<div class="d-flex flex-column">' +
                    '<strong>' + doctorName + '</strong>' +
                    '<small class="text-muted">' + department + '</small>' +
                    '<small class="text-muted">' + doctorIdText + '</small>' +
                  '</div>' +
                '</label>' +
              '</div>';
            
            doctorsList.appendChild(doctorDiv);
          });

          // Add event listeners to doctor radio buttons
          document.querySelectorAll('input[name="doctorId"]').forEach(radio => {
            radio.addEventListener('change', (e) => {
              selectedDoctorId = e.target.value;
            });
          });
        }

        function assignDoctor(doctorId) {
          fetch('<%= request.getContextPath() %>/assign-doctor', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'doctorId=' + doctorId
          })
          .then(response => response.json())
          .then(data => {
            if (data.success) {
              alert('Doctor assigned successfully!');
              location.reload();
            } else {
              alert('Failed to assign doctor: ' + (data.error || 'Unknown error'));
            }
          })
          .catch(error => {
            console.error('Error assigning doctor:', error);
            alert('An error occurred while assigning the doctor.');
          });
        }

        function resetModal() {
          departmentStep.style.display = 'block';
          doctorStep.style.display = 'none';
          loadingState.style.display = 'none';
          nextBtn.style.display = 'inline-block';
          backBtn.style.display = 'none';
          selectDoctorBtn.style.display = 'none';
          selectedDepartment = '';
          selectedDoctorId = '';
          
          // Clear selections
          document.querySelectorAll('input[name="department"]').forEach(radio => radio.checked = false);
          document.querySelectorAll('input[name="doctorId"]').forEach(radio => radio.checked = false);
        }
      });
    </script>
  <% } %>
</body>
</html> 