package com.group_3.healthlink;

public class SystemLog {
  private int logId;

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
    return this.action != null ? this.action.getDisplayName() : "Unknown Action";
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
