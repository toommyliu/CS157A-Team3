<%@ page import="com.group_3.healthlink.User" %>
<%
  String requestUrl = request.getRequestURL().toString();
  String contextPath = request.getContextPath();
  String currentPath = requestUrl.substring(contextPath.length()).replace(".jsp", "");

  User user = (User) session.getAttribute("user");
  System.out.println("Current Path: " + currentPath);
%>
<div
  class="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark min-vh-100 sidebar"
>
  <a
    href="<%= contextPath %>/dashboard"
    class="d-flex align-items-center mb-2 text-white text-decoration-none"
  >
    <span class="fs-4">Healthlink</span>
  </a>
  <hr />
  <ul class="nav nav-pills flex-column mb-auto">
    <li>
      <a href="<%= contextPath %>" class="nav-link text-white <%= currentPath.contains("/dashboard") ? "active" : "" %>"> Dashboard </a>
    </li>
    <% if (user != null) { %>
        <% if (user.isPatient()) { %>
            <li>
              <a href="<%= contextPath %>/notes"
                 class="nav-link text-white <%= currentPath.contains("/notes") ? "active" : "" %>"> Notes </a>
            </li>
            <li>
              <a href="<%= contextPath %>/medications"
                 class="nav-link text-white <%= currentPath.contains("/medications") ? "active" : "" %>"> Medications </a>
          </li>
        <% } else if (user.isDoctor()) { %>
            <li>
              <a href="<%= contextPath %>/patients"
                 class="nav-link text-white <%= currentPath.contains("/patients") ? "active" : "" %>"> Patients </a>
            </li>
        <% } %>
    <% } %>
    <li>
      <a href="<%= contextPath %>/messages" class="nav-link text-white <%= currentPath.contains("/messages")  ? "active" : "" %>"> Messages </a>
    </li>
  </ul>
  <hr />
  <% if (user != null) { %>
      <p><%= user.getFullName() %></p>
  <% } %>
</div>
