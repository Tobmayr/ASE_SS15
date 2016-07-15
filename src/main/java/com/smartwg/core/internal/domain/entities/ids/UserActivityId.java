package com.smartwg.core.internal.domain.entities.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserActivityId implements Serializable {

  private Integer user;

  private Integer activity;

  /**
   * default constructor for hibernate
   */
  public UserActivityId() {}

  public UserActivityId(Integer user, Integer activity) {
    super();
    this.user = user;
    this.activity = activity;
  }

  public Integer getUser() {
    return user;
  }

  public void setUser(Integer user) {
    this.user = user;
  }

  public Integer getActivity() {
    return activity;
  }

  public void setActivity(Integer activity) {
    this.activity = activity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserActivityId)) {
      return false;
    }

    UserActivityId that = (UserActivityId) o;

    return new EqualsBuilder().append(user, that.user).append(activity, that.activity).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(user).append(activity).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", user).append("activity", activity).toString();
  }
}
