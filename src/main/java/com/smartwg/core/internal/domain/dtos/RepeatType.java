package com.smartwg.core.internal.domain.dtos;

import java.util.ResourceBundle;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.util.Constants;

/**
 * @author Tobias Ortmayr (to)
 */
public enum RepeatType {

  FOREVER("repFever"), TIMES("repTimes"), UNTIL("repUntil");
  private String ms_id;
  private   ResourceBundle ms = SmartWG.getMessageBundle();

  RepeatType(String ms_id) {
    this.ms_id = ms_id;

  }

  public String getName() {
    return ms.getString(ms_id);
  }

}
