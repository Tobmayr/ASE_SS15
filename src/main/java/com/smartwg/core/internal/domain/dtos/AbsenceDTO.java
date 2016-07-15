package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Anna Sadriu (as)
 */
public class AbsenceDTO {

  private Integer id;
  private Date awayFrom = new Date();
  private Date awayTill = new Date();
  private boolean temporary = true;
  private UserGroupDTO userGroup;


  /**
   * Constructor for creating new shopping lists
   */
  public AbsenceDTO() {}

  /**
   * Constructor for named query
   *
   * @param id {@link Long}
   * @param awayFrom {@link Date}
   * @param awayTill {@link Date}
   * @param temporary {@link boolean}
   */
  public AbsenceDTO(final Integer id, final Date awayFrom, final Date awayTill,
      final boolean temporary) {
    this.id = id;
    this.awayFrom = awayFrom;
    this.awayTill = awayTill;
    this.temporary = temporary;
  }

  /**
   * Constructor with user
   *
   * @param id {@link Long}
   * @param awayFrom {@link Date}
   * @param awayTill {@link Date}
   * @param temporary {@link boolean}
   * @param user {@link User}
   */
  public AbsenceDTO(final Integer id, final Date awayFrom, final Date awayTill,
      final boolean temporary, final UserGroup userGroup) {
    this.id = id;
    this.awayFrom = awayFrom;
    this.awayTill = awayTill;
    this.temporary = temporary;
    if (userGroup != null) {
      this.userGroup = new UserGroupDTO(userGroup);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getAwayFrom() {
    return awayFrom;
  }

  public void setAwayFrom(Date awayFrom) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(awayFrom);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.awayFrom = calendar.getTime();
  }

  public Date getAwayTill() {

    return awayTill;
  }

  public void setAwayTill(Date awayTill) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(awayTill);
    calendar.set(Calendar.HOUR_OF_DAY, 12);
    this.awayTill = calendar.getTime();
  }

  public boolean isTemporary() {
    return temporary;
  }

  public void setTemporary(boolean temporary) {
    this.temporary = temporary;
  }

  public UserGroupDTO getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(UserGroupDTO userGroup) {
    this.userGroup = userGroup;
  }


}
