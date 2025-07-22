<%@ page import="com.group_3.healthlink.User" %> <%@ page
import="com.group_3.healthlink.services.DoctorService" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Healthlink - Dashboard</title>
    <link href="css/styles.css" rel="stylesheet" />
  </head>
  <body>
    <%
      User user = (User) session.getAttribute("user");
      User[] patients = null;
      if (user != null && user.isDoctor()) {
        patients = DoctorService.getPatients(user.getUserId());
      }
    %>

    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <p>hello from dashboard!</p>
        <p>it seems you are logged in.</p>
        <h3>User Details:</h3>
        <ul>
          <li>ID: <%= user.getUserId() %></li>
          <li>Email: <%= user.getEmailAddress() %></li>
          <li>First Name: <%= user.getFirstName() %></li>
          <li>Last Name: <%= user.getLastName() %></li>
          <li>Role: <%= user.getRole() %></li>
          <li>Created At: <%= user.getCreatedAt() %></li>
          <li>Updated At: <%= user.getUpdatedAt() %></li>
        </ul>

        <% if (user.isDoctor()) { %>
        <div class="mt-4">
          <h3>Your Patients:</h3>
          <% if (patients != null && patients.length > 0) { %>
            <div class="table-responsive">
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th>Patient ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Created At</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <% for (User patient : patients) { %>
                  <tr>
                    <td><%= patient.getUserId() %></td>
                    <td><%= patient.getFullName() %></td>
                    <td><%= patient.getEmailAddress() %></td>
                    <td><%= patient.getCreatedAt() %></td>
                    <td>
                      <button
                        class="btn btn-primary medicationBtn"
                        data-bs-toggle="modal"
                        data-bs-target="#medicationModal"
                        data-patient-id="<%= patient.getUserId() %>"
                      >
                        <i class="bi bi-prescription2"></i>
                      </button>
                    </td>
                  </tr>
                  <% } %>
                </tbody>
              </table>
            </div>
            <% } else { %>
              <p class="text-muted">No patients assigned yet.</p>
            <% } %>
          </div>
        <% } %>

        <div class="mt-4">
          <a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">
            Logout
          </a>
        </div>
      </div>
    </div>

    <% if (user.isDoctor() && patients != null && patients.length > 0) { %>
      <div class="modal fade" id="medicationModal" tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h2 class="modal-title fs-5">New Medication</h2>
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <form
              action="<%= request.getContextPath() %>/medication/create"
              method="POST"
              id="medicationForm"
            >
              <div class="modal-body">
                <div class="mb-2">
                  <label for="patientId" class="form-label">Patient</label>
                  <select
                    id="patientId"
                    name="patientId"
                    class="form-select"
                    required
                  >
                    <% for (User patient : patients) { %>
                      <option value="<%= patient.getUserId() %>">
                        <%= patient.getFullName() %> (<%= patient.getEmailAddress()%>)
                      </option>
                    <% } %>
                  </select>
                </div>
                <div class="mb-2">
                  <label for="medicationName" class="form-label"
                    >Medication Name</label
                  >
                  <input
                    type="text"
                    id="medicationName"
                    name="medicationName"
                    class="form-control"
                    placeholder="Medication name"
                    required
                  />
                </div>
                <div class="mb-2">
                  <label for="dosage" class="form-label">Dosage</label>
                  <input
                    type="text"
                    id="dosage"
                    name="dosage"
                    class="form-control"
                    placeholder="Dosage"
                    required
                  />
                </div>
                <div class="mb-2">
                  <label for="frequency" class="form-label">Frequency</label>
                  <input
                    type="text"
                    id="frequency"
                    name="frequency"
                    class="form-control"
                    placeholder="Frequency"
                    required
                  />
                </div>
                <div class="mb-2">
                  <label for="noteContent" class="form-label"
                    >Notes (optional)</label
                  >
                  <textarea
                    id="noteContent"
                    name="noteContent"
                    class="form-control"
                    placeholder="Note content (optional)"
                  ></textarea>
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
    <% } %>

    <script src="js/bootstrap.min.js"></script>
    <script>
      document.addEventListener('DOMContentLoaded', () => {
        for (const btn of document.querySelectorAll('.medicationBtn')) {
          btn.addEventListener('click', () => {
            const patientId = btn.getAttribute('data-patient-id');
            const select = document.getElementById('patientId');
            if (select) {
              select.value = patientId;
            }
          });
        }

        const medicationForm = document.querySelector('#medicationForm');
        const medicationModalEl = document.querySelector('#medicationModal');

        const medicationModal = new bootstrap.Modal(medicationModalEl);
        medicationForm.addEventListener('submit', async (ev) => {
          ev.preventDefault();
          const formData = new FormData(medicationForm);
          const data = new URLSearchParams();
          for (const pair of formData) {
            data.append(pair[0], pair[1]);
          }
          try {
            const resp = await fetch(medicationForm.action, {
              method: 'POST',
              body: data,
            });
            let result;
            try {
              result = await resp.json();
            } catch (e) {
              result = { success: resp.ok };
            }
            medicationModal.hide();
            alert(
              result.success
                ? 'New medication created'
                : 'Did not create medication'
            );
          } catch (err) {
            console.error('error during medication creation');
            console.error(err);
          }
        });
      });
    </script>
  </body>
</html>
