package com.group_3.healthlink;

import java.sql.Timestamp;

public class TestResult {
    private int resultId;
    private int patientId;
    private int doctorId;
    private String fileName;
    private byte[] fileData;
    private String fileType;
    private Timestamp uploadDate;
    private String description;
    
    // Patient and Doctor names for display
    private String patientName;
    private String doctorName;

    // Constructors
    public TestResult() {}

    public TestResult(int patientId, int doctorId, String fileName, byte[] fileData, String fileType, String description) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.fileName = fileName;
        this.fileData = fileData;
        this.fileType = fileType;
        this.description = description;
    }

    // Getters and Setters
    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    // Helper method to get file extension
    public String getFileExtension() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    // Helper method to check if file is image
    public boolean isImage() {
        String ext = getFileExtension();
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("gif");
    }

    // Helper method to check if file is PDF
    public boolean isPdf() {
        return getFileExtension().equals("pdf");
    }
} 