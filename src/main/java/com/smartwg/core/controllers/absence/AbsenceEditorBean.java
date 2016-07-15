package com.smartwg.core.controllers.absence;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Anna Sadriu (as)
 */
@Named(value = "absenceBean")
@Scope(value = "view")
public class AbsenceEditorBean {

  // validation messages
  private static final String VALIDATION_WARN_DATE_ORDER =
      "The 'Away from' date should be before or equal the 'Away till' date.";

  private AbsenceDTO currentAbsence;
  private GroupDTO currentGroup;

  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private GroupFacade groupFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;

  @PostConstruct
  public void init() {
    String absenceId =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
            .get("absenceId");
    if (absenceId != null) {
      final Integer integer = Integer.valueOf(absenceId);
      currentAbsence = absenceFacade.getAbsenceById(integer);
    } else {
      currentAbsence = new AbsenceDTO();
    }
    currentAbsence.setUserGroup(userBean.getCurrentUserGroup());
    if (currentGroup == null) {
      currentGroup = groupFacade.findById(userBean.getCurrentUserGroup().getGroupId());
    }
  }

  public String saveAbsence() {
    if (currentAbsence.getAwayTill().before(currentAbsence.getAwayFrom())) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_WARN, VALIDATION_WARN_DATE_ORDER);
      return null;
    }
    currentAbsence.setUserGroup(userBean.getCurrentUserGroup());
    absenceFacade.saveAbsence(currentAbsence);
    return navigation.getPageUserAbsences();
  }

  // getters & setters
  public AbsenceDTO getCurrentAbsence() {
    return currentAbsence;
  }

  public void setCurrentAbsence(AbsenceDTO currentAbsence) {
    this.currentAbsence = currentAbsence;
  }
}
