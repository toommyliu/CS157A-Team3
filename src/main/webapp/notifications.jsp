<%@ page import="com.group_3.healthlink.Notification" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Healthlink - Notifications</title>
    <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' h:mm a");
    %>

    <div class="app-container">
        <jsp:include page="layouts/sidebar.jsp" />

        <div class="main-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="h2 fw-semibold">Notifications</h2>
                <% if (notifications != null && !notifications.isEmpty()) { %>
                    <button class="btn btn-outline-primary" onclick="markAllAsRead()">
                        <i class="bi bi-check-all me-2"></i>Mark All as Read
                    </button>
                <% } %>
            </div>

            <% if (notifications == null || notifications.isEmpty()) { %>
                <div class="text-center py-5">
                    <i class="bi bi-bell-slash" style="font-size: 4rem; color: #6c757d;"></i>
                    <h4 class="mt-3 text-muted">No notifications</h4>
                    <p class="text-muted">You're all caught up! Check back later for new updates.</p>
                </div>
            <% } else { %>
                <div class="row">
                    <div class="col-12">
                        <div class="list-group">
                            <% for (Notification notification : notifications) { %>
                                <div class="list-group-item list-group-item-action p-3 <% if (!notification.isRead()) { %>border-start border-primary border-4<% } %>"
                                     id="notification-<%= notification.getNotificationId() %>">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                            <div class="d-flex align-items-center mb-2">
                                                <% if (!notification.isRead()) { %>
                                                    <span class="badge bg-primary me-2">New</span>
                                                <% } %>
                                                <span class="badge bg-secondary me-2"><%= notification.getType() != null ? notification.getType().replace("_", " ") : "general" %></span>
                                                <small class="text-muted">
                                                    <%= dateFormat.format(notification.getTimestamp()) %>
                                                </small>
                                            </div>
                                            <p class="mb-2 <% if (!notification.isRead()) { %>fw-semibold<% } %>">
                                                <%= notification.getMessage() %>
                                            </p>
                                            <% if (notification.getSenderName() != null) { %>
                                                <small class="text-muted">
                                                    From: <%= notification.getSenderName() %>
                                                </small>
                                            <% } %>
                                        </div>
                                        <div class="ms-3">
                                            <div class="btn-group" role="group">
                                                <% if (!notification.isRead()) { %>
                                                    <button class="btn btn-sm btn-outline-success" 
                                                            onclick="markAsRead(<%= notification.getNotificationId() %>)"
                                                            title="Mark as read">
                                                        <i class="bi bi-check"></i>
                                                    </button>
                                                <% } %>
                                                <button class="btn btn-sm btn-outline-danger" 
                                                        onclick="deleteNotification(<%= notification.getNotificationId() %>)"
                                                        title="Delete notification">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    </div>

    <script src="js/bootstrap.min.js"></script>
    <script>
        function markAsRead(notificationId) {
            fetch('<%= request.getContextPath() %>/notifications', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=markAsRead&notificationId=' + notificationId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const notificationElement = document.getElementById('notification-' + notificationId);
                    notificationElement.classList.remove('border-start', 'border-primary', 'border-4');
                    
                    // Remove the "New" badge
                    const newBadge = notificationElement.querySelector('.badge.bg-primary');
                    if (newBadge) {
                        newBadge.remove();
                    }
                    
                    // Remove the bold font
                    const messageElement = notificationElement.querySelector('p');
                    if (messageElement) {
                        messageElement.classList.remove('fw-semibold');
                    }
                    
                    // Replace the mark as read button with a disabled one
                    const buttonGroup = notificationElement.querySelector('.btn-group');
                    if (buttonGroup) {
                        const markAsReadBtn = buttonGroup.querySelector('.btn-outline-success');
                        if (markAsReadBtn) {
                            markAsReadBtn.remove();
                        }
                    }
                    
                    // Update notification count in sidebar
                    updateNotificationCount();
                } else {
                    alert('Error: ' + (data.error || 'Failed to mark notification as read'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while marking the notification as read');
            });
        }
        
        function markAllAsRead() {
            if (!confirm('Mark all notifications as read?')) {
                return;
            }
            
            fetch('<%= request.getContextPath() %>/notifications', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=markAllAsRead'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Remove all "New" badges and styling
                    document.querySelectorAll('.list-group-item').forEach(item => {
                        item.classList.remove('border-start', 'border-primary', 'border-4');
                        
                        const newBadge = item.querySelector('.badge.bg-primary');
                        if (newBadge) {
                            newBadge.remove();
                        }
                        
                        const messageElement = item.querySelector('p');
                        if (messageElement) {
                            messageElement.classList.remove('fw-semibold');
                        }
                        
                        const markAsReadBtn = item.querySelector('.btn-outline-success');
                        if (markAsReadBtn) {
                            markAsReadBtn.remove();
                        }
                    });
                    
                    updateNotificationCount();
                    
                    // Hide the "Mark All as Read" button
                    const markAllBtn = document.querySelector('.btn-outline-primary');
                    if (markAllBtn) {
                        markAllBtn.style.display = 'none';
                    }
                } else {
                    alert('Error: ' + (data.error || 'Failed to mark all notifications as read'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while marking all notifications as read');
            });
        }
        
        function deleteNotification(notificationId) {
            if (!confirm('Are you sure you want to delete this notification?')) {
                return;
            }
            
            fetch('<%= request.getContextPath() %>/notifications', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=delete&notificationId=' + notificationId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const notificationElement = document.getElementById('notification-' + notificationId);
                    notificationElement.remove();
                    
                    // Check if there are any notifications left
                    const remainingNotifications = document.querySelectorAll('.list-group-item');
                    if (remainingNotifications.length === 0) {
                        location.reload(); // Reload to show "No notifications" message
                    }
                    
                    updateNotificationCount();
                } else {
                    alert('Error: ' + (data.error || 'Failed to delete notification'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while deleting the notification');
            });
        }
        
        function updateNotificationCount() {
            setTimeout(() => {
                location.reload();
            }, 1000);
        }
    </script>
</body>
</html> 