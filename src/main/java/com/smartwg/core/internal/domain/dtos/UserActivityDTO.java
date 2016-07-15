package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;

/**
 * @author Anna Sadriu (as)
 *
 */
public class UserActivityDTO {

  private Integer rating;
  private User user;
  private Integer userId;
  private String userName;
  private Integer activityId;
  private String activityName;

  public UserActivityDTO() {}



  public UserActivityDTO(User user, Integer userId, String userName, Integer activityId,
      String activityName, Integer rating) {
    this.user = user;
    this.userId = userId;
    this.userName = userName;
    this.activityId = activityId;
    this.activityName = activityName;
    this.rating = rating;
  }

  public UserActivityDTO(UserActivity userActivity) {
    this.user = userActivity.getUser();
    this.userId = userActivity.getUser().getId();
    this.userName = userActivity.getUser().getUserName();
    this.activityId = userActivity.getActivity().getId();
    this.activityName = userActivity.getActivity().getName();
    this.rating = userActivity.getRating();
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getActivityId() {
    return activityId;
  }

  public void setActivityId(Integer activityId) {
    this.activityId = activityId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }
}
