package com.group_3.healthlink;

import java.util.Date;

public class User {
    private int id;
    private String emailAddress;
    private String passwordHashed;
    private String firstName;
    private String lastName;
    private String role;
    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    public User(int id,
                String emailAddress, String passwordHashed, String firstName, String lastName, String role,
                Date createdAt, Date updatedAt
    ) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.passwordHashed = passwordHashed;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRole() {

        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
