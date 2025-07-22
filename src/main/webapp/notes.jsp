<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.services.PatientService" %>
<%@ page import="com.group_3.healthlink.Patient" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<html>
<head>
    <title>Healthlink - Notes</title>
    <link href="css/styles.css" rel="stylesheet"/>
</head>
<body>
    <div class="app-container">
        <jsp:include page="layouts/sidebar.jsp"/>

        <div class="main-content">
            <div class="d-flex">
                <div class="d-flex border-bottom gap-4 mb-2">
                    <h2 class="h2 fw-semibold">My Notes</h2>
                    <button class="btn btn-primary mb-2" data-bs-toggle="modal" data-bs-target="#noteModal">
                        <i class="bi bi-plus"></i>
                        New Note
                    </button>
                </div>
            </div>
        </div>
    </div>

    <% User user = (User)session.getAttribute("user"); %>
    <% Patient patient = PatientService.getByUserId(user.getUserId()); %>
    <% Doctor[] doctors = PatientService.getDoctors(patient.getPatientId()); %>

    <div class="modal fade" id="noteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title fs-5" id="exampleModalLabel">New note</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="<%= request.getContextPath() %>/notes/create" method="POST">
                    <div class="modal-body">
                        <textarea type="text" id="noteContent" name="noteContent" class="form-control mb-2" placeholder="Note content" required></textarea>
                        <% if (doctors.length > 0) { %>
                            <label for="doctor" class="form-label">For doctor</label>
                            <select class="form-select" name="doctorId" id="doctorId">
                                <option value="" selected disabled></option>
                                <% if (doctors.length > 0) { %>
                                    <% for (Doctor doctor : doctors) { %>
                                        <option value="<%= doctor.getUserId() %>">Dr. <%= doctor.getFullName() %> (<%= doctor.getDepartment() %>)</option>
                                    <% } %>
                                <% } %>
                            </select>
                        <% } %>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary">Create</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.min.js"></script>
</body>
</html>
