package com.smartwg.core.internal.domain.dtos;

import java.util.Date;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.entities.UserGroup;

/**
 * @author Tobias Ortmayr(to)
 *
 */
public class UserGroupDTO {

  private Role role;
  private Integer userId;
  private String userName;
  private Integer groupId;
  private String groupName;
  private Integer groupCountryId;
  private boolean deleted;
  private double balance;
  private Date joinDate;
  private Date leaveDate;

  public UserGroupDTO() {}

  public UserGroupDTO(UserGroup userGroup) {
    this.userId = userGroup.getUser().getId();
    this.userName = userGroup.getUser().getUserName();
    this.groupId = userGroup.getGroup().getId();
    this.groupName = userGroup.getGroup().getName();
    this.role = userGroup.getRole();
    this.groupCountryId = userGroup.getGroup().getCountry().getId();
    this.deleted = userGroup.isDeleted();
    this.balance = userGroup.getBalance();
    this.joinDate = userGroup.getJoinDate();
    this.leaveDate = userGroup.getLeaveDate();
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }

  public Date getLeaveDate() {
    return leaveDate;
  }

  public void setLeaveDate(Date leaveDate) {
    this.leaveDate = leaveDate;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer user) {
    this.userId = user;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer group) {
    this.groupId = group;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Integer getGroupCountryId() {
    return groupCountryId;
  }

  public void setGroupCountryId(Integer groupCountryId) {
    this.groupCountryId = groupCountryId;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean b) {
    this.deleted = b;

  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }



}
