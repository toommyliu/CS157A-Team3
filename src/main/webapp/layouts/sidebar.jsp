<%@ page import="com.group_3.healthlink.User" %>
<%
    String requestUrl = request.getRequestURL().toString(); // http://localhost:8080/healthlink_war_exploded/notes.jsp
    String contextPath = request.getContextPath(); // /healthlink_war_exploded
    String currentPath = requestUrl
            .substring(requestUrl.indexOf(contextPath) + contextPath.length()) // /notes.jsp
            .replace(".jsp", ""); // /notes

    User user = (User) session.getAttribute("user");
%>
<div
        class="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark min-vh-100 sidebar"
        style="display: flex !important; flex-direction: column !important;"
>
    <a
            href="<%= contextPath %>/dashboard"
            class="d-flex align-items-center mb-2 text-white text-decoration-none"
    >
        <span class="fs-4">Healthlink</span>
    </a>
    <hr/>
    <ul class="nav nav-pills flex-column mb-auto">
        <li>
            <a href="<%= contextPath %>"
               class="nav-link text-white <%= currentPath.contains("/dashboard") ? "active" : "" %>">
                <i class="bi bi-house me-2"></i> Dashboard
            </a>
        </li>
        <% if (user != null) { %>
            <% if (user.isPatient()) { %>
                <li>
                    <a href="<%= contextPath %>/doctors"
                       class="nav-link text-white <%= currentPath.contains("/doctors") ? "active" : "" %>">
                        <i class="bi bi-person-vcard me-2"></i>
                        My Doctors
                    </a>
                </li>
                <li>
                    <a href="<%= contextPath %>/notes"
                       class="nav-link text-white <%= currentPath.contains("/notes") ? "active" : "" %>">
                        <i class="bi bi-journal-text me-2"></i>
                        Notes
                    </a>
                </li>
                <li>
                    <a href="<%= contextPath %>/medications"
                       class="nav-link text-white <%= currentPath.contains("/medications") ? "active" : "" %>">
                        <i class="bi bi-capsule-pill me-2"></i>
                        Medications
                    </a>
                </li>
                <li>
                    <a href="<%= contextPath %>/test-results"
                       class="nav-link text-white <%= currentPath.contains("/test-results") ? "active" : "" %>">
                        <i class="bi bi-file-earmark-medical me-2"></i>
                        Test Results
                    </a>
                </li>
            <% } else if (user.isDoctor()) { %>
                <li>
                    <a href="<%= contextPath %>/patients"
                       class="nav-link text-white <%= currentPath.contains("/patients") ? "active" : "" %>">
                        <i class="bi bi-person me-2"></i>
                        Patients
                    </a>
                </li>
            <% } %>
        <% } %>
        <li>
            <a href="<%= contextPath %>/messages"
               class="nav-link text-white <%= currentPath.contains("/messages")  ? "active" : "" %>">
                <i class="bi bi-chat-dots me-2"></i> Messages
            </a>
        </li>
        <% if (user.isAdmin()) { %>
            <div class="border-top my-2"></div>

            <li>
                <a href="<%= contextPath %>/admin/doctors"
                   class="nav-link text-white <%= currentPath.contains("/admin/doctors") ? "active" : "" %>">
                    <i class="bi bi-person-vcard me-2"></i>
                    Doctors
                </a>
            </li>
        <% } %>
    </ul>
    <% if (user != null) { %>
        <div class="mt-auto" style="margin-top: auto !important;">
            <a href="<%= contextPath %>/profile" 
               class="d-flex align-items-center text-white text-decoration-none p-3 rounded"
               style="transition: background-color 0.2s ease;"
               onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'"
               onmouseout="this.style.backgroundColor='transparent'">
                <i class="bi bi-person-circle me-3" style="font-size: 2rem; color: #fff;"></i>
                <div class="d-flex flex-column">
                    <span class="fw-semibold text-white"><%= user.getFullName() %></span>
                    <small class="text-light opacity-75"><%= user.getRole() %></small>
                </div>
            </a>
        </div>
    <% } %>
</div>
