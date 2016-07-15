package com.smartwg.core.internal.domain.dtos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.UserActivity;

/**
 * 
 * @author Matthias Höllthaler (mh), Anna Sadriu (as)
 *
 */
public class ActivityDTO {

  private Integer id;
  private String name;
  private Date date;
  private int points;
  private int rating;
  private double median;
  private Integer group;
  private Integer createdById;
  private String createdBy_Username;
  private Integer assignedToId;
  private String assigentTo_Username;
  private List<UserActivityDTO> ratings = new ArrayList<UserActivityDTO>();
  private RecurringDTO recurring;
  private boolean is_done;


  public ActivityDTO() {}

  public ActivityDTO(final Integer id, final String name, final Date date, final int points,
      final Integer group, final Integer createdBy, final Integer assignedTo,
      final boolean is_done, final int rating) {
    super();
    this.id = id;
    this.name = name;
    this.date = date;
    this.points = points;
    this.group = group;
    this.createdById = createdBy;
    this.assignedToId = assignedTo;
    this.is_done = is_done;
  }

  public ActivityDTO(final Activity a) {
    this.id = a.getId();
    this.name = a.getName();
    this.date = a.getDate();
    this.points = a.getPoints();
    this.group = a.getGroup().getId();
    this.createdById = a.getCreatedBy().getId();
    this.createdBy_Username = a.getCreatedBy().getUserName();
    if (a.getAssignedTo() != null) {
      this.assigentTo_Username = a.getAssignedTo().getUserName();
      this.assignedToId = a.getAssignedTo().getId();
    }
    this.is_done = a.isDone();

    for (UserActivity ua : a.getRatings()) {
      this.ratings.add(new UserActivityDTO(ua));
    }
    if (a.getRecurring() != null) {
      this.recurring = new RecurringDTO(a.getRecurring());
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.date = calendar.getTime();
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public boolean getIsDone() {
    return is_done;
  }

  public void setIsDone(boolean is_done) {
    this.is_done = is_done;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public Integer getGroupId() {
    return group;
  }

  public void setGroupId(Integer group) {
    this.group = group;
  }

  public Integer getCreatedByUserId() {
    return createdById;
  }

  public void setCreatedByUserId(Integer createdBy) {
    this.createdById = createdBy;
  }

  public Integer getAssignedToUserId() {
    return assignedToId;
  }

  public void setAssignedToUserId(Integer assignedTo) {
    this.assignedToId = assignedTo;
  }

  public String getCreatedBy_Username() {
    return createdBy_Username;
  }

  public void setCreatedBy_Username(String createdBy_Username) {
    this.createdBy_Username = createdBy_Username;
  }

  public String getAssigentTo_Username() {
    return assigentTo_Username;
  }

  public void setAssigentTo_Username(String assigentTo_Username) {
    this.assigentTo_Username = assigentTo_Username;
  }

  public List<UserActivityDTO> getRatings() {
    return ratings;
  }

  public void setRatings(List<UserActivityDTO> ratings) {
    this.ratings = ratings;
  }

  public double getMedian() {
    return median;
  }

  public void setMedian(double median) {
    this.median = median;
  }

  public Integer getRatingFromUser(String username) {
    Integer rate = 0;
    for (UserActivityDTO ua : ratings)
      if (ua.getUserName().equals(username))
        rate = ua.getRating();
    return rate;
  }

  public void setRatingFromUser(Integer rate, String username) {
    boolean userNotVotedYet = true;

    if (ratings.size() != 0)
      for (UserActivityDTO ua : ratings)
        if (ua.getUserName() == username) {
          ua.setRating(rate);
          userNotVotedYet = false;
        }

    if (ratings.size() == 0 || userNotVotedYet) {
      UserActivityDTO uaDTO = new UserActivityDTO();
      uaDTO.setActivityId(this.getId());
      uaDTO.setRating(rate);
      uaDTO.setActivityName(this.getName());
      uaDTO.setUserName(username);
      ratings.add(uaDTO);
    }
  }

  public RecurringDTO getRecurring() {
    return recurring;
  }

  public void setRecurring(RecurringDTO recurring) {
    this.recurring = recurring;
  }


}
