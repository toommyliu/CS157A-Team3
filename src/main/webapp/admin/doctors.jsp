<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<html>
<head>
    <title>Healthlink - Doctors</title>
    <link href="../css/styles.css" rel="stylesheet"/>
</head>
<body>
    <div class="app-container">
        <jsp:include page="../layouts/sidebar.jsp"/>

        <div class="main-content">
            <div class="d-flex flex-column">
                <div class="d-flex flex-column flex-sm-row gap-2">
                    <h2 class="h2 fw-semibold">Doctors</h2>
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#doctorModal">
                        Add New Doctor
                    </button>
                </div>
            </div>

            <% Doctor[] doctors = (Doctor[]) request.getAttribute("doctors"); %>
            <% if (doctors != null && doctors.length > 0) { %>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Doctor ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Department</th>
                        <th>Created At</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Doctor doctor : doctors) { %>
                    <tr>
                        <td><%= doctor.getDoctorId() %>
                        </td>
                        <td><%= doctor.getFirstName() %>
                        </td>
                        <td><%= doctor.getLastName() %>
                        </td>
                        <td><%= doctor.getEmailAddress() %>
                        </td>
                        <td><%= doctor.getDepartment() %>
                        </td>
                        <td><%= doctor.getCreatedAt() %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>

            <div class="modal" id="doctorModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 600px;">
                        <div class="modal-header">
                            <h2 class="modal-title fs-5">Add New Doctor</h2>
                            <button
                                    type="button"
                                    class="btn-close"
                                    data-bs-dismiss="modal"
                                    aria-label="Close"
                            ></button>
                        </div>
                        <form
                                action="<%= request.getContextPath() %>/admin/doctors"
                                method="POST"
                                id="doctorForm"
                        >
                            <div class="modal-body">
                                <div class="d-flex flex-row gap-4 w-100 mb-3">
                                    <div class="d-flex flex-column w-50">
                                        <label for="first-name" class="form-label">First Name</label>
                                        <input
                                                id="first-name"
                                                type="text"
                                                name="firstName"
                                                class="form-control"
                                                placeholder="Enter the doctor's first name"
                                                required
                                        />
                                    </div>

                                    <div class="d-flex flex-column w-50">
                                        <label for="last-name" class="form-label">Last Name</label>
                                        <input
                                                id="last-name"
                                                type="text"
                                                name="lastName"
                                                class="form-control"
                                                placeholder="Enter the doctor's last name"
                                                required
                                        />
                                    </div>
                                </div>

                                <div class="d-flex flex-column mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input
                                            id="email"
                                            type="email"
                                            name="email"
                                            class="form-control"
                                            placeholder="Enter the doctor's email"
                                            required
                                    />
                                </div>

                                <div class="d-flex flex-column mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input
                                            id="password"
                                            type="password"
                                            name="password"
                                            class="form-control"
                                            placeholder="Enter the doctor's password"
                                            required
                                    />
                                </div>

                                <div class="d-flex flex-column mb-3">
                                    <label for="department" class="form-label">Department</label>
                                    <input
                                            id="text"
                                            type="text"
                                            name="department"
                                            class="form-control"
                                            placeholder="Enter the doctor's department"
                                            required
                                    />
                                </div>

                                <div class="modal-footer">
                                    <button
                                            type="button"
                                            class="btn btn-secondary"
                                            data-bs-dismiss="modal"
                                    >
                                        Cancel
                                    </button>
                                    <button type="submit" class="btn btn-primary">Create</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="../js/bootstrap.min.js"></script>
</body>
</html>
