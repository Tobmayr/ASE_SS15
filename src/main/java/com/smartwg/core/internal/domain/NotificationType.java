package com.smartwg.core.internal.domain;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.util.Constants;

import java.util.ResourceBundle;

/**
 * @author Kamil Sierzant(ks)
 */
public enum NotificationType {

  BILL("NotificationType.BILL"), SHOPPING_LIST("NotificationType.SHOPPING_LIST"), ACTIVITY(
      "NotificationType.ACTIVITY");

  private   ResourceBundle BUNDLE = SmartWG.getMessageBundle();

  private String labelKey;

  NotificationType(final String labelKey) {
    this.labelKey = labelKey;
  }

  public String getName() {
    return BUNDLE.getString(labelKey);
  }
}
