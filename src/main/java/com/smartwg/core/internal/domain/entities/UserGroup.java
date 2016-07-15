package com.smartwg.core.internal.domain.entities;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;

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
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_group")
@IdClass(UserGroupId.class)
public class UserGroup implements Serializable {

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  private boolean deleted;

  private double balance;
  @Column(name = "join_date")
  private Date joinDate;
  @Column(name = "leave_date")
  private Date leaveDate;

  @Id
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "user_id", updatable = false, insertable = false, referencedColumnName = "id")
  private User user;

  @JoinColumn(name = "group_id", updatable = false, insertable = false, referencedColumnName = "id")
  @Id
  @ManyToOne(cascade = CascadeType.DETACH)
  private Group group;

  @OneToMany(mappedBy = "userGroup")
  private List<UserGroupCategory> userGroupCategories = new ArrayList<UserGroupCategory>();

  @OneToMany(mappedBy = "userGroup")
  private List<Absence> absences = new ArrayList<Absence>();

  /**
   * default constructor for hibernate
   */
  public UserGroup() {}


  public boolean isDeleted() {
    return deleted;
  }


  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }


  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public List<UserGroupCategory> getUserGroupCategories() {
    return userGroupCategories;
  }

  public void setUserGroupCategories(List<UserGroupCategory> userGroupCategories) {
    this.userGroupCategories = userGroupCategories;
  }



  public double getBalance() {
    return balance;
  }


  public void setBalance(double balance) {
    this.balance = balance;
  }


  public List<Absence> getAbsences() {
    return absences;
  }


  public void setAbsences(List<Absence> absences) {
    this.absences = absences;
  }


  public Date getJoinDate() {
    return joinDate;
  }


  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }


  public Date getLeaveDate() {
    return leaveDate;
  }


  public void setLeaveDate(Date leaveDate) {
    this.leaveDate = leaveDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserGroup)) {
      return false;
    }

    UserGroup userGroup = (UserGroup) o;

    return new EqualsBuilder().append(role, userGroup.role).append(user, userGroup.user)
        .append(group, userGroup.group).append(userGroupCategories, userGroup.userGroupCategories)
        .append(balance, userGroup.getBalance()).append(joinDate, userGroup.joinDate)
        .append(leaveDate, userGroup.leaveDate).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(role).append(user).append(group)
        .append(userGroupCategories).append(balance).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("role", role).append("user", user)
        .append("group", group).append("balance", balance).append("join", joinDate)
        .append("leave", leaveDate).toString();
  }

}
