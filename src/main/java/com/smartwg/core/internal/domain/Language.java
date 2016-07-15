package com.smartwg.core.internal.domain;

import com.smartwg.core.controllers.SmartWG;

public enum Language {
  
  GERMAN("GERMAN", "de"), ENGLISH("ENGLISH", "en"), TURKISH("TURKISH", "tr"), POLISH("POLISH", "pl"), BULGARIAN(
      "BULGARIAN", "bg");
 

  private String messageId;
  private String locale;

  Language(String messageId, String locale) {
    this.messageId = messageId;
    this.locale = locale;
  }

  public String getLocale() {
    return locale;
  }

  public String getName() {
    return SmartWG.getMessageBundle().getString(messageId);
  }

  public static Language valueOfByLocale(String locale) {
    if (locale == null)
      return ENGLISH;
    for (Language l : values()) {
      if (l.getLocale().equals(locale)) {
        return l;
      }
    }
    return ENGLISH;
  }
}
