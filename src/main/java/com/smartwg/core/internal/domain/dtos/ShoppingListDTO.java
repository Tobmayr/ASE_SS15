package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Kamil Sierzant (ks)
 */
public class ShoppingListDTO {

  private Integer id;
  private String name;
  private Date deadline;
  private boolean emailNotification;
  private Integer createdBy;
  private String createdByFirstName;
  private String createdByLastName;
  private String createdByUsername;
  private Integer assignedTo;
  private String assignedToFirstName;
  private String assignedToLastName;
  private String assignedToUsername;
  private boolean privateList;
  private ShoppingListState state;
  private List<ListItemDTO> listItemDTOs = new ArrayList<>();
  private Integer activity;
  private String activityName;

  /**
   * Constructor for creating new shopping lists
   */
  public ShoppingListDTO() {
    this.state = ShoppingListState.NEW;
  }

  /**
   * Constructor for named query
   *
   * @param id {@link Long}
   * @param name {@link String}
   * @param deadline {@link Date}
   * @param emailNotification {@link boolean}
   * @param createdBy {@link Long}
   * @param createdByFirstName {@link String}
   * @param createdByLastName {@link String}
   * @param assignedTo {@link Long}
   * @param privateList {@link Boolean}
   * @param state {@link ShoppingListState}
   * @param activity {@link Integer}
   * @param activityName {@link String}
   */
  public ShoppingListDTO(final Integer id, final String name, final Date deadline,
      final boolean emailNotification, final Integer createdBy, final String createdByFirstName,
      final String createdByLastName, final String createdByUsername, final Integer assignedTo,
      final String assignedToFirstName, final String assignedToLastName,
      final String assignedToUsername, final boolean privateList, final ShoppingListState state,
      final Integer activity, final String activityName) {
    this.id = id;
    this.name = name;
    this.deadline = deadline;
    this.emailNotification = emailNotification;
    this.createdBy = createdBy;
    this.createdByFirstName = createdByFirstName;
    this.createdByLastName = createdByLastName;
    this.createdByUsername = createdByUsername;
    this.assignedTo = assignedTo;
    this.assignedToFirstName = assignedToFirstName;
    this.assignedToLastName = assignedToLastName;
    this.assignedToUsername = assignedToUsername;
    this.privateList = privateList;
    this.state = state;
    this.activity = activity;
    this.activityName = activityName;
  }

  public ShoppingListDTO(final Integer id, final String name, final Date deadline,
      final boolean emailNotification, final User createdBy, final User assignedTo,
      final boolean privateList, final ShoppingListState state, final Activity activity) {
    this.id = id;
    this.name = name;
    this.deadline = deadline;
    this.emailNotification = emailNotification;
    if (createdBy != null) {
      this.createdBy = createdBy.getId();
      this.createdByFirstName = createdBy.getFirstName();
      this.createdByLastName = createdBy.getLastName();
      this.createdByUsername = createdBy.getUserName();
    }
    if (assignedTo != null) {
      this.assignedTo = assignedTo.getId();
      this.assignedToFirstName = assignedTo.getFirstName();
      this.assignedToLastName = assignedTo.getLastName();
      this.assignedToUsername = assignedTo.getUserName();
    }
    this.privateList = privateList;
    this.state = state;
    if (activity != null) {
      this.activity = activity.getId();
      this.activityName = activity.getName();
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public boolean isEmailNotification() {
    return emailNotification;
  }

  public void setEmailNotification(boolean emailNotification) {
    this.emailNotification = emailNotification;
  }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedByFirstName() {
    return createdByFirstName;
  }

  public void setCreatedByFirstName(String createdByFirstName) {
    this.createdByFirstName = createdByFirstName;
  }

  public String getCreatedByLastName() {
    return createdByLastName;
  }

  public void setCreatedByLastName(String createdByLastName) {
    this.createdByLastName = createdByLastName;
  }

  public String getCreatedByUsername() {
    return createdByUsername;
  }

  public void setCreatedByUsername(String createdByUsername) {
    this.createdByUsername = createdByUsername;
  }

  public Integer getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(Integer assignedTo) {
    this.assignedTo = assignedTo;
  }

  public String getAssignedToFirstName() {
    return assignedToFirstName;
  }

  public void setAssignedToFirstName(String assignedToFirstName) {
    this.assignedToFirstName = assignedToFirstName;
  }

  public String getAssignedToLastName() {
    return assignedToLastName;
  }

  public void setAssignedToLastName(String assignedToLastName) {
    this.assignedToLastName = assignedToLastName;
  }

  public String getAssignedToUsername() {
    return assignedToUsername;
  }

  public void setAssignedToUsername(String assignedToUsername) {
    this.assignedToUsername = assignedToUsername;
  }

  public boolean isPrivateList() {
    return privateList;
  }

  public void setPrivateList(boolean privateList) {
    this.privateList = privateList;
  }

  public ShoppingListState getState() {
    return state;
  }

  public void setState(ShoppingListState state) {
    this.state = state;
  }

  public List<ListItemDTO> getListItemDTOs() {
    return listItemDTOs;
  }

  public void setListItemDTOs(List<ListItemDTO> listItemDTOs) {
    this.listItemDTOs = listItemDTOs;
  }

  public Integer getActivityId() {
    return activity;
  }

  public void setActivityId(Integer activity) {
    this.activity = activity;
  }

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }
}
