<%@ page import="com.group_3.healthlink.User" %>

<div
  class="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark min-vh-100 sidebar"
>
  <% User user = (User) session.getAttribute("user"); %>
  <a
    href="<%= request.getContextPath() %>/dashboard"
    class="d-flex align-items-center mb-2 text-white text-decoration-none"
  >
    <span class="fs-4">Healthlink</span>
  </a>
  <hr />
  <ul class="nav nav-pills flex-column mb-auto">
    <li>
      <a href="#" class="nav-link active text-white"> Dashboard </a>
    </li>
    <% if (user != null) { %>
        <% if (user.isPatient()) { %>
            <li>
              <a href="<%= request.getContextPath() %>/notes"
                 class="nav-link text-white"> Notes </a>
            </li>
            <li>
              <a href="<%= request.getContextPath() %>/medications"
                 class="nav-link text-white"> Medications </a>
          </li>
        <% } else if (user.isDoctor()) { %>
            <li>
              <a href="<%= request.getContextPath() %>/patients"
                 class="nav-link text-white"> Patients </a>
            </li>
        <% } %>
    <% } %>
    <li>
      <a href="#" class="nav-link text-white"> Messages </a>
    </li>
  </ul>
  <hr />
  <%
    if (user != null) { %>
      <p><%= user.getFullName() %></p>
  <% } %>
</div>
