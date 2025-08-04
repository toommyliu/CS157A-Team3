package com.group_3.healthlink;

public class SystemLog {
  private int logId;

  /**
   * The userId of the user who performed the action.
   */
  private int userId;

  private SystemLogAction action;

  private String detail;

  private java.sql.Timestamp timestamp;

  public SystemLog() {
  }

  public SystemLog(int logId, int userId, SystemLogAction action, String detail, java.sql.Timestamp timestamp) {
    this.logId = logId;
    this.userId = userId;
    this.action = action;
    this.detail = detail;
    this.timestamp = timestamp;
  }

  public int getLogId() {
    return logId;
  }

  public void setLogId(int logId) {
    this.logId = logId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public SystemLogAction getAction() {
    return action;
  }

  public void setAction(SystemLogAction action) {
    this.action = action;
  }

  public String getActionName() {
    switch (this.action) {

      case LOGIN -> {
        return "Login";
      }
      case LOGOUT -> {
        return "Logout";
      }

      case ASSIGN_PATIENT_TO_DOCTOR -> {
        return "Assign Doctor";
      }
      case REMOVE_PATIENT_FROM_DOCTOR -> {
        return "Remove Doctor";
      }

      case CREATE_MEDICATION -> {
        return "Create Medication";
      }
      case DELETE_MEDICATION -> {
        return "Delete Medication";
      }

      case CREATE_DOCTOR -> {
        return "Create Doctor";
      }
      case UPDATE_DOCTOR -> {
        return "Update Doctor";
      }

      default -> {
        return "Unknown Action";
      }
    }
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public java.sql.Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(java.sql.Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
