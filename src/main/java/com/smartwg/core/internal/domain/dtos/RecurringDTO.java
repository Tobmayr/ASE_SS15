package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.entities.Recurring;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Tobias Ortmayr (to)
 */
public class RecurringDTO {

  private RecurringType recurringType = RecurringType.DAY;
  private int value;
  private Date date = new Date();
  private Date endDate = new Date();
  private int times;
  private Integer id;
  private RepeatType repeatType = RepeatType.FOREVER;

  public RecurringDTO() {};

  public RecurringDTO(final Integer id, final int value, final RecurringType recurringType,
      final Date date, final Date endDate, final Integer times) {
    this.id = id;
    this.recurringType = recurringType;
    this.value = value;
    this.date = date;
    this.endDate = endDate;
    this.times = times;
  }

  public RecurringDTO(Recurring recurring) {
    this.id = recurring.getId();
    this.recurringType = recurring.getRecurringType();
    this.value = recurring.getValue();
    this.date = recurring.getDate();
    this.endDate = recurring.getEndDate();
    this.times = recurring.getTimes();
    this.repeatType = recurring.getRepeatType();
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
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.date = calendar.getTime();
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    if (endDate == null) {
      this.endDate = null;
      return;
    }
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(endDate);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.endDate = calendar.getTime();
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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



}
