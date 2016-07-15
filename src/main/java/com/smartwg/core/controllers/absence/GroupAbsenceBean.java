package com.smartwg.core.controllers.absence;

import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;

import org.springframework.context.annotation.Scope;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Anna Sadriu (as)
 */
@Named(value = "groupAbsence")
@Scope("view")
public class GroupAbsenceBean {

  // group absences overview
  private List<AbsenceDTO> groupAbsences;
  private List<AbsenceDTO> filteredGroupAbsences;

  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private UserBean userBean;

  @PostConstruct
  public void init() {
    groupAbsences = absenceFacade.getGroupAbsences(userBean.getCurrentUserGroup().getGroupId());
  }

  // getters & setters
  public List<AbsenceDTO> getGroupAbsences() {
    return groupAbsences;
  }

  public void setGroupAbsences(List<AbsenceDTO> groupAbsences) {
    this.groupAbsences = groupAbsences;
  }

  public List<AbsenceDTO> getFilteredGroupAbsences() {
    return filteredGroupAbsences;
  }

  public void setFilteredGroupAbsences(List<AbsenceDTO> filteredGroupAbsences) {
    this.filteredGroupAbsences = filteredGroupAbsences;
  }
}
