package com.group_3.healthlink;

public enum UserRole {
  Patient,
  Doctor,
  Admin;

  @Override
    public String toString() {
        switch (this) {
            case Patient:
                return "Patient";
            case Doctor:
                return "Doctor";
            case Admin:
                return "Admin";
            default:
                return "Unknown";
        }
    }
}
