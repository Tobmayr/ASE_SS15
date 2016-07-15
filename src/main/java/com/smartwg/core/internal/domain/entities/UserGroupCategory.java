package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.smartwg.core.internal.domain.entities.ids.UGCategoryId;

@Entity
@Table(name = "user_group_category")
@IdClass(UGCategoryId.class)
public class UserGroupCategory implements Serializable {

  private BigDecimal percent;

  @EmbeddedId
  @ManyToOne(optional = false, cascade = CascadeType.DETACH)
  @JoinColumns({
      @JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false,
          insertable = false),
      @JoinColumn(name = "group_id", referencedColumnName = "group_id", updatable = false,
          insertable = false)})
  private UserGroup userGroup;

  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "category_id", referencedColumnName = "id", updatable = false,
      insertable = false)
  @Id
  private Category category;

  /**
   * default constructor for hibernate
   */
  public UserGroupCategory() {}

  public BigDecimal getPercent() {
    return percent;
  }

  public void setPercent(BigDecimal percent) {
    this.percent = percent;
  }

  public UserGroup getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(UserGroup userGroup) {
    this.userGroup = userGroup;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserGroupCategory)) {
      return false;
    }

    UserGroupCategory that = (UserGroupCategory) o;

    return new EqualsBuilder().append(percent, that.percent).append(userGroup, that.userGroup)
        .append(category, that.category).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(percent).append(userGroup).append(category)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("percent", percent).append("userGroup", userGroup)
        .append("category", category).toString();
  }
}
