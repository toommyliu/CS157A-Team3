<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.group_3.healthlink.*" %>
<%@ page import="java.util.HashMap" %>
<html>
<head>
  <title>Healthlink - Patients</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
  <style>
    /* Enhanced tab styling */
    .nav-tabs .nav-link {
      border: none;
      border-bottom: 2px solid transparent;
      color: #6c757d;
      font-weight: 500;
      padding: 0.75rem 1.5rem;
      transition: all 0.2s ease;
    }
    
    .nav-tabs .nav-link:hover {
      border-color: transparent;
      color: #495057;
      background-color: rgba(0, 123, 255, 0.05);
    }
    
    .nav-tabs .nav-link.active {
      border-bottom: 2px solid #0d6efd;
      color: #0d6efd;
      background-color: transparent;
    }
    
    .nav-tabs .nav-link.active:hover {
      border-bottom: 2px solid #0d6efd;
      color: #0d6efd;
    }
    
    /* Card styling for medications */
    .medication-card {
      transition: all 0.2s ease;
      border: 1px solid #dee2e6;
    }
    
    .medication-card:hover {
      box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
      border-color: #adb5bd;
    }
    
    /* Tab content animation */
    .tab-pane {
      transition: opacity 0.15s linear;
    }
    
    .tab-pane.fade {
      opacity: 0;
    }
    
    .tab-pane.fade.show {
      opacity: 1;
    }
    
    /* Empty state styling */
    .empty-state {
      padding: 3rem 1rem;
    }
    
    .empty-state i {
      opacity: 0.5;
    }
  </style>
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
              <%
                com.group_3.healthlink.Doctor doctor = DoctorService.getByUserId(user.getUserId());
                Patient[] allPatients = null;
                if (doctor != null) {
                  allPatients = DoctorService.getPatients(doctor.getDoctorId());
                } else {
                  allPatients = new Patient[0];
                }
              %>
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
                          <div class="btn-group" role="group">
                            <a href="${pageContext.request.contextPath}/patients/<%=p.getUserId()%>" class="btn btn-outline-primary btn-sm">
                              <i class="bi bi-eye"></i> View
                            </a>
                            <a href="${pageContext.request.contextPath}/messages/<%=p.getUserId()%>" class="btn btn-outline-primary btn-sm">
                              <i class="bi bi-chat"></i> Message
                            </a>
                            <button type="button" class="btn btn-outline-danger btn-sm remove-patient-btn"
                                    data-patient-id="<%= p.getPatientId() %>"
                                    data-patient-name="<%= p.getFullName() %>">
                              <i class="bi bi-trash"></i> Remove
                            </button>
                          </div>
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
                List<Medication> medications = (List<Medication>) request.getAttribute("medications");
                if (patient != null) { %>
              <div class="main-content">
                <div class="d-flex justify-content-between align-items-center border-bottom mb-4 pb-2">
                  <h2 class="h2 fw-semibold mb-0">Patient Details</h2>
                  <a href="${pageContext.request.contextPath}/patients" class="btn btn-secondary">Back to Patients</a>
                </div>

                <div class="row">
                  <!-- Personal Information Card -->
                  <div class="col-10">
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
                          </div>
                          <div class="col-md-6">
                            <p><strong>Phone Number:</strong> <%= patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "N/A" %></p>
                            <p><strong>Address:</strong> <%= patient.getAddress() != null ? patient.getAddress() : "N/A" %></p>
                            <p><strong>Date of Birth:</strong> <%= patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "N/A" %></p>
                            <p><strong>Member Since:</strong> <%= patient.getCreatedAt() != null ? patient.getCreatedAt() : "N/A" %></p>
                          </div>
                        </div>
                        
                        <!-- Emergency Contact Section -->
                        <hr class="my-4">
                        <h6 class="text-muted mb-3">
                          <i class="bi bi-exclamation-triangle me-2"></i>Emergency Contact
                        </h6>
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

                  <!-- Tabbed Content Section -->
                  <div class="col-10">
                    <div class="card">
                      <div class="card-header">
                        <ul class="nav nav-tabs card-header-tabs" id="patientTabs" role="tablist">
                          <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="medications-tab" data-bs-toggle="tab" data-bs-target="#medications" type="button" role="tab" aria-controls="medications" aria-selected="true">
                              <i class="bi bi-capsule-pill me-2"></i>Medications
                            </button>
                          </li>
                          <li class="nav-item" role="presentation">
                            <button class="nav-link" id="test-results-tab" data-bs-toggle="tab" data-bs-target="#test-results" type="button" role="tab" aria-controls="test-results" aria-selected="false">
                              <i class="bi bi-file-earmark-medical me-2"></i>Test Results
                            </button>
                          </li>
                        </ul>
                      </div>
                      <div class="card-body">
                        <div class="tab-content" id="patientTabsContent">
                          <!-- Medications Tab -->
                          <div class="tab-pane fade show active" id="medications" role="tabpanel" aria-labelledby="medications-tab">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                              <h6 class="mb-0">Patient Medications</h6>
                              <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addMedicationModal">
                                <i class="bi bi-plus-circle me-1"></i>Add Medication
                              </button>
                            </div>
                            <% if (medications != null && !medications.isEmpty()) { %>
                              <div class="medication-list">
                                <% for (Medication medication : medications) { %>
                                  <div class="card border mb-3">
                                    <div class="card-body">
                                      <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                          <h6 class="card-title mb-2"><%= medication.getName() %></h6>
                                          <p class="text-muted small mb-1">
                                            <i class="bi bi-capsule me-1"></i>Dosage: <%= medication.getDosage() %>
                                          </p>
                                          <p class="text-muted small mb-1">
                                            <i class="bi bi-clock me-1"></i>Frequency: <%= medication.getFrequency() %>
                                          </p>
                                          <% if (medication.getNotes() != null && !medication.getNotes().trim().isEmpty()) { %>
                                            <p class="text-muted small mb-0">
                                              <i class="bi bi-sticky me-1"></i>Notes: <%= medication.getNotes() %>
                                            </p>
                                          <% } %>
                                        </div>
                                        <div class="btn-group ms-2">
                                          <button class="btn btn-outline-primary edit-medication-btn"
                                                  data-id="<%= medication.getId() %>"
                                                  data-name="<%= medication.getName() %>"
                                                  data-dosage="<%= medication.getDosage() %>"
                                                  data-frequency="<%= medication.getFrequency() %>"
                                                  data-notes="<%= medication.getNotes() != null ? medication.getNotes() : "" %>"
                                                  data-bs-toggle="modal"
                                                  data-bs-target="#editMedicationModal"
                                                  title="Edit Medication">
                                            <i class="bi bi-pencil me-1"></i>
                                          </button>
                                          <button class="btn btn-outline-danger delete-medication-btn"
                                                  data-id="<%= medication.getId() %>"
                                                  data-name="<%= medication.getName() %>"
                                                  title="Delete Medication">
                                            <i class="bi bi-trash me-1"></i>
                                          </button>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                <% } %>
                              </div>
                            <% } else { %>
                              <div class="text-center py-4">
                                <i class="bi bi-capsule-pill text-muted" style="font-size: 3rem;"></i>
                                <h6 class="text-muted mt-3">No medications prescribed</h6>
                                <p class="text-muted small">This patient hasn't been prescribed any medications yet.</p>
                              </div>
                            <% } %>
                          </div>

                          <!-- Test Results Tab -->
                          <div class="tab-pane fade" id="test-results" role="tabpanel" aria-labelledby="test-results-tab">
                            <%
                            List<TestResult> testResults = (List<TestResult>) request.getAttribute("testResults");
                            %>
                            <% if (testResults != null && !testResults.isEmpty()) { %>
                              <div class="table-responsive">
                                <table class="table table-hover">
                                  <thead class="table-light">
                                    <tr>
                                      <th>File Name</th>
                                      <th>Type</th>
                                      <th>Description</th>
                                      <th>Upload Date</th>
                                      <th>Actions</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <% for (TestResult result : testResults) { %>
                                      <tr>
                                        <td>
                                          <div class="d-flex align-items-center">
                                            <i class="bi bi-file-earmark-<%= result.isPdf() ? "pdf" : "image" %> text-<%= result.isPdf() ? "danger" : "info" %> me-2"></i>
                                            <span class="fw-medium"><%= result.getFileName() %></span>
                                          </div>
                                        </td>
                                        <td>
                                          <span class="badge bg-<%= result.isPdf() ? "danger" : "info" %>">
                                            <%= result.getFileType().toUpperCase() %>
                                          </span>
                                        </td>
                                        <td>
                                          <% if (result.getDescription() != null && !result.getDescription().trim().isEmpty()) { %>
                                            <%= result.getDescription() %>
                                          <% } else { %>
                                            <span class="text-muted fst-italic">No description</span>
                                          <% } %>
                                        </td>
                                        <td><%= result.getUploadDate() %></td>
                                        <td>
                                          <a href="<%= request.getContextPath() %>/test-results/download/<%= result.getResultId() %>"
                                             class="btn btn-outline-primary btn-sm"
                                             title="Download Test Result">
                                            <i class="bi bi-download"></i>
                                          </a>
                                        </td>
                                      </tr>
                                    <% } %>
                                  </tbody>
                                </table>
                              </div>
                            <% } else { %>
                              <div class="text-center py-4">
                                <i class="bi bi-file-earmark-medical text-muted" style="font-size: 3rem;"></i>
                                <h6 class="text-muted mt-3">No test results found</h6>
                                <p class="text-muted small">This patient hasn't uploaded any test results yet.</p>
                              </div>
                            <% } %>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="modal fade" id="addMedicationModal" tabindex="-1">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h2 class="modal-title fs-5">Add New Medication</h2>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <form id="addMedicationForm" action="<%= request.getContextPath() %>/medication/create" method="POST">
                        <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                        <div class="modal-body">
                          <div class="mb-3">
                            <label for="addMedicationName" class="form-label">Medication Name</label>
                            <input type="text" id="addMedicationName" name="medicationName" class="form-control" placeholder="Medication name" required>
                          </div>
                          <div class="mb-3">
                            <label for="addDosage" class="form-label">Dosage</label>
                            <input type="text" id="addDosage" name="dosage" class="form-control" placeholder="e.g., 10mg" required>
                          </div>
                          <div class="mb-3">
                            <label for="addFrequency" class="form-label">Frequency</label>
                            <input type="text" id="addFrequency" name="frequency" class="form-control" placeholder="e.g., Twice daily" required>
                          </div>
                          <div class="mb-3">
                            <label for="addNoteContent" class="form-label">Notes (Optional)</label>
                            <textarea id="addNoteContent" name="noteContent" class="form-control" rows="3" placeholder="Additional notes"></textarea>
                          </div>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                          <button type="submit" class="btn btn-primary">Add Medication</button>
                        </div>
                      </form>
                    </div>
                  </div>
                </div>

                <div class="modal fade" id="editMedicationModal" tabindex="-1">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h2 class="modal-title fs-5">Edit Medication</h2>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <form id="editMedicationForm" action="<%= request.getContextPath() %>/medication/update" method="POST">
                        <input type="hidden" id="editMedicationId" name="medicationId">
                        <div class="modal-body">
                          <div class="mb-3">
                            <label for="editMedicationName" class="form-label">Medication Name</label>
                            <input type="text" id="editMedicationName" name="medicationName" class="form-control" required>
                          </div>
                          <div class="mb-3">
                            <label for="editDosage" class="form-label">Dosage</label>
                            <input type="text" id="editDosage" name="dosage" class="form-control" required>
                          </div>
                          <div class="mb-3">
                            <label for="editFrequency" class="form-label">Frequency</label>
                            <input type="text" id="editFrequency" name="frequency" class="form-control" required>
                          </div>
                          <div class="mb-3">
                            <label for="editNoteContent" class="form-label">Notes (Optional)</label>
                            <textarea id="editNoteContent" name="noteContent" class="form-control" rows="3"></textarea>
                          </div>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                          <button type="submit" class="btn btn-primary">Update Medication</button>
                        </div>
                      </form>
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
  <script>
    document.addEventListener('DOMContentLoaded', function() {
      // Check if Bootstrap is loaded
      if (typeof bootstrap === 'undefined') {
        console.warn('Bootstrap is not loaded');
        return;
      }

      // Enhanced tab functionality
      const tabButtons = document.querySelectorAll('#patientTabs .nav-link');
      const tabPanes = document.querySelectorAll('.tab-pane');
      
      tabButtons.forEach(button => {
        button.addEventListener('click', function(e) {
          // Remove active class from all tabs
          tabButtons.forEach(btn => {
            btn.classList.remove('active');
            btn.setAttribute('aria-selected', 'false');
          });
          
          // Add active class to clicked tab
          this.classList.add('active');
          this.setAttribute('aria-selected', 'true');
          
          // Hide all tab panes
          tabPanes.forEach(pane => {
            pane.classList.remove('show', 'active');
          });
          
          // Show the target tab pane
          const targetId = this.getAttribute('data-bs-target');
          const targetPane = document.querySelector(targetId);
          if (targetPane) {
            targetPane.classList.add('show', 'active');
          }
        });
      });

      const addMedicationForm = document.querySelector('#addMedicationForm');
      const addMedicationModalElement = document.getElementById('addMedicationModal');
      const addMedicationModal = addMedicationModalElement ? new bootstrap.Modal(addMedicationModalElement) : null;

      if (addMedicationForm) {
        addMedicationForm.addEventListener('submit', async function(ev) {
          ev.preventDefault();
          const formData = new FormData(addMedicationForm);
          const data = new URLSearchParams();
          for (const pair of formData) {
            data.append(pair[0], pair[1]);
          }

          try {
            const resp = await fetch(addMedicationForm.action, {
              method: 'POST',
              body: data,
            });

            let result;
            try {
              result = await resp.json();
            } catch {
              result = { success: resp.ok };
            }

            if (addMedicationModal) {
              addMedicationModal.hide();
            }

            if (result.success) {
              alert('Medication added successfully');
              window.location.reload();
            } else {
              alert('Failed to add medication');
            }
          } catch (error) {
            console.error('error adding medication:', error);
            alert('Error adding medication');
          }
        });
      }


      const editButtons = document.querySelectorAll('.edit-medication-btn');
      for (const btn of editButtons) {
        btn.addEventListener('click', () => {
          const id = btn.getAttribute('data-id');
          const name = btn.getAttribute('data-name');
          const dosage = btn.getAttribute('data-dosage');
          const frequency = btn.getAttribute('data-frequency');
          const notes = btn.getAttribute('data-notes');

          document.querySelector('#editMedicationId').value = id;
          document.querySelector('#editMedicationName').value = name;
          document.querySelector('#editDosage').value = dosage;
          document.querySelector('#editFrequency').value = frequency;
          document.querySelector('#editNoteContent').value = notes;
        });
      }

      const editMedicationForm = document.querySelector('#editMedicationForm');
      const editMedicationModalElement = document.getElementById('editMedicationModal');
      const editMedicationModal = editMedicationModalElement ? new bootstrap.Modal(editMedicationModalElement) : null;

      if (editMedicationForm) {
        editMedicationForm.addEventListener('submit', async function(ev) {
        ev.preventDefault();
        const formData = new FormData(editMedicationForm);
        const data = new URLSearchParams();
        for (const pair of formData) {
          data.append(pair[0], pair[1]);
        }

        try {
          const resp = await fetch(editMedicationForm.action, {
            method: 'POST',
            body: data,
          });

          let result;
          try {
            result = await resp.json();
          } catch (e) {
            result = { success: resp.ok };
          }

                      if (editMedicationModal) {
              editMedicationModal.hide();
            }

          if (result.success) {
            alert('Medication updated successfully');
            location.reload();
          } else {
            alert('Failed to update medication');
          }
        } catch (error) {
          console.error('error updating medication:');
          console.error(error);
          alert('Error updating medication');
        }
      });
    }

      const deleteButtons = document.querySelectorAll('.delete-medication-btn');
      for (const btn of deleteButtons) {
        btn.addEventListener('click', async () => {
          const id = btn.getAttribute('data-id');
          const name = btn.getAttribute('data-name');

          console.log('delete button clicked', id, name);

          if (confirm(`Are you sure you want to delete this medication?`)) {
            try {
              const formData = new URLSearchParams();
              formData.append('medicationId', id);

              const resp = await fetch('${pageContext.request.contextPath}/medication/delete', {
                method: 'POST',
                body: formData,
              });

              let result;
              try {
                result = await resp.json();
              } catch (e) {
                result = { success: resp.ok };
              }

              if (result.success) {
                alert('Medication deleted successfully');
                location.reload();
              } else {
                alert('Failed to delete medication');
              }
            } catch (error) {
              console.error('Error deleting medication:', error);
              alert('Error deleting medication');
            }
          }
        });
      }
    });

    // Handle remove patient buttons
    const removePatientButtons = document.querySelectorAll('.remove-patient-btn');
    for (const btn of removePatientButtons) {
      btn.addEventListener('click', async () => {
        const patientId = btn.getAttribute('data-patient-id');
        const patientName = btn.getAttribute('data-patient-name');

        if (confirm(`Are you sure you want to remove ${patientName} from your patient list?`)) {
          try {
            const formData = new URLSearchParams();
            formData.append('patientId', patientId);

            const resp = await fetch('${pageContext.request.contextPath}/remove-patient', {
              method: 'POST',
              body: formData,
            });

            let result;
            try {
              result = await resp.json();
            } catch (e) {
              result = { success: resp.ok };
            }

            if (result.success) {
              alert('Patient removed successfully');
              location.reload();
            } else {
              alert('Failed to remove patient: ' + (result.error || 'Unknown error'));
            }
          } catch (error) {
            console.error('Error removing patient:', error);
            alert('Error removing patient');
          }
        }
      });
    }
  </script>
  </body>
</html>
