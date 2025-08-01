<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Note" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<%@ page import="com.group_3.healthlink.services.DoctorService" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");

    if (user == null || !"doctor".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("login.jsp"); // or error page
        return;
    }

    Doctor doctor = DoctorService.getByUserId(user.getUserId());
    if (doctor == null) {
        out.println("Doctor record not found.");
        return;
    }

    List<Note> notes = DoctorService.getNotesByDoctorId(doctor.getDoctorId());
%>

<html>
<head>
    <title>Doctor Notes</title>
    <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
    <div class="app-container">
        <jsp:include page="layouts/sidebar.jsp" />

        <div class="main-content">
            <h2 class="h2 fw-semibold">Notes Written By Me</h2>

            <% if (notes == null || notes.isEmpty()) { %>
                <p>No notes found.</p>
            <% } else { %>
                <p>Total notes: <%= notes.size() %></p>
                <ul>
                    <% for (Note note : notes) { %>
                        <li>
                            <strong>Patient ID:</strong> <%= note.getPatientId() %><br/>
                            <strong>Note:</strong> <%= note.getContent() %><br/>
                            <strong>Created At:</strong> <%= note.getTimestamp() %>
                            <hr/>
                        </li>
                    <% } %>
                </ul>
            <% } %>

            <div style="margin-top: 20px;">
                <a href="dashboard.jsp">← Back to Dashboard</a> |
                <a href="add-note.jsp">+ Write New Note</a> <!-- Optional if you have note form -->
            </div>
        </div>
    </div>
</body>
</html>
