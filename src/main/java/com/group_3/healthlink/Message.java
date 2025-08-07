package com.group_3.healthlink;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp timestamp;
    private boolean isRead;

    public Message() {
    }

    public Message(int messageId, int senderId, int receiverId, String content, Timestamp timestamp, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    // Constructor without messageId (for creating new messages)
    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false; // New messages are unread by default
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                '}';
    }
}