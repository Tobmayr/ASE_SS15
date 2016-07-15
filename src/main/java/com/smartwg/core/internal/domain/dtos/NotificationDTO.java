package com.smartwg.core.internal.domain.dtos;

/**
 * @author Kamil Sierzant(ks)
 */
public class NotificationDTO {

  private String email;
  private String activeNotificationTypes;

  public NotificationDTO(String email, String activeNotificationTypes) {
    this.email = email;
    this.activeNotificationTypes = activeNotificationTypes;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getActiveNotificationTypes() {
    return activeNotificationTypes;
  }

  public void setActiveNotificationTypes(String activeNotificationTypes) {
    this.activeNotificationTypes = activeNotificationTypes;
  }
}
