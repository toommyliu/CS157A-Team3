<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.UserRole" %>
<%@ page import="com.group_3.healthlink.TestResult" %>
<%@ page import="com.group_3.healthlink.services.AssignmentService" %>
<%@ page import="com.group_3.healthlink.services.PatientService" %>
<%@ page import="com.group_3.healthlink.services.TestResultService" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    UserRole userRole = (UserRole) request.getAttribute("userRole");
    List<TestResult> testResults = (List<TestResult>) request.getAttribute("testResults");
    List<TestResultService.PatientTestSummary> patientSummaries = (List<TestResultService.PatientTestSummary>) request.getAttribute("patientSummaries");
    Integer selectedPatientId = (Integer) request.getAttribute("selectedPatientId");
    Integer selectedDoctorId = (Integer) request.getAttribute("selectedDoctorId");
    String error = (String) request.getAttribute("error");
%>

<html>
<head>
    <title>Test Results - HealthLink</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <link href="css/styles.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet" />
</head>
<body>
    <div class="app-container">
        <jsp:include page="layouts/sidebar.jsp" />
        
        <div class="main-content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <h2 class="h2 fw-semibold mb-4">
                            <i class="bi bi-file-earmark-medical"></i> Test Results
                        </h2>
                        
                        <% if (error != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <i class="bi bi-exclamation-triangle"></i> <%= error %>
                            </div>
                        <% } %>
                        
                        <% if (userRole == UserRole.Patient) { %>
                            <!-- Upload Form for Patients -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="bi bi-cloud-upload"></i> Upload New Test Result
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form id="uploadForm" enctype="multipart/form-data">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="doctorId" class="form-label">Select Doctor</label>
                                                    <select class="form-select" id="doctorId" name="doctorId" required>
                                                        <option value="">Choose a doctor...</option>
                                                        <% 
                                                        // Get patient ID from user ID
                                                        int patientId = PatientService.getByUserId(user.getUserId()).getPatientId();
                                                        List<Integer> assignedDoctorIds = AssignmentService.getAssignedDoctorIds(patientId);
                                                        for (Integer doctorId : assignedDoctorIds) {
                                                            // Get doctor name
                                                            com.group_3.healthlink.Doctor doctor = com.group_3.healthlink.services.DoctorService.getByDoctorId(doctorId);
                                                            if (doctor != null) {
                                                                com.group_3.healthlink.User doctorUser = com.group_3.healthlink.services.AuthService.getUserById(doctor.getUserId());
                                                                String doctorName = doctorUser != null ? doctorUser.getFullName() : "Unknown Doctor";
                                                        %>
                                                            <option value="<%= doctorId %>"><%= doctorName %></option>
                                                        <% 
                                                            }
                                                        } 
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="file" class="form-label">Test Result File</label>
                                                    <input type="file" class="form-control" id="file" name="file" 
                                                           accept=".pdf,.jpg,.jpeg,.png,.gif" required>
                                                    <div class="form-text">Accepted formats: PDF, JPG, JPEG, PNG, GIF (Max 10MB)</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="description" class="form-label">Description</label>
                                            <textarea class="form-control" id="description" name="description" 
                                                      rows="3" placeholder="Describe the test result..."></textarea>
                                        </div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="bi bi-upload"></i> Upload Test Result
                                        </button>
                                    </form>
                                </div>
                            </div>
                        <% } %>
                        
                        <% if (userRole == UserRole.Patient) { 
                            if (selectedDoctorId == null) { 
                        %>
                            <!-- Doctor Cards View for Patients -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="bi bi-person-vcard"></i> Test Results
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <% 
                                    // Get patient ID and assigned doctors
                                    int patientId = PatientService.getByUserId(user.getUserId()).getPatientId();
                                    List<Integer> assignedDoctorIds = AssignmentService.getAssignedDoctorIds(patientId);
                                    boolean hasDoctors = false;
                                    %>
                                    <div class="row">
                                        <% for (Integer doctorId : assignedDoctorIds) { 
                                            com.group_3.healthlink.Doctor doctor = DoctorService.getByDoctorId(doctorId);
                                            if (doctor != null) {
                                                com.group_3.healthlink.User doctorUser = com.group_3.healthlink.services.AuthService.getUserById(doctor.getUserId());
                                                if (doctorUser != null) {
                                                    hasDoctors = true;
                                                    // Get test results count for this doctor
                                                    List<TestResult> doctorResults = TestResultService.getTestResultsByPatientAndDoctor(patientId, doctorId);
                                                    int fileCount = doctorResults != null ? doctorResults.size() : 0;
                                        %>
                                            <div class="col-md-4 mb-3">
                                                <div class="card h-100 doctor-card" style="cursor: pointer;" 
                                                     onclick="showDoctorResults(<%= doctorId %>, '<%= doctorUser.getFullName() %>')">
                                                    <div class="card-body text-center">
                                                        <div class="mb-3">
                                                            <i class="bi bi-person-vcard" style="font-size: 3rem; color: #6c757d;"></i>
                                                        </div>
                                                        <h6 class="card-title"><%= doctorUser.getFullName() %></h6>
                                                        <div class="d-flex justify-content-center align-items-center">
                                                            <span class="badge bg-<%= fileCount > 0 ? "primary" : "secondary" %> me-2">
                                                                <i class="bi bi-file-earmark-medical"></i>
                                                                <%= fileCount %> file<%= fileCount != 1 ? "s" : "" %>
                                                            </span>
                                                        </div>
                                                        <small class="text-muted">Click to view test results</small>
                                                    </div>
                                                </div>
                                            </div>
                                        <% 
                                                }
                                            }
                                        } 
                                        %>
                                    </div>
                                    <% if (!hasDoctors) { %>
                                        <div class="text-center py-4">
                                            <i class="bi bi-person-vcard text-muted" style="font-size: 3rem;"></i>
                                            <h6 class="text-muted mt-3">No doctors assigned</h6>
                                            <p class="text-muted small">You need to be assigned to doctors to view their test results.</p>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                        <% } %>
                        
                        <!-- Test Results List -->
                        <% if (userRole != UserRole.Patient || selectedDoctorId != null) { %>
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="card-title mb-0">
                                    <i class="bi bi-list-ul"></i> 
                                    <% if (userRole == UserRole.Patient) { %>
                                        Test Results
                                    <% } else { %>
                                        <% if (selectedPatientId != null) { %>
                                            Patient Test Results
                                        <% } else { %>
                                            Patient Test Results
                                        <% } %>
                                    <% } %>
                                </h5>
                                <% if (userRole == UserRole.Patient && selectedDoctorId != null) { %>
                                    <a href="${pageContext.request.contextPath}/test-results" class="btn btn-primary btn-sm">
                                        <i class="bi bi-arrow-left"></i> Back
                                    </a>
                                <% } else if (userRole == UserRole.Doctor && selectedPatientId != null) { %>
                                    <a href="${pageContext.request.contextPath}/test-results" class="btn btn-primary btn-sm">
                                        <i class="bi bi-arrow-left"></i> Back to All Patients
                                    </a>
                                <% } %>
                            </div>
                            <div class="card-body">
                                <% if (userRole == UserRole.Doctor && selectedPatientId == null && patientSummaries != null && !patientSummaries.isEmpty()) { %>
                                    <!-- Patient Cards View for Doctors -->
                                    <div class="row">
                                        <% for (TestResultService.PatientTestSummary summary : patientSummaries) { %>
                                            <div class="col-md-4 mb-3">
                                                <div class="card h-100 patient-card" style="cursor: pointer;" 
                                                     onclick="window.location.href='${pageContext.request.contextPath}/test-results?patientId=<%= summary.getPatientId() %>'">
                                                    <div class="card-body text-center">
                                                        <div class="mb-3">
                                                            <i class="bi bi-person-circle" style="font-size: 3rem; color: #6c757d;"></i>
                                                        </div>
                                                        <h6 class="card-title"><%= summary.getPatientName() %></h6>
                                                        <div class="d-flex justify-content-center align-items-center">
                                                            <span class="badge bg-<%= summary.getFileCount() > 0 ? "primary" : "secondary" %> me-2">
                                                                <i class="bi bi-file-earmark-medical"></i>
                                                                <%= summary.getFileCount() %> file<%= summary.getFileCount() != 1 ? "s" : "" %>
                                                            </span>
                                                        </div>
                                                        <small class="text-muted">Click to view test results</small>
                                                    </div>
                                                </div>
                                            </div>
                                        <% } %>
                                    </div>
                                <% } else if (testResults != null && !testResults.isEmpty()) { %>
                                    <div class="table-responsive">
                                        <table class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th>File Name</th>
                                                    <th>Type</th>
                                                    <% if (userRole == UserRole.Doctor) { %>
                                                        <th>Patient</th>
                                                    <% } else { %>
                                                        <th>Doctor</th>
                                                    <% } %>
                                                    <th>Description</th>
                                                    <th>Upload Date</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (TestResult result : testResults) { %>
                                                    <tr>
                                                        <td>
                                                            <i class="bi bi-file-earmark-<%= result.isPdf() ? "pdf" : "image" %>"></i>
                                                            <%= result.getFileName() %>
                                                        </td>
                                                        <td>
                                                            <span class="badge bg-<%= result.isPdf() ? "danger" : "info" %>">
                                                                <%= result.getFileType().toUpperCase() %>
                                                            </span>
                                                        </td>
                                                        <% if (userRole == UserRole.Doctor) { %>
                                                            <td><%= result.getPatientName() %></td>
                                                        <% } else { %>
                                                            <td><%= result.getDoctorName() %></td>
                                                        <% } %>
                                                        <td>
                                                            <% if (result.getDescription() != null && !result.getDescription().isEmpty()) { %>
                                                                <%= result.getDescription() %>
                                                            <% } else { %>
                                                                <span class="text-muted">No description</span>
                                                            <% } %>
                                                        </td>
                                                        <td><%= result.getUploadDate() %></td>
                                                        <td>
                                                            <div class="btn-group" role="group">
                                                                <a href="${pageContext.request.contextPath}/test-results/download/<%= result.getResultId() %>" 
                                                                   class="btn btn-outline-primary btn-sm">
                                                                    <i class="bi bi-download"></i> Download
                                                                </a>
                                                                <button type="button" class="btn btn-outline-danger btn-sm delete-btn"
                                                                        data-result-id="<%= result.getResultId() %>"
                                                                        data-file-name="<%= result.getFileName() %>">
                                                                    <i class="bi bi-trash"></i> Delete
                                                                </button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                <% } else { %>
                                    <div class="text-center py-4">
                                        <i class="bi bi-file-earmark-medical text-muted" style="font-size: 3rem;"></i>
                                        <h5 class="text-muted mt-3">No test results found</h5>
                                        <% if (userRole == UserRole.Patient) { %>
                                            <p class="text-muted">Upload your first test result using the form above.</p>
                                        <% } else { %>
                                            <% if (selectedPatientId != null) { %>
                                                <p class="text-muted">This patient hasn't uploaded any test results yet.</p>
                                            <% } else { %>
                                                <p class="text-muted">Your patients haven't uploaded any test results yet.</p>
                                            <% } %>
                                        <% } %>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <style>
        .patient-card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .patient-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
    <script src="js/bootstrap.min.js"></script>
    <script>
        function showDoctorResults(doctorId, doctorName) {
            console.log('showDoctorResults called with:', doctorId, doctorName);
            // Navigate to the test results page with the specific doctor
            const url = '${pageContext.request.contextPath}/test-results?doctorId=' + doctorId;
            console.log('Navigating to:', url);
            window.location.href = url;
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            // Handle file upload form
            const uploadForm = document.getElementById('uploadForm');
            if (uploadForm) {
                uploadForm.addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const formData = new FormData(uploadForm);
                    const submitBtn = uploadForm.querySelector('button[type="submit"]');
                    const originalText = submitBtn.innerHTML;
                    
                    try {
                        submitBtn.disabled = true;
                        submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Uploading...';
                        
                        const response = await fetch('${pageContext.request.contextPath}/test-results/upload', {
                            method: 'POST',
                            body: formData
                        });
                        
                        if (response.ok) {
                            alert('Test result uploaded successfully!');
                            location.reload();
                        } else {
                            const errorText = await response.text();
                            alert('Upload failed: ' + errorText);
                        }
                    } catch (error) {
                        console.error('Upload error:', error);
                        alert('Upload failed: ' + error.message);
                    } finally {
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = originalText;
                    }
                });
            }
            
            // Handle delete buttons
            const deleteButtons = document.querySelectorAll('.delete-btn');
            deleteButtons.forEach(btn => {
                btn.addEventListener('click', async function() {
                    const resultId = this.getAttribute('data-result-id');
                    const fileName = this.getAttribute('data-file-name');
                    
                    if (confirm(`Are you sure you want to delete "${fileName}"?`)) {
                        try {
                            const formData = new URLSearchParams();
                            formData.append('resultId', resultId);
                            
                            const response = await fetch('${pageContext.request.contextPath}/test-results/delete', {
                                method: 'POST',
                                body: formData
                            });
                            
                            if (response.ok) {
                                alert('Test result deleted successfully!');
                                location.reload();
                            } else {
                                const errorText = await response.text();
                                alert('Delete failed: ' + errorText);
                            }
                        } catch (error) {
                            console.error('Delete error:', error);
                            alert('Delete failed: ' + error.message);
                        }
                    }
                });
            });
        });
    </script>
</body>
</html> 