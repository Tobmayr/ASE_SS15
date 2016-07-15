package com.smartwg.core.internal.domain.entities;

import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.dtos.RepeatType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "recurring")
public class Recurring implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private RecurringType recurringType = RecurringType.DAY;

  private int value;

  private Date date;

  @Column(name = "end_date")
  private Date endDate;

  private int times;

  @Transient
  private RepeatType repeatType;

  /**
   * default constructor for hibernate
   */
  public Recurring() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public RecurringType getRecurringType() {
    return recurringType;
  }

  public void setRecurringType(RecurringType recurringType) {
    this.recurringType = recurringType;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }



  public RepeatType getRepeatType() {
    if (repeatType != null)
      return repeatType;
    else if (times == 0 && endDate == null)
      return RepeatType.FOREVER;
    else if (times > 0 && endDate == null)
      return RepeatType.TIMES;
    else if (times == 0 && endDate != null)
      return RepeatType.UNTIL;
    return null;
  }

  public void setRepeatType(RepeatType repeatType) {
    this.repeatType = repeatType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Recurring)) {
      return false;
    }

    Recurring recurring = (Recurring) o;

    return new EqualsBuilder().append(value, recurring.value).append(times, recurring.times)
        .append(id, recurring.id).append(recurringType, recurring.recurringType)
        .append(date, recurring.date).append(endDate, recurring.endDate).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(recurringType).append(value).append(date)
        .append(endDate).append(times).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("recurringType", recurringType)
        .append("value", value).append("date", date).append("endDate", endDate)
        .append("times", times).toString();
  }
}
