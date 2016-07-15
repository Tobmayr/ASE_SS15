package com.smartwg.core.internal.domain;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.util.Constants;

import java.util.ResourceBundle;

/**
 * @author Kamil Sierzant (ks)
 */

public enum RecurringType {

  MONTH("recMonth"), DAY("recDay"), YEAR("recYear");

  private   ResourceBundle BUNDLE = SmartWG.getMessageBundle();

  private String messageId;

  RecurringType(String messageId) {
    this.messageId = messageId;
  }

  public String getName() {
    return BUNDLE.getString(messageId);
  }

}
