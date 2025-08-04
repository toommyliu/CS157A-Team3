package com.group_3.healthlink;

public enum SystemLogAction {
  // Top level actions (1-99)
  LOGIN(1),
  LOGOUT(2),
  NEW_USER(3),

  // Patient actions (100-199)
  ASSIGN_PATIENT_TO_DOCTOR(100),
  REMOVE_PATIENT_FROM_DOCTOR(101),

  // Doctor actions (200-299)
  CREATE_MEDICATION(200),
  DELETE_MEDICATION(201),

  // Admin actions (300-399)
  CREATE_DOCTOR(300),
  UPDATE_DOCTOR(301);

  private final int code;

  SystemLogAction(int code) {
    this.code = code;
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
