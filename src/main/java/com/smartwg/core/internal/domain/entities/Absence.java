package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks),Anna Sadriu (as)
 */
@Entity
@Table(name = "absence")
public class Absence implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
  @JoinColumns({@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
      @JoinColumn(name = "group_id", referencedColumnName = "group_id")})
  private UserGroup userGroup;

  @Column(name = "away_from", nullable = false)
  private Date awayFrom;

  // TODO rename to away_till (double l)
  @Column(name = "away_til", nullable = false)
  private Date awayTill;

  private boolean temporary;

  /**
   * default constructor for hibernate
   */
  public Absence() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserGroup getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(UserGroup userGroup) {
    this.userGroup = userGroup;
  }

  public Date getAwayFrom() {
    return awayFrom;
  }

  public void setAwayFrom(Date awayFrom) {
    this.awayFrom = awayFrom;
  }

  public Date getAwayTill() {
    return awayTill;
  }

  public void setAwayTill(Date awayTill) {
    this.awayTill = awayTill;
  }

  public boolean isTemporary() {
    return temporary;
  }

  public void setTemporary(boolean temporary) {
    this.temporary = temporary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Absence)) {
      return false;
    }

    Absence absence = (Absence) o;

    return new EqualsBuilder().append(temporary, absence.temporary).append(id, absence.id)
        .append(userGroup, absence.userGroup).append(awayFrom, absence.awayFrom)
        .append(awayTill, absence.awayTill).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(userGroup).append(awayFrom)
        .append(awayTill).append(temporary).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("userGroup", userGroup)
        .append("awayFrom", awayFrom).append("awayTil", awayTill).append("temporary", temporary)
        .toString();
  }
}
