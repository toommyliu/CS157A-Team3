<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Healthlink - Onboarding</title>
    <link href="css/styles.css" rel="stylesheet"/>
</head>
<body>
    <div>
        <div class="p-4">
            <h2 class="h2 fw-semibold">🎉 Welcome to HealthLink.</h2>
            <h5 class="text-muted">
                We are excited to work together with you! Please fill out your information before proceeding.
            </h5>
        </div>
        
        <% if (request.getAttribute("error") != null && (Boolean) request.getAttribute("error")) { %>
            <div class="alert alert-danger mx-auto w-50 text-center" role="alert">
                An error occurred, try again.
            </div>
        <% } %>
        
        <div class="d-flex justify-content-center">
            <form
                id="onboarding-form"
                method="POST"
                action="<%= request.getContextPath() %>/onboarding"
                class="p-4 m-0 bg-light-subtle rounded shadow w-50"
            >
                <div class="mb-3">
                    <label for="dateOfBirth" class="form-label">Date of Birth</label>
                    <input
                            type="date"
                            id="dateOfBirth"
                            name="dateOfBirth"
                            class="form-control"
                            required
                    />
                </div>
                <div class="mb-3">
                    <label for="phoneNumber" class="form-label">Phone Number</label>
                    <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            class="form-control"
                            required
                            pattern="^\(\d{3}\) \d{3}-\d{4}$"
                            placeholder="(408) 456-7890"
                    />
                </div>
                <div class="mb-3">
                    <label for="address" class="form-label">Address</label>
                    <input
                            type="text"
                            id="address"
                            name="address"
                            class="form-control"
                            required
                    />
                </div>
                <div class="mb-3">
                    <label for="emergencyContactName" class="form-label">Emergency Contact Name</label>
                    <input
                            type="text"
                            id="emergencyContactName"
                            name="emergencyContactName"
                            class="form-control"
                            required
                    />
                </div>
                <div class="mb-3">
                    <label for="emergencyContactPhone" class="form-label">Emergency Contact Phone Number</label>
                    <input
                            type="tel"
                            id="emergencyContactPhone"
                            name="emergencyContactPhone"
                            class="form-control"
                            required
                            pattern="^\(\d{3}\) \d{3}-\d{4}$"
                            placeholder="(408) 456-7890"
                    />
                </div>
                <button type="submit" class="btn btn-primary w-100">Submit</button>
            </form>
        </div>
    </div>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
