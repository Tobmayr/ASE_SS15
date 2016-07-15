package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;

  @ManyToOne(optional = true)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL, CascadeType.DETACH},
      orphanRemoval = true)
  private List<UserGroupCategory> userGroupCategories = new ArrayList<UserGroupCategory>();

  @Column(name = "fixed_cost")
  private boolean fixedCost;

  @Column(name = "is_default")
  private boolean defaultCategory;

  @Column(name = "deleted")
  private boolean deleted;

  @OneToMany(mappedBy = "parentCategory", orphanRemoval = true)
  private List<Category> childCategories = new ArrayList<Category>();

  /**
   * default constructor for hibernate
   */
  public Category() {}

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
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

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public Category getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(Category parentCategory) {
    this.parentCategory = parentCategory;
  }

  public List<UserGroupCategory> getUserGroupCategories() {
    return userGroupCategories;
  }

  public void setUserGroupCategories(List<UserGroupCategory> userGroupCategories) {
    this.userGroupCategories = userGroupCategories;
  }

  public boolean isFixedCost() {
    return fixedCost;
  }

  public void setFixedCost(boolean fixedCost) {
    this.fixedCost = fixedCost;
  }

  public boolean isDefaultCategory() {
    return defaultCategory;
  }

  public void setDefaultCategory(boolean defaultCategory) {
    this.defaultCategory = defaultCategory;
  }

  public List<Category> getChildCategories() {
    return childCategories;
  }

  public void setChildCategories(List<Category> childCategories) {
    this.childCategories = childCategories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Category)) {
      return false;
    }

    Category category = (Category) o;

    return new EqualsBuilder().append(fixedCost, category.fixedCost)
        .append(defaultCategory, category.defaultCategory).append(id, category.id)
        .append(name, category.name).append(group, category.group)
        .append(parentCategory, category.parentCategory)
        .append(userGroupCategories, category.userGroupCategories).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(group).append(parentCategory)
        .append(userGroupCategories).append(fixedCost).append(defaultCategory).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("group", group)
        .append("fixedCost", fixedCost).append("defaultCategory", defaultCategory).toString();
  }
}
