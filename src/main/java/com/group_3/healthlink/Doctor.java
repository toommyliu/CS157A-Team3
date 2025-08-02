package com.group_3.healthlink;

public class Doctor extends User {
  private int doctorId;
  private String department;

  public Doctor() {
  }

  public Doctor(int doctorId, String department) {
    this.doctorId = doctorId;
    this.department = department;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(int doctorId) {
    this.doctorId = doctorId;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getDrName() {
    return "Dr. " + getFullName();
  }
}
