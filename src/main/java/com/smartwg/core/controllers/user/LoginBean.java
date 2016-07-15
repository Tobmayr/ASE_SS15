package com.smartwg.core.controllers.user;

import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.HttpUtil;
import com.smartwg.core.util.PrimefacesUtil;

@Named("login")
@Scope("view")
public class LoginBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginBean.class);
  private ResourceBundle valMs = SmartWG.getValidationBundle();
  @Inject
  private UserFacade userFacade;
  @Inject
  private GroupFacade groupFacade;
  @Inject
  private NavigationBean navigation;
  @Inject
  private UserBean userBean;
  @Inject
  private SmartWG smartWg;
  private String password;
  private String emailUsername;

  @PostConstruct
  public void init() {
    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    final String userId = requestParameterMap.get("userId");
    final String token = requestParameterMap.get("token");
    final String msg = requestParameterMap.get("msg");
    final String uname = requestParameterMap.get("uname");
    System.out.println("MSG IS : " + msg);
    if (userId != null && token != null) {
      confirmRegistration(userId, token);
    } else if (msg != null) {
      String message = valMs.getString(msg);
      if (uname != null) {
        message = message.replace(":user", uname);
      }
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO, message);
    }

  }

  private void confirmRegistration(String userId, String token) {
    boolean isConfirmed = userFacade.confirmUserRegistration(Integer.valueOf(userId), token);
    if (isConfirmed) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
          valMs.getString("info.user.registration.confirmed"));
    }

  }

  public String login() {
    UserDTO currentUser = userFacade.login(emailUsername, password);
    if (currentUser == null) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_WARN,
          valMs.getString("error.credentials.invalid"));
      return navigation.getPageIndex();
    } else {
      HttpSession session = HttpUtil.getSession();
      session.setAttribute("username", emailUsername);
      LOGGER.info(session.getAttribute("username").toString());
      smartWg.setCurrent_locale(currentUser.getLocale());
      smartWg.changeLanguage();
      if (currentUser.getGroups() != null && !currentUser.getGroups().isEmpty()) {
        userBean.setCurrentUser(currentUser);
        UserGroupDTO currentUserGroup = currentUser.getGroups().get(0);
        userBean.setCurrentUserGroup(currentUserGroup);
        userBean
            .setUsersOfCurrentGroup(userFacade.findUsersByGroupID(currentUserGroup.getGroupId()));
        userBean.setGroupsOfCurrentUser(groupFacade.getAllGroupsByUserID(currentUser.getId()));
        userBean.setCurrentGroup(groupFacade.findById(currentUserGroup.getGroupCountryId()));
        smartWg.addOnlineUser(currentUser);
      } else {
        userBean.setCurrentUser(currentUser);
        return navigation.getPageDashboardFreshUser();
      }

      return navigation.getPageHome();
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmailUsername() {
    return emailUsername;
  }

  public void setEmailUsername(String emailUsername) {
    this.emailUsername = emailUsername;
  }



}
