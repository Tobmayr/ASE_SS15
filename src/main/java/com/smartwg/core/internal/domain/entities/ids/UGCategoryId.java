package com.smartwg.core.internal.domain.entities.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class UGCategoryId implements Serializable {

  private UserGroupId userGroup;
  private Integer category;

  /**
   * default constructor for hibernate
   */
  public UGCategoryId() {}

  public UserGroupId getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(UserGroupId userGroup) {
    this.userGroup = userGroup;
  }

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UGCategoryId)) {
      return false;
    }

    UGCategoryId that = (UGCategoryId) o;

    return new EqualsBuilder().append(userGroup, that.userGroup).append(category, that.category)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(userGroup).append(category).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("userGroup", userGroup).append("category", category)
        .toString();
  }
}
