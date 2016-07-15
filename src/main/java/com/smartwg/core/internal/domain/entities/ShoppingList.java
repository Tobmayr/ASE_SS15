package com.smartwg.core.internal.domain.entities;

import com.smartwg.core.internal.domain.ShoppingListState;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "shopping_list")
public class ShoppingList implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Date deadline;

  @Column(name = "email_notification")
  private boolean emailNotification;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;

  @ManyToOne
  @JoinColumn(name = "activity_id")
  private Activity activity;

  @ManyToOne(optional = false)
  @JoinColumn(name = "created_by_user_id", nullable = false)
  private User createdBy;

  @ManyToOne(optional = true)
  @JoinColumn(name = "assigned_to_user_id", nullable = true)
  private User assignedTo;

  @Column(name = "is_private", nullable = false)
  private boolean privateList;

  @Enumerated(value = EnumType.STRING)
  private ShoppingListState state;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "resource_id")
  private Resource resource;

  @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ListPosition> listPositions = new ArrayList<>();

  @OneToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "bill_id")
  private Bill bill;

  /**
   * default constructor for hibernate
   */
  public ShoppingList() {}

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

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public boolean isEmailNotification() {
    return emailNotification;
  }

  public void setEmailNotification(boolean emailNotification) {
    this.emailNotification = emailNotification;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
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

  public boolean isPrivateList() {
    return privateList;
  }

  public void setPrivateList(boolean privateList) {
    this.privateList = privateList;
  }

  public ShoppingListState getState() {
    return state;
  }

  public void setState(ShoppingListState state) {
    this.state = state;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public List<ListPosition> getListPositions() {
    return listPositions;
  }

  public void setListPositions(List<ListPosition> listPositions) {
    this.listPositions = listPositions;
  }

  public void addListPosition(final ListPosition listPosition) {
    this.listPositions.add(listPosition);
  }

  public Bill getBill() {
    return bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ShoppingList)) {
      return false;
    }

    ShoppingList that = (ShoppingList) o;

    return new EqualsBuilder().append(emailNotification, that.emailNotification)
        .append(privateList, that.privateList).append(id, that.id).append(name, that.name)
        .append(deadline, that.deadline).append(group, that.group).append(activity, that.activity)
        .append(createdBy, that.createdBy).append(assignedTo, that.assignedTo)
        .append(state, that.state).append(resource, that.resource)
        .append(listPositions, that.listPositions).append(bill, that.bill).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(deadline)
        .append(emailNotification).append(group).append(activity).append(createdBy)
        .append(assignedTo).append(privateList).append(state).append(resource)
        .append(listPositions).append(bill).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("deadline", deadline).append("emailNotification", emailNotification)
        .append("group", group).append("activity", activity).append("createdBy", createdBy)
        .append("assignedTo", assignedTo).append("privateList", privateList).append("state", state)
        .append("resource", resource).toString();
  }
}
