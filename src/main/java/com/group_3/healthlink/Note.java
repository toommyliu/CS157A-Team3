package com.group_3.healthlink;

public class Note {
    private int id;
    private int userId;
    private String content;
    private java.sql.Timestamp timestamp;

    public Note() {
    }

    public Note(int id, int userId, String content, java.sql.Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
