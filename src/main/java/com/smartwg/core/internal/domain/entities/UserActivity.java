package com.smartwg.core.internal.domain.entities;

import com.smartwg.core.internal.domain.entities.ids.UserActivityId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Entity
@Table(name = "user_activity")
@IdClass(UserActivityId.class)
public class UserActivity implements Serializable {

  @Column(nullable = false)
  private Integer rating;

  @Id
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "user_id", updatable = true, insertable = true, referencedColumnName = "id")
  private User user;

  @Id
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "activity_id", updatable = true, insertable = true,
      referencedColumnName = "id")
  private Activity activity;

  /**
   * default constructor for hibernate
   */
  public UserActivity() {}

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

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserActivity)) {
      return false;
    }

    UserActivity that = (UserActivity) o;

    return new EqualsBuilder().append(rating, that.rating).append(user, that.user)
        .append(activity, that.activity).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(rating).append(user).append(activity).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("rating", rating).append("user", user)
        .append("activity", activity).toString();
  }
}
