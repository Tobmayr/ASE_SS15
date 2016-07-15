package com.smartwg.core.controllers.user;

import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

@Named("resetPass")
@Scope("view")
public class ResetPassBean {
  private ResourceBundle valMs = SmartWG.getValidationBundle();
  @Inject
  private UserFacade facade;
  @Inject
  private NavigationBean navigation;
  private UserDTO user;
  private String userToken;
  private String passwordMatch;


  @PostConstruct
  public void init() {

    final Map<String, String> requestParameterMap =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String userId = requestParameterMap.get("userId");

    userToken = requestParameterMap.get("token");

    if (userId != null) {
      final Integer id = Integer.valueOf(userId);

      user = facade.findById(id);

    } else {
      user = new UserDTO();
    }

  }

  public String changePW() {
    if (user.getId() == null || !user.getConfirmCode().equals(userToken)) {
      return navigation.getPageIndex() + Constants.PAGE_REDIRECT + "&msg=" + "errror.change.failed";
    }
    if (!user.getPassword_clear().equals(passwordMatch)) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.password.match"));
      return null;
    }

    facade.updateUser(user);
    return navigation.getPageIndex() + Constants.PAGE_REDIRECT + "&msg="
        + "info.change.successfull" + "&uname=" + user.getUserName();

  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public String getPasswordMatch() {
    return passwordMatch;
  }

  public void setPasswordMatch(String passwordMatch) {
    this.passwordMatch = passwordMatch;
  }



}
