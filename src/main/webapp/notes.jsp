<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Healthlink - Notes</title>
    <link href="css/styles.css" rel="stylesheet"/>
</head>
<body>
    <div class="app-container">
        <jsp:include page="layouts/sidebar.jsp"/>

        <div class="main-content">
            <div class="d-flex justify-content-between align-items-center border-bottom gap-2">
                <h1 class="h1 fw-semibold">My Notes</h1>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#noteModal">
                    New Note
                </button>
            </div>

        </div>
    </div>

    <div class="modal fade" id="noteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">New note</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="<%= request.getContextPath() %>/notes/create" method="POST">
                    <div class="modal-body">
                        <input type="text" id="noteTitle" name="title" class="form-control mb-3" placeholder="Note Title" required>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary">Create</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.min.js"></script>
</body>
</html>
