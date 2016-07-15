package com.smartwg.core.controllers;

import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

@Named("freshDashboard")
@Scope("view")
public class FreshDashboardBean {

  private String placeholder = "Insert beautiful dashboard here";
  private ResourceBundle valMs = SmartWG.getValidationBundle();

  @PostConstruct
  public void init() {
    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    String msg = requestParameterMap.get("msg");
    String group = requestParameterMap.get("group");
    if (msg != null) {
      String message = valMs.getString(msg);
      if (group != null) {
        message = message.replace(":group", group);
      }
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO, message);
    }
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }


}
