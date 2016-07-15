package com.smartwg.core.internal.domain.entities.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserGroupId implements Serializable {

  private Integer group;

  private Integer user;

  /**
   * default constructor for hibernate
   */
  public UserGroupId() {}

  public UserGroupId(Integer group, Integer user) {
    super();
    this.group = group;
    this.user = user;
  }

  public int hashCode() {
    return group + user;
  }

  public Integer getGroup() {
    return group;
  }

  public void setGroup(Integer group) {
    this.group = group;
  }

  public Integer getUser() {
    return user;
  }

  public void setUser(Integer user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserGroupId)) {
      return false;
    }

    UserGroupId that = (UserGroupId) o;

    return new EqualsBuilder().append(group, that.group).append(user, that.user).isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("group", group).append("user", user).toString();
  }
}
