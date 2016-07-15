package com.smartwg.core.controllers.absence;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Anna Sadriu (as)
 */
@Named(value = "userAbsence")
@Scope("view")
public class UserAbsenceBean {

  // validation messages
  // private static final String VALIDATION_INFO_ABSENCE_CREATED =
  // "Absence %s successfully created.";
  private static final String VALIDATION_INFO_ABSENCE_DELETED = "Absence %s successfully deleted.";

  private static final String REDIRECT = "?faces-redirect=true";

  // user absences overview
  private List<AbsenceDTO> myAbsences;
  private List<AbsenceDTO> filteredUserAbsences;

  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;

  @PostConstruct
  public void init() {
    myAbsences =
        absenceFacade.getUserAbsences(userBean.getCurrentUser().getId(), userBean.getCurrentGroup()
            .getId());
  }

  public String createAbsence() {
    return navigation.getPageNewAbsence();
  }

  public String editAbsence(final AbsenceDTO absenceDTO) {
    return navigation.getPageNewAbsence() + REDIRECT + "&absenceId=" + absenceDTO.getId();
  }

  public String deleteAbsence(final AbsenceDTO selectedAbsenceDTO) {
    absenceFacade.deleteAbsence(selectedAbsenceDTO.getId());
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
        String.format(VALIDATION_INFO_ABSENCE_DELETED, selectedAbsenceDTO.getId()));

    return navigation.getPageUserAbsences();
  }

  // getters and setters
  public List<AbsenceDTO> getMyAbsences() {
    return myAbsences;
  }

  public void setMyAbsences(List<AbsenceDTO> myAbsences) {
    this.myAbsences = myAbsences;
  }

  public List<AbsenceDTO> getFilteredUserAbsences() {
    return filteredUserAbsences;
  }

  public void setFilteredUserAbsences(List<AbsenceDTO> filteredUserAbsences) {
    this.filteredUserAbsences = filteredUserAbsences;
  }
}
