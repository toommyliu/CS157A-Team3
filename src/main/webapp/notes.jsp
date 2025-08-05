<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.Note" %>
<%@ page import="com.group_3.healthlink.services.NotesService" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
  <head>
    <title>Healthlink - Notes</title>
    <link href="css/styles.css" rel="stylesheet" />
  </head>
  <body>
    <% User user = (User)session.getAttribute("user"); %> <% Note[] notes =
    NotesService.getUserNotes(user.getUserId()); %>

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
        <div id="notesList" class="row">
          <% for (Note note: notes) { %>
            <% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); %>
            <div class="col-md-4 mb-4">
              <div
                class="card h-100 note-card d-flex flex-column justify-content-between"
                data-note-id="<%= note.getId() %>"
                data-note-content="<%= note.getContent() %>"
              >
                <div class="card-body">
                  <h5 class="card-title text-secondary">#<%= note.getId() %></h5>
                  <p class="card-text"><%= note.getContent() %></p>
                </div>
                <div
                  class="card-footer d-flex justify-content-between align-items-center"
                >
                  <small class="text-muted">
                    <%= sdf.format(note.getTimestamp()) %>
                  </small>
                  <div class="d-flex gap-2">
                    <button
                      class="btn btn-outline-primary btn-sm edit-note-btn"
                      data-note-id="<%= note.getId() %>"
                      data-note-content="<%= note.getContent() %>"
                      data-note-timestamp="<%= sdf.format(note.getTimestamp()) %>"
                    >
                      Edit
                    </button>
                    <button
                      class="btn btn-danger btn-sm delete-note-btn"
                      data-note-id="<%= note.getId() %>"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <% } %>
          </div>
        <% } else { %>
          <p>You have no notes.</p>
        <% } %>
      </div>
    </div>

    <!-- New Note Modal -->
    <div class="modal fade" id="noteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title fs-5">New note</h2>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
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

    <div class="modal fade" id="editNoteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title fs-5">Edit note</h2>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
            ></button>
          </div>
          <form
            id="editNoteForm"
            action="<%= request.getContextPath() %>/notes/update"
            method="POST"
          >
            <div class="modal-body">
              <input type="hidden" id="editNoteId" name="noteId" />
              <textarea
                id="editNoteContent"
                name="noteContent"
                class="form-control mb-2"
                placeholder="Edit note content"
                required
              ></textarea>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                >
                  Cancel
                </button>
                <button type="submit" class="btn btn-primary">Save</button>
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
        const editNoteForm = document.getElementById('editNoteForm');
        const viewNoteModal = new bootstrap.Modal('#editNoteModal');

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

            alert(
              result.success
                ? 'Successfully created note'
                : json?.error || 'Failed to create note'
            );
            if (result.success) location.reload();
          } catch (err) {
            console.error('error during note creation');
            console.error(err);

            alert('Failed to create note');
          }
        });

        const editNoteBtns = document.querySelectorAll('.edit-note-btn');
        for (const btn of editNoteBtns) {
          btn.addEventListener('click', () => {
            const noteId = btn.getAttribute('data-note-id');
            const noteContent = btn.getAttribute('data-note-content');
            document.querySelector('#editNoteId').value = noteId;
            document.querySelector('#editNoteContent').value = noteContent;
            viewNoteModal.show();
          });
        }

        const deleteNoteBtns = document.querySelectorAll('.delete-note-btn');
        for (const btn of deleteNoteBtns) {
          btn.addEventListener('click', async () => {
            const noteId = btn.getAttribute('data-note-id');
            if (!noteId) {
              alert('No note selected to delete.');
              return;
            }

            if (!confirm('Are you sure you want to delete this note?'))
              return;

            try {
              const resp = await fetch(
                '<%= request.getContextPath() %>/notes/delete',
                {
                  method: 'POST',
                  body: new URLSearchParams({ noteId }),
                }
              );
              const result = await resp.json();
              alert(
                result.success
                  ? 'Successfully deleted note'
                  : result.error || 'Failed to delete note'
              );
              if (result.success) location.reload();
            } catch (err) {
              console.error('Error during note deletion', err);
              alert('Failed to delete note');
            }
          });
        }

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

            alert(
              result.success
                ? 'Note updated successfully'
                : result.error || 'Failed to update note'
            );
            if (result.success) location.reload();
          } catch (err) {
            console.error('Error during note update', err);
            alert('Failed to update note');
          }
        });
      });
    </script>
  </body>
</html>
