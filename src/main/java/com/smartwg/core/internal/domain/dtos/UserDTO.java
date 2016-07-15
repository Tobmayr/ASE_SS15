package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.domain.entities.UserGroup;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Kamil Sierzant (sk), Anna Sadriu (as)
 */
public class UserDTO implements Serializable {

  private Integer id;
  private String firstName;
  private String lastName;
  private String userName;
  private byte[] password;
  private byte[] salt;
  private String password_clear;
  private String email;
  private Date birthDate;
  private BigDecimal income;
  private List<NotificationType> notificationTypes;
  private String confirmCode;
  private boolean confirmed;
  private List<UserGroupDTO> groups = new ArrayList<UserGroupDTO>();
  private List<UserActivityDTO> ratings = new ArrayList<UserActivityDTO>();
  private boolean online;
  private String locale;

  public UserDTO() {};

  public UserDTO(final Integer id, final String firstName, final String lastName,
      final String userName, final String email, Date birthDate, BigDecimal income,
      String notificationTypes) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
    this.birthDate = birthDate;
    this.income = income;
    this.notificationTypes =
        notificationTypes != null ? convertToList(notificationTypes) : new ArrayList<>();
  }

  public UserDTO(User u) {
    this.id = u.getId();
    this.firstName = u.getFirstName();
    this.lastName = u.getLastName();
    this.password = u.getPassword();
    this.userName = u.getUserName();
    this.salt = u.getSalt();
    this.email = u.getEmail();
    this.birthDate = u.getBirthDate();
    this.income = u.getIncome();
    this.notificationTypes =
        u.getNotify() != null ? convertToList(u.getNotify()) : new ArrayList<>();
    this.confirmCode = u.getConfirmCode();
    this.confirmed = u.isConfirmed();
    for (UserGroup ug : u.getGroups()) {
      this.groups.add(new UserGroupDTO(ug));
    }
    for (UserActivity ua : u.getRatings()) {
      this.ratings.add(new UserActivityDTO(ua));
    }

    for (UserActivity ua : u.getRatings()) {
      this.ratings.add(new UserActivityDTO(ua));
    }

    this.online = true;
    this.locale = u.getLocale();
  }

  private List<NotificationType> convertToList(final String notificationsString) {
    final List<NotificationType> notifications = new ArrayList<>();
    if (StringUtils.isNotBlank(notificationsString)) {
      for (String notification : notificationsString.split(";")) {
        notifications.add(NotificationType.valueOf(notification));
      }
    }
    return notifications;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof UserDTO))
      return false;
    UserDTO that = (UserDTO) obj;
    return new EqualsBuilder().append(this.confirmed, that.confirmed)
        .append(this.birthDate, that.birthDate).append(this.id, that.id)
        .append(this.email, that.email).append(this.userName, that.userName).isEquals();

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public BigDecimal getIncome() {
    return income;
  }

  public void setIncome(BigDecimal income) {
    this.income = income;
  }

  public List<NotificationType> getNotificationTypes() {
    return notificationTypes;
  }

  public void setNotificationTypes(List<NotificationType> notificationTypes) {
    this.notificationTypes = notificationTypes;
  }

  public byte[] getPassword() {
    return password;
  }

  public void setPassword(byte[] password) {
    this.password = password;
  }

  public byte[] getSalt() {
    return salt;
  }

  public void setSalt(byte[] salt) {
    this.salt = salt;
  }

  public String getPassword_clear() {
    return password_clear;
  }

  public void setPassword_clear(String password_clear) {
    this.password_clear = password_clear;
  }

  public String getConfirmCode() {
    return confirmCode;
  }

  public void setConfirmCode(String confirmCode) {
    this.confirmCode = confirmCode;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  public List<UserGroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<UserGroupDTO> groups) {

    this.groups = groups;
  }

  public List<UserActivityDTO> getRatings() {
    return ratings;
  }

  public void setRatings(List<UserActivityDTO> ratings) {
    this.ratings = ratings;
  }

  @Override
  public String toString() {
    return userName;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public String getLocale() {
    if (locale == null)
      return "en";
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }


}
