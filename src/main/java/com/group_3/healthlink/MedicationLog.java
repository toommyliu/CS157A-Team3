package com.group_3.healthlink;

public class MedicationLog {
  private int medicationLogId;

  private int medicationId;

  private int patientId;

  private String dosageTaken;

  private String note;

  // The timestamp when the medication was taken, not necessarily the same as createdAt
  private java.sql.Timestamp takenAt;

  // The timestamp when the log entry was created in the database
  private java.sql.Timestamp createdAt;

  public MedicationLog() {}

  public MedicationLog(int medicationLogId, int medicationId, int patientId, String dosageTaken, String note, java.sql.Timestamp takenAt, java.sql.Timestamp createdAt) {
    this.medicationLogId = medicationLogId;
    this.medicationId = medicationId;
    this.patientId = patientId;
    this.dosageTaken = dosageTaken;
    this.note = note;
    this.takenAt = takenAt;
    this.createdAt = createdAt;
  }

  public int getMedicationLogId() {
    return medicationLogId;
  }

  public void setMedicationLogId(int medicationLogId) {
    this.medicationLogId = medicationLogId;
  }

  public int getMedicationId() {
    return medicationId;
  }

  public void setMedicationId(int medicationId) {
    this.medicationId = medicationId;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public String getDosageTaken() {
    return dosageTaken;
  }

  public void setDosageTaken(String dosageTaken) {
    this.dosageTaken = dosageTaken;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public java.sql.Timestamp getTakenAt() {
    return takenAt;
  }

  public void setTakenAt(java.sql.Timestamp takenAt) {
    this.takenAt = takenAt;
  }

  public java.sql.Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.sql.Timestamp createdAt) {
    this.createdAt = createdAt;
  }
}
