package com.group_3.healthlink;

import java.util.Date;

public class Note {
    private int id;
    private int patientId;
    private int doctorId;
    private String content;
    private Date createdAt;

    public Note() {
    }

    public Note(int id, int patientId, int doctorId, String content, Date createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
