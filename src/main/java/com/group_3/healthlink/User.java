package com.group_3.healthlink;

import java.util.Date;

public class User {

  private int userId;
  private String emailAddress;
  private String passwordHashed;
  private String firstName;
  private String lastName;
  private UserRole role;
  private Date createdAt;
  private Date updatedAt;

  public User() {
  }

  public User(int userId,
              String emailAddress, String passwordHashed, String firstName, String lastName,
              UserRole role,
              Date createdAt, Date updatedAt
  ) {
    this.userId = userId;
    this.emailAddress = emailAddress;
    this.passwordHashed = passwordHashed;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPasswordHashed() {
    return passwordHashed;
  }

  public void setPasswordHashed(String passwordHashed) {
    this.passwordHashed = passwordHashed;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFullName() {
    String fullName = "";
    if (firstName != null && !firstName.isEmpty()) {
      fullName += firstName;
    }

    if (lastName != null && !lastName.isEmpty()) {
      if (!fullName.isEmpty()) {
        fullName += " ";
      }
      fullName += lastName;
    }

    return fullName;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Boolean isPatient() {
    return this.role == UserRole.Patient;
  }

  public Boolean isDoctor() {
    return this.role == UserRole.Doctor;
  }

  public Boolean isAdmin() {
    return this.role == UserRole.Admin;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
