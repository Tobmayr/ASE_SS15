package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "feature")
public class Feature implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  private String description;

  @ManyToMany(mappedBy = "features")
  private List<Group> groups = new ArrayList<Group>();

  /**
   * default constructor for hibernate
   */
  public Feature() {}

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Feature)) {
      return false;
    }

    Feature feature = (Feature) o;

    return new EqualsBuilder().append(id, feature.id).append(name, feature.name)
        .append(description, feature.description).append(groups, feature.groups).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(description).append(groups)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("description", description).toString();
  }
}
