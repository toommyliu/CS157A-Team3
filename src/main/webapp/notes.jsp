<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.services.PatientService" %>
<%@ page import="com.group_3.healthlink.Patient" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Doctor" %>
<%@ page import="com.group_3.healthlink.Note" %>
<%@ page import="com.group_3.healthlink.services.NotesService" %>

<html>
  <head>
    <title>Healthlink - Notes</title>
    <link href="css/styles.css" rel="stylesheet" />
  </head>
  <body>
    <% User user = (User)session.getAttribute("user"); %>
    <% Patient patient = PatientService.getByUserId(user.getUserId()); %>
    <% Doctor[] doctors = PatientService.getDoctors(patient.getPatientId()); %>
    <% Note[] notes = NotesService.getNotesByPatientId(patient.getPatientId()); %>

    <div class="app-container">
      <jsp:include page="layouts/sidebar.jsp" />

      <div class="main-content">
        <div class="d-flex">
          <div class="d-flex border-bottom gap-4 mb-2">
            <h2 class="h2 fw-semibold">My Notes</h2>
            <button
              class="btn btn-primary mb-2"
              data-bs-toggle="modal"
              data-bs-target="#noteModal"
            >
              <i class="bi bi-plus"></i>
              New Note
            </button>
          </div>
        </div>

        <% if (notes.length > 0) { %>
        <div id="notesList">
          <% for (Note note: notes) { %>
          <div
            class="card mb-3 note-card"
            data-note-id="<%= note.getId() %>"
            data-note-content="<%= note.getContent() %>"
          >
            <div class="card-body">
              <p class="card-text"><%= note.getContent() %></p>
              <div class="card-footer">
                <p class="text-muted mb-0">
                  created at <%= note.getCreatedAt() %>
                </p>
                <button
                  class="btn btn-outline-primary btn-sm view-note-btn"
                  data-note-id="<%= note.getId() %>"
                  data-note-content="<%= note.getContent() %>"
                >
                  View/Edit
                </button>
              </div>
            </div>
          </div>
          <% } %>
        </div>
        <% } %>
      </div>
    </div>

    <div class="modal fade" id="noteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title fs-5">New note</h2>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <form
            action="<%= request.getContextPath() %>/notes/create"
            method="POST"
          >
            <div class="modal-body">
              <textarea
                type="text"
                id="noteContent"
                name="noteContent"
                class="form-control mb-2"
                placeholder="Note content"
                required
              ></textarea>
              <% if (doctors.length > 0) { %>
                <label for="doctor" class="form-label">For doctor</label>
                <select class="form-select mb-2" name="doctorId" id="doctorId">
                  <option value="" selected disabled></option>
                  <% if (doctors.length > 0) { %>
                    <% for (Doctor doctor : doctors) { %>
                    <option value="<%= doctor.getUserId() %>">
                      Dr. <%= doctor.getFullName() %> (<%= doctor.getDepartment() %>)
                  </option>
                  <% } %> 
                <% } %>
                </select>
              <% } %>
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

    <div class="modal fade" id="viewNoteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title fs-5">Note</h2>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <form id="editNoteForm">
            <div class="modal-body">
              <textarea
                id="editNoteContent"
                name="noteContent"
                class="form-control mb-2"
                required
              ></textarea>
              <input type="hidden" id="editNoteId" name="noteId" />
              <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="deleteNoteBtn">
                  Delete
                </button>
                <button type="submit" class="btn btn-primary">Update</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>

    <script src="js/bootstrap.min.js"></script>
    <script>
      document.addEventListener('DOMContentLoaded', () => {
        const form = document.querySelector('#noteModal form');
        const modal = new bootstrap.Modal('#noteModal');
        const viewNoteModal = new bootstrap.Modal(
          document.getElementById('viewNoteModal')
        );
        const editNoteForm = document.getElementById('editNoteForm');
        const deleteNoteBtn = document.getElementById('deleteNoteBtn');
        const notesList = document.getElementById('notesList');

        form.addEventListener('submit', async (ev) => {
          ev.preventDefault();
          const formData = new FormData(form);
          const data = new URLSearchParams();
          for (const pair of formData) {
            data.append(pair[0], pair[1]);
          }
          try {
            const resp = await fetch(form.action, {
              method: 'POST',
              body: data,
            });

            const result = await resp.json();
            modal.hide();

            alert(result.success ? 'new note created' : 'did not create note');
            if (result.success) {
              location.reload();
            }
          } catch (err) {
            console.error('error during note creation');
            console.error(err);
          }
        });

        notesList.addEventListener('click', (ev) => {
          if (ev.target.classList.contains('view-note-btn')) {
            const noteId = ev.target.getAttribute('data-note-id');
            const noteContent = ev.target.getAttribute('data-note-content');
            document.getElementById('editNoteId').value = noteId;
            document.getElementById('editNoteContent').value = noteContent;
            viewNoteModal.show();
          }
        });

        editNoteForm.addEventListener('submit', async (ev) => {
          ev.preventDefault();
          const formData = new FormData(editNoteForm);
          const data = new URLSearchParams();
          for (const pair of formData) {
            data.append(pair[0], pair[1]);
          }

          try {
            const resp = await fetch(
              '<%= request.getContextPath() %>/notes/update',
              {
                method: 'POST',
                body: data,
              }
            );

            const result = await resp.json();
            viewNoteModal.hide();

            alert(result.success ? 'note updated' : 'did not update note');
            if (result.success) {
              location.reload();
            }
          } catch (err) {
            console.error('error during note update');
            console.error(err);
          }
        });

        deleteNoteBtn.addEventListener('click', async () => {
          const noteId = document.getElementById('editNoteId').value;

          try {
            const resp = await fetch(
              '<%= request.getContextPath() %>/notes/delete',
              {
                method: 'POST',
                body: new URLSearchParams({ noteId }),
              }
            );

            const result = await resp.json();
            viewNoteModal.hide();

            alert(result.success ? 'note deleted' : 'did not delete note');
            if (result.success) {
              location.reload();
            }
          } catch (err) {
            console.error('error during note deletion');
            console.error(err);
          }
        });
      });
    </script>
  </body>
</html>
