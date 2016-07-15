package com.smartwg.core.controllers.user;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.HttpUtil;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Özde Simsek (os), Tobias Ortmayr (to)
 */
@Named("registrationBean")
@Scope("view")
public class RegistrationBean {

  private ResourceBundle ms = SmartWG.getMessageBundle();

  private ResourceBundle valMs = SmartWG.getValidationBundle();

  @Inject
  private UserFacade userFacade;
  @Inject
  private NavigationBean navigation;
  private UserDTO currentUser;
  private UserGroupDTO currentUserGroup;
  private String income;
  private String passwordMatch;
  private boolean gtc;

  @PostConstruct
  public void init() {
    if (currentUser == null) {
      currentUser = new UserDTO();
    }

    currentUser.setNotificationTypes(getNotifications());
  }

  public UserDTO getCurrentUser() {
    return currentUser;
  }


  public void setCurrentUser(UserDTO currentUser) {
    this.currentUser = currentUser;
  }


  public UserGroupDTO getCurrentUserGroup() {
    return currentUserGroup;
  }


  public void setCurrentUserGroup(UserGroupDTO currentUserGroup) {
    this.currentUserGroup = currentUserGroup;
  }

  public String createUser() {
    if (!currentUser.getPassword_clear().equals(passwordMatch)) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.password.match"));
      return null;
    }
    if (!userFacade.verifyUnique(currentUser.getUserName(), currentUser.getEmail())) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.nameemail.unqiue"));
      return null;
    }

    final String userToken = UUID.randomUUID().toString();
    currentUser.setConfirmCode(userToken);
    currentUser.setConfirmed(true);// TODO change to false in order to enable registration
                                   // confirmation

    currentUser = userFacade.createUser(currentUser);

    String baseURL = HttpUtil.getBaseURL();
    String pageLink =
        baseURL + navigation.getPageIndex() + ".jsf" + "?userId=" + currentUser.getId() + "&token="
            + userToken;
    userFacade.sendRegistrationConfirmationMail(currentUser, pageLink);

    return navigation.getPageIndex() + Constants.PAGE_REDIRECT + "&msg="
        + "info.registration.successfull";
  }

  public String resetPassword() {
    UserDTO result = userFacade.findByEmail(currentUser.getEmail());
    if (result == null) {
      PrimefacesUtil
          .addValidationMessage(FacesMessage.SEVERITY_ERROR, ms.getString("mailNotInUse"));
      return navigation.getPageForgotpass();
    } else {
      String baseURL = HttpUtil.getBaseURL();

      final String userToken = UUID.randomUUID().toString();
      result.setConfirmCode(userToken);
      result.setPassword_clear(null);
      userFacade.updateUser(result);
      String pageLink =
          baseURL + navigation.getPageResetPass() + ".jsf" + "?userId=" + result.getId()
              + "&token=" + userToken;
      userFacade.sendPassworResetMail(result, pageLink);
    }
    return navigation.getPageIndex() + Constants.PAGE_REDIRECT + "&msg=" + "info.resetrequest";
  }

  public String getIncome() {
    return income;
  }


  public void setIncome(String income) {
    this.income = income;
  }


  public String getPasswordMatch() {
    return passwordMatch;
  }

  public void setPasswordMatch(String passwordMatch) {
    this.passwordMatch = passwordMatch;
  }

  public boolean isGtc() {
    return gtc;
  }


  public void setGtc(boolean gtc) {
    this.gtc = gtc;
  }

  public List<NotificationType> getNotifications() {
    return Arrays.asList(NotificationType.BILL, NotificationType.SHOPPING_LIST,
        NotificationType.ACTIVITY);
  }
}
