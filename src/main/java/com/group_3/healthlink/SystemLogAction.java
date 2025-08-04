package com.group_3.healthlink;

public enum SystemLogAction {
  // Top level actions (1-99)
  LOGIN(1, "Login"),
  LOGOUT(2, "Logout"),
  NEW_USER(3, "New User"),
  UPDATE_PROFILE(4, "Update Profile"),

  // Patient actions (100-199)
  ASSIGN_PATIENT_TO_DOCTOR(100, "Assign Doctor"),
  REMOVE_PATIENT_FROM_DOCTOR(101, "Remove Doctor"),

  // Doctor actions (200-299)
  CREATE_MEDICATION(200, "Create Medication"),
  DELETE_MEDICATION(201, "Delete Medication"),

  // Admin actions (300-399)
  CREATE_DOCTOR(300, "Create Doctor"),
  UPDATE_DOCTOR(301, "Update Doctor");

  private final int code;
  private final String displayName;

  SystemLogAction(int code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  /**
   * Get the action code, a.k.a. the enum value.
   *
   * @return the action code
   */
  public int getCode() {
    return code;
  }

  /**
   * Get the display name for the action.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Get the SystemLogAction enum from its code.
   *
   * @param code the action code
   * @return the corresponding SystemLogAction enum
   */
  public static SystemLogAction fromCode(int code) {
    for (SystemLogAction action : SystemLogAction.values()) {
      if (action.getCode() == code)
        return action;
    }

    throw new IllegalArgumentException("Invalid SystemLogAction code: " + code);
  }
}
