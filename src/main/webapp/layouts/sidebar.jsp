<%@ page import="com.group_3.healthlink.User" %>
<%
  String requestUrl = request.getRequestURL().toString(); // http://localhost:8080/healthlink_war_exploded/notes.jsp
  String contextPath = request.getContextPath(); // /healthlink_war_exploded
  String currentPath = requestUrl
          .substring(requestUrl.indexOf(contextPath) + contextPath.length()) // /notes.jsp
          .replace(".jsp", ""); // /notes

  User user = (User) session.getAttribute("user");
  System.out.println("requestUrl: " + requestUrl);
  System.out.println("contextPath: " + contextPath);
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
