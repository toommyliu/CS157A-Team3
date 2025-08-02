<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.group_3.healthlink.Medication" %>
<%@ page import="com.group_3.healthlink.MedicationLog" %>

<html>
<head>
  <title>Healthlink - Medications</title>
  <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
<div class="app-container">
    <jsp:include page="layouts/sidebar.jsp" />

    <div class="main-content">
      <div>
        <h2>Your Medications</h2>
        <%
          List<Medication> medications = (List<Medication>) request.getAttribute("medications");
          if (medications != null && !medications.isEmpty()) {
        %>
          <table class="table table-striped">
            <thead>
              <tr>
                <th>Name</th>
                <th>Dosage</th>
                <th>Frequency</th>
                <th>Notes</th>
                <th>Prescribed By</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <% Map<Integer, String> doctorNames = (Map<Integer, String>) request.getAttribute("doctorNames"); %>
              <% for (Medication med : medications) { %>
                <tr>
                  <td><%= med.getName() %></td>
                  <td><%= med.getDosage() %></td>
                  <td><%= med.getFrequency() %></td>
                  <td><%= med.getNotes() != null ? med.getNotes() : "" %></td>
                  <td><%= doctorNames != null ? "Dr. " + doctorNames.get(med.getDoctorId()) : "Unknown" %></td>
                  <td>
                    <button type="button" class="btn btn-outline-primary btn-sm" title="Make log entry" data-bs-toggle="modal" data-bs-target="#logMedicationModal"
                      data-medname="<%= med.getName() %>"
                      data-dosage="<%= med.getDosage() %>"
                      data-medid="<%= med.getId() %>">
                      <i class="bi bi-journal-plus"></i>
                    </button>
                  </td>
                </tr>
                <% } %>
            </tbody>
          </table>
        <% } else { %>
          <p>No medications found.</p>
        <% } %>
      </div>
      <div class="overflow-y-scroll">
          <h3>Your Medication Log History</h3>
          <%
            List<MedicationLog> medicationLogs = (List<MedicationLog>) request.getAttribute("medicationLogs");
            Map<Integer, String> doctorNames = (Map<Integer, String>) request.getAttribute("doctorNames");
            Map<Integer, Medication> medicationMap = (Map<Integer, Medication>) request.getAttribute("medicationMap");
            if (medicationLogs != null && !medicationLogs.isEmpty()) {
           %>
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Date/Time Taken</th>
                  <th>Medication Name</th>
                  <th>Dosage Taken</th>
                  <th>Note</th>
                  <th>Prescribed By</th>
                </tr>
              </thead>
              <tbody>
                <% for (MedicationLog log : medicationLogs) {
                    Medication med = medicationMap != null ? medicationMap.get(log.getMedicationId()) : null;
                %>
                  <tr>
                    <td><%= log.getTakenAt() %></td>
                    <td><%= med != null ? med.getName() : "Unknown" %></td>
                    <td><%= log.getDosageTaken() %></td>
                    <td><%= log.getNote() != null ? log.getNote() : "" %></td>
                    <td><%= doctorNames != null && med != null ? "Dr. " + doctorNames.get(med.getDoctorId()) : "Unknown" %></td>
                  </tr>
                <% } %>
              </tbody>
            </table>
        <% } else { %>
          <p>No medication log history found.</p>
        <% } %>
      </div>
    </div>
  </div>

  <div class="modal fade" id="logMedicationModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <form action="<%= request.getContextPath() %>/medication-log" method="post">
          <div class="modal-header">
            <h5 class="modal-title" id="logMedicationModalLabel">Log Medication</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <input type="hidden" name="medicationId" id="modalMedicationId" />
            <input type="hidden" name="patientId" value="<%= request.getAttribute("patientId") %>" />

            <div class="mb-3">
              <label class="form-label">Medication Name</label>
              <input type="text" class="form-control" id="modalMedicationName" name="medicationName" readonly disabled />
            </div>
            <div class="mb-3">
              <label class="form-label">Dosage</label>
              <input type="text" class="form-control" id="modalDosage" name="dosage" disabled readonly />
            </div>
            <div class="mb-3">
              <label class="form-label">Dosage Taken</label>
              <input type="text" class="form-control" name="dosageTaken" required />
            </div>
            <div class="mb-3">
              <label class="form-label">Note</label>
              <textarea class="form-control" name="note"></textarea>
            </div>
            <div class="mb-3">
              <label class="form-label">Taken At</label>
              <input type="datetime-local" class="form-control" name="takenAt" step="any" required />
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="submit" class="btn btn-primary">Save Log</button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <script src="js/bootstrap.min.js"></script>
  <script>
    document.addEventListener('DOMContentLoaded', () => {
      const logMedicationModal = document.querySelector('#logMedicationModal');
      logMedicationModal.addEventListener('show.bs.modal', (ev) => {
        const button = ev.relatedTarget;
        const medName = button.getAttribute('data-medname');
        const dosage = button.getAttribute('data-dosage');
        const medId = button.getAttribute('data-medid');
        document.querySelector('#modalMedicationName').value = medName || '';
        document.querySelector('#modalDosage').value = dosage || '';
        document.querySelector('#modalMedicationId').value = medId || '';

        const now = new Date();
        const pad = n => n.toString().padStart(2, '0');
        const formatted = now.getFullYear() + '-' + pad(now.getMonth() + 1) + '-' + pad(now.getDate()) + 'T' + pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds());
        document.querySelector('input[name="takenAt"]').value = formatted;
      });
    });
  </script>
</body>
</html>
