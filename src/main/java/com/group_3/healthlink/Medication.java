package com.group_3.healthlink;

public class Medication {
  private int id;
  private int patientId;
  private int doctorId;
  private String name;
  private String dosage;
  private String frequency;
  private String notes;

  public Medication() {
  }

  public Medication(int id, int patientId, int doctorId, String name, String dosage, String frequency, String notes) {
    this.id = id;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.name = name;
    this.dosage = dosage;
    this.frequency = frequency;
    this.notes = notes;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDosage() {
    return dosage;
  }

  public void setDosage(String dosage) {
    this.dosage = dosage;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
