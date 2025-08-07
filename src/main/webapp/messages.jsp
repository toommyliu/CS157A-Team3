<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.group_3.healthlink.User" %>
<%@ page import="com.group_3.healthlink.UserRole" %>
<%@ page import="com.group_3.healthlink.services.MessageService" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Healthlink - Messages</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
  <style>
    .chat-container {
      display: flex;
      height: calc(100vh - 100px);
      border: 1px solid #dee2e6;
      border-radius: 8px;
      overflow: hidden;
    }
    
    .contacts-sidebar {
      width: 300px;
      border-right: 1px solid #dee2e6;
      background-color: #f8f9fa;
      overflow-y: auto;
    }
    
    .chat-area {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
    
    .chat-header {
      padding: 15px 20px;
      border-bottom: 1px solid #dee2e6;
      background-color: #fff;
    }
    
    .chat-messages {
      flex: 1;
      padding: 20px;
      overflow-y: auto;
      background-color: #fff;
    }
    
    .chat-input {
      padding: 15px 20px;
      border-top: 1px solid #dee2e6;
      background-color: #fff;
    }
    
    .contact-item {
      padding: 15px 20px;
      border-bottom: 1px solid #dee2e6;
      cursor: pointer;
      transition: background-color 0.2s;
      position: relative;
    }
    
    .contact-item:hover {
      background-color: #e9ecef;
    }
    
    .contact-item.active {
      background-color: #007bff;
      color: white;
    }
    
    .unread-indicator {
      position: absolute;
      top: 15px;
      right: 20px;
      width: 8px;
      height: 8px;
      background-color: #007bff;
      border-radius: 50%;
      display: none;
    }
    
    .contact-item.has-unread .unread-indicator {
      display: block;
    }
    
    .message {
      margin-bottom: 15px;
      display: flex;
    }
    
    .message.sent {
      justify-content: flex-end;
    }
    
    .message.received {
      justify-content: flex-start;
    }
    
    .message-content {
      max-width: 70%;
      padding: 10px 15px;
      border-radius: 18px;
      word-wrap: break-word;
    }
    
    .message.sent .message-content {
      background-color: #007bff;
      color: white;
    }
    
    .message.received .message-content {
      background-color: #e9ecef;
      color: #333;
    }
    
    .message-time {
      font-size: 0.75rem;
      color: #6c757d;
      margin-top: 5px;
    }
    
    .no-chat-selected {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;
      color: #6c757d;
      font-size: 1.1rem;
    }
    
    .unread-indicator {
      position: absolute;
      top: 15px;
      right: 20px;
      width: 8px;
      height: 8px;
      background-color: #007bff;
      border-radius: 50%;
      display: none;
    }
    
    .contact-item.has-unread .unread-indicator {
      display: block;
    }
  </style>
</head>
<body>
  <% User currentUser = (User)session.getAttribute("user"); %>
  <% if (currentUser == null) { %>
    <script>
      window.location.href = '<%= request.getContextPath() %>/login';
    </script>
  <% } else { %>
<div class="app-container">
  <jsp:include page="layouts/sidebar.jsp" />

  <div class="main-content">
        <div class="d-flex justify-content-between align-items-center border-bottom mb-4 pb-2">
          <h2 class="h2 fw-semibold mb-0">Messages</h2>
        </div>

        <div class="chat-container">
          <div class="contacts-sidebar">
            <div class="p-3 border-bottom">
              <h5 class="mb-0">
                <% if (currentUser.getRole() == UserRole.Patient) { %>
                  My Doctors
                <% } else { %>
                  My Patients
                <% } %>
              </h5>
            </div>
            <div id="contactsList"></div>
          </div>

          <div class="chat-area">
            <div id="chatHeader" class="chat-header" style="display: none;">
              <h5 class="mb-0" id="chatTitle">Select a contact to start chatting</h5>
            </div>
            
            <div id="chatMessages" class="chat-messages">
              <div class="no-chat-selected">
                <div class="text-center">
                  <i class="bi bi-chat-dots" style="font-size: 3rem; color: #dee2e6;"></i>
                  <p class="mt-3">Select a contact to start chatting</p>
                </div>
              </div>
            </div>
            
            <div id="chatInput" class="chat-input" style="display: none;">
              <form id="messageForm" class="d-flex gap-2">
                <input type="text" id="messageInput" class="form-control" placeholder="Type your message..." required>
                <button type="submit" class="btn btn-primary">
                  <i class="bi bi-send"></i>
                </button>
              </form>
            </div>
          </div>
        </div>
  </div>
</div>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script>
      let currentChatUserId = null;
      let currentUser = {
        userId: <%= currentUser.getUserId() %>,
        role: '<%= currentUser.getRole() %>'
      };
      
      <% Integer selectedUserId = (Integer) request.getAttribute("selectedUserId"); %>
      <% if (selectedUserId != null) { %>
        let initialSelectedUserId = <%= selectedUserId %>;
      <% } else { %>
        let initialSelectedUserId = null;
      <% } %>

      document.addEventListener('DOMContentLoaded', function() {
        loadContacts();
      });
      
      window.addEventListener('popstate', function(event) {
        if (event.state && event.state.userId) {
          // User navigated to a specific chat
          const userId = event.state.userId;
          loadContacts();
        } else {
          // User navigated to main messages page
          currentChatUserId = null;
          document.getElementById('chatHeader').style.display = 'none';
          document.getElementById('chatInput').style.display = 'none';
          document.querySelectorAll('.contact-item').forEach(item => {
            item.classList.remove('active');
          });
        }
      });

      // Mark messages as read
      async function markMessagesAsRead(otherUserId) {
        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/mark-read/' + otherUserId, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            }
          });
          if (response.ok) {
            console.log('Messages marked as read');
          }
        } catch (error) {
          console.error('Error marking messages as read:', error);
        }
      }

      // Check if a contact has unread messages
      async function checkUnreadMessages(otherUserId) {
        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/history/' + otherUserId);
          if (response.ok) {
            const messages = await response.json();
            return messages.some(message => 
              message.receiverId === currentUser.userId && !message.isRead
            );
          }
        } catch (error) {
          console.error('Error checking unread messages:', error);
        }
        return false;
      }

      // Load contacts from API
      async function loadContacts() {
        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/threads');
          if (response.ok) {
            const contacts = await response.json();
            displayContacts(contacts);
            
            // If there's an initial selected user, select them
            if (initialSelectedUserId) {
              const contact = contacts.find(c => c.userId === initialSelectedUserId);
              if (contact) {
                selectContact(contact, true); // true = don't update URL (we're already there)
              }
            }
          } else {
            console.error('Failed to load contacts');
          }
        } catch (error) {
          console.error('Error loading contacts:', error);
        }
      }

      // Display contacts in sidebar
      async function displayContacts(contacts) {
        const contactsList = document.getElementById('contactsList');
        contactsList.innerHTML = '';

        if (contacts.length === 0) {
          contactsList.innerHTML = '<div class="p-3 text-muted">No contacts available</div>';
          return;
        }

        for (const contact of contacts) {
          const contactItem = document.createElement('div');
          contactItem.className = 'contact-item';
          contactItem.onclick = () => selectContact(contact);
          
          // Construct full name from firstName and lastName
          const fullName = (contact.firstName || '') + ' ' + (contact.lastName || '').trim();
          const displayName = fullName.trim() || 'Unknown';
          
          // Check if this contact has unread messages
          const hasUnread = await checkUnreadMessages(contact.userId);
          if (hasUnread) {
            contactItem.classList.add('has-unread');
          }
          
          contactItem.setAttribute('data-user-id', contact.userId);
          
          contactItem.innerHTML = 
            '<div class="fw-semibold">' + displayName + '</div>' +
            '<div class="small text-muted">' + (contact.emailAddress || '') + '</div>' +
            '<div class="unread-indicator"></div>';
          
          contactsList.appendChild(contactItem);
        }
      }

      // Select a contact and load chat
      async function selectContact(contact, skipUrlUpdate = false) {
        // Update active contact
        document.querySelectorAll('.contact-item').forEach(item => {
          item.classList.remove('active');
        });
        
        // Find and activate the correct contact item
        const contactItems = document.querySelectorAll('.contact-item');
        for (let item of contactItems) {
          if (item.textContent.includes(contact.emailAddress)) {
            item.classList.add('active');
            break;
          }
        }

        currentChatUserId = contact.userId;
        
        // Show chat interface
        document.getElementById('chatHeader').style.display = 'block';
        document.getElementById('chatInput').style.display = 'block';
        
        const fullName = (contact.firstName || '') + ' ' + (contact.lastName || '').trim();
        const displayName = fullName.trim() || 'Unknown';
        document.getElementById('chatTitle').textContent = displayName;
        
        // Update URL if not skipping
        if (!skipUrlUpdate) {
          const newUrl = '${pageContext.request.contextPath}/messages/' + contact.userId;
          window.history.pushState({userId: contact.userId}, '', newUrl);
        }
        
        // Load chat history
        await loadChatHistory(contact.userId);
        
        // Mark messages as read when chat is opened
        await markMessagesAsRead(contact.userId);
        
        // Remove unread indicator from this contact
        const contactItem = document.querySelector(`.contact-item[data-user-id="${contact.userId}"]`);
        if (contactItem) {
          contactItem.classList.remove('has-unread');
        }
      }

      // Load chat history
      async function loadChatHistory(otherUserId) {
        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/history/' + otherUserId);
          if (response.ok) {
            const messages = await response.json();
            displayMessages(messages);
            updateMessageCount(messages); 
          } else {
            console.error('Failed to load chat history');
          }
        } catch (error) {
          console.error('Error loading chat history:', error);
        }
      }

      // Display messages in chat area
      function displayMessages(messages) {
        const chatMessages = document.getElementById('chatMessages');
        chatMessages.innerHTML = '';

        if (messages.length === 0) {
          chatMessages.innerHTML = '<div class="text-center text-muted mt-4">No messages yet. Start the conversation!</div>';
          return;
        }

        messages.forEach(message => {
          const messageDiv = document.createElement('div');
          const isSent = message.senderId === currentUser.userId;
          messageDiv.className = 'message ' + (isSent ? 'sent' : 'received');
          
          const time = new Date(message.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
          
          messageDiv.innerHTML = 
            '<div class="message-content">' +
              '<div>' + message.content + '</div>' +
              '<div class="message-time">' + time + '</div>' +
            '</div>';
          
          chatMessages.appendChild(messageDiv);
        });

        // Scroll to bottom
        chatMessages.scrollTop = chatMessages.scrollHeight;
      }

      // Handle message form submission
      document.getElementById('messageForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (!currentChatUserId) {
          alert('Please select a contact first');
          return;
        }

        const messageInput = document.getElementById('messageInput');
        const content = messageInput.value.trim();
        
        if (!content) return;

        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/send', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              receiverId: currentChatUserId,
              content: content
            })
          });

          if (response.ok) {
            const result = await response.json();
            if (result.success) {
              messageInput.value = '';
              await checkForNewMessages();
            } else {
              alert('Failed to send message');
            }
          } else {
            alert('Failed to send message');
          }
        } catch (error) {
          console.error('Error sending message:', error);
          alert('Error sending message');
        }
      });

      let lastMessageCount = 0;
      let lastMessageTimestamp = null;
      
      // Check for new messages every 3 seconds
      setInterval(async () => {
        if (currentChatUserId) {
          await checkForNewMessages();
        }
        
        await updateSidebarUnreadCount();
      }, 3000);
      
      // Check for new messages without full reload
      async function checkForNewMessages() {
        try {
          const response = await fetch('${pageContext.request.contextPath}/messages/api/history/' + currentChatUserId);
          if (response.ok) {
            const messages = await response.json();
            
            // Check if we have new messages
            if (messages.length > lastMessageCount) {
              displayMessages(messages);
              lastMessageCount = messages.length;
              
              // Auto-scroll to bottom for new messages
              const chatMessages = document.getElementById('chatMessages');
              chatMessages.scrollTop = chatMessages.scrollHeight;
              
              // Visual feedback for new messages
              showNewMessageIndicator();
            }
          }
        } catch (error) {
          console.error('Error checking for new messages:', error);
        }
      }
      
       // Update message count when chat history is loaded
       function updateMessageCount(messages) {
         lastMessageCount = messages.length;
         if (messages.length > 0) {
           lastMessageTimestamp = messages[messages.length - 1].timestamp;
         }
       }
       
       // Show visual indicator for new messages
       function showNewMessageIndicator() {
         const chatMessages = document.getElementById('chatMessages');
         chatMessages.style.backgroundColor = '#f8f9fa';
         setTimeout(() => {
           chatMessages.style.backgroundColor = '#fff';
         }, 500);
       }
       
       // Update unread count in sidebar
       async function updateSidebarUnreadCount() {
         try {
           const response = await fetch('${pageContext.request.contextPath}/messages/api/unread-conversations');
           if (response.ok) {
             const data = await response.json();
             const unreadCount = data.unreadConversations;
             
             // Find the Messages link in sidebar and update badge
             const messagesLink = document.querySelector('a[href*="/messages"]');
             if (messagesLink) {
               let badge = messagesLink.querySelector('.badge');
               if (unreadCount > 0) {
                 if (!badge) {
                   badge = document.createElement('span');
                   badge.className = 'badge bg-primary ms-2';
                   messagesLink.appendChild(badge);
                 }
                 badge.textContent = unreadCount;
               } else {
                 if (badge) {
                   badge.remove();
                 }
               }
             }
           }
         } catch (error) {
           console.error('Error updating sidebar unread count:', error);
         }
       }
    </script>
  <% } %>
</body>
</html>
