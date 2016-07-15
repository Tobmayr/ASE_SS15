package com.smartwg.core.internal.domain;

import java.util.ResourceBundle;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.util.Constants;

public enum ActivityRepeatType {
  NOREPEAT("ENUM_NOREPEAT"), DAILY("ENUM_DAILY"), WEEKLY("ENUM_WEEKLY"), MONTHLY("ENUM_MONTHLY");
  private   ResourceBundle BUNDLE = SmartWG.getMessageBundle();
  private String messageId;

  ActivityRepeatType(String messageId) {
    this.messageId = messageId;
  }

  public String getName() {
    return BUNDLE.getString(messageId);
  }
}
