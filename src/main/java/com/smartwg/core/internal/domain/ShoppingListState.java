package com.smartwg.core.internal.domain;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.util.Constants;

import java.util.ResourceBundle;

/**
 * @author Kamil Sierzant (ks)
 */
public enum ShoppingListState {
  NEW("ShoppingListState.NEW"), RELEASED("ShoppingListState.RELEASED"), ASSIGNED(
      "ShoppingListState.ASSIGNED"), DONE("ShoppingListState.DONE"), CANCELLED(
      "ShoppingListState.CANCELLED");

  private   ResourceBundle BUNDLE = SmartWG.getMessageBundle();
  private String labelKey;

  ShoppingListState(String labelKey) {
    this.labelKey = labelKey;
  }

  public String getName() {
    return BUNDLE.getString(labelKey);
  }
}
