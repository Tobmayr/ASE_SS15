package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks), Anna Sadriu (as)
 */
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Date date;

  @Column(nullable = false)
  private int points;

  private int rating;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "recurring_id")
  public Recurring recurring;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;

  @ManyToOne(optional = false, cascade = CascadeType.DETACH)
  @JoinColumn(name = "created_by_user_id", nullable = false)
  private User createdBy;

  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "assigned_to_user_id")
  private User assignedTo;

  @Column(name = "is_done", nullable = false)
  private boolean isDone;

  @OneToMany(mappedBy = "activity")
  private List<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "activity", cascade = {CascadeType.PERSIST, CascadeType.DETACH})
  private List<UserActivity> ratings = new ArrayList<UserActivity>();

  // @OneToMany(mappedBy = "activity", cascade = CascadeType.DETACH)
  // private List<UserActivity> userActivity = new ArrayList<>();

  /**
   * default constructor for hibernate
   */
  public Activity() {}

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
    this.date = date;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public User getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(User assignedTo) {
    this.assignedTo = assignedTo;
  }

  public boolean isDone() {
    return isDone;
  }

  public void setIsDone(boolean isDone) {
    this.isDone = isDone;
  }

  public List<ShoppingList> getShoppingLists() {
    return shoppingLists;
  }

  public void setShoppingLists(List<ShoppingList> shoppingLists) {
    this.shoppingLists = shoppingLists;
  }

  public List<UserActivity> getRatings() {
    return ratings;
  }

  public void setRatings(List<UserActivity> ratings) {
    this.ratings = ratings;
  }

  public void addUserActivity(UserActivity userActivity) {
    ratings.add(userActivity);
  }

  // public List<UserActivity> getUserActivity() {
  // return userActivity;
  // }
  //
  // public void setUserActivity(List<UserActivity> userActivity) {
  // this.userActivity = userActivity;
  // }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Activity)) {
      return false;
    }

    Activity activity = (Activity) o;

    return new EqualsBuilder().append(points, activity.points).append(rating, activity.rating)
        .append(isDone, activity.isDone).append(id, activity.id).append(name, activity.name)
        .append(date, activity.date).append(group, activity.group)
        .append(createdBy, activity.createdBy).append(assignedTo, activity.assignedTo)
        .append(shoppingLists, activity.shoppingLists).append(ratings, activity.ratings).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(date).append(points)
        .append(rating).append(group).append(createdBy).append(assignedTo).append(isDone)
        .append(shoppingLists).append(ratings).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("date", date)
        .append("points", points).append("rating", rating).append("group", group)
        .append("createdBy", createdBy).append("assignedTo", assignedTo).append("isDone", isDone)
        .append("ratings", ratings).toString();
  }

  public Recurring getRecurring() {
    return recurring;
  }

  public void setRecurring(Recurring recurring) {
    this.recurring = recurring;
  }


}
