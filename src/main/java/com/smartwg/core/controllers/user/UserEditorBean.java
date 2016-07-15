package com.smartwg.core.controllers.user;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.Language;
import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

@Named("userEditor")
@Scope("view")
public class UserEditorBean {
  private ResourceBundle valMs = SmartWG.getValidationBundle();
  private ResourceBundle ms = SmartWG.getMessageBundle();

  @Inject
  private NavigationBean navigation;
  @Inject
  private UserBean userBean;
  @Inject
  private GroupFacade groupFacade;
  @Inject
  private UserFacade userFacade;
  @Inject
  private SmartWG smartWg;
  private String notification;
  private UserDTO user;
  private List<GroupDTO> groups;
  private boolean editable = false;
  private String passwordMatch;
  private Language language = Language.ENGLISH;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserEditorBean.class);

  public UserEditorBean() {}

  @PostConstruct
  public void init() {
    user = userBean.getCurrentUser();
    groups = groupFacade.getAllGroupsByUserID(user.getId());
    language = Language.valueOfByLocale(user.getLocale());
    notification = notificationRepresentation();
  }

  public void discardChanges() {
    user = userBean.getCurrentUser();
    editable = false;
  }



  public String viewGroup(final GroupDTO group, final String returnPage) {
    return navigation.getPageGroupList() + Constants.PAGE_REDIRECT + "&GroupId=" + group.getId()
        + "&returnPage=" + returnPage;
  }

  public String notificationRepresentation() {
    List<NotificationType> list = user.getNotificationTypes();
    if (list.isEmpty()) {
      return ms.getString("lnone");
    } else {
      String result = "";
      for (NotificationType type : list) {
        result += type.getName() + ",";
      }

      return result.substring(0, result.lastIndexOf(","));
    }
  }

  public String unsubscripeGroup(GroupDTO group) {


    for (UserGroupDTO ug : group.getUserGroups()) {
      if (ug.getUserId().equals(user.getId())) {
        if (ug.getRole().equals(Role.ADMIN)) {
          PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
              valMs.getString("error.unsubscribeGroup.admin"));
          return navigation.getPageUserEdit();
        } else {
          userFacade.removeUserGroup(ug);
        }
      }
    }
    groups = groupFacade.getAllGroupsByUserID(user.getId());
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
        valMs.getString("info.unsubscribeGroup.successfull") + group.getName());


    return navigation.getPageUserEdit();
  }

  public boolean isUnsubscribed(GroupDTO group) {
    for (UserGroupDTO g : group.getUserGroups()) {
      if (g.getUserId().equals(user.getId()) && g.isDeleted()) {
        return true;
      }
    }
    return false;
  }

  public void editUser() {
    if (editable) {
      if (!user.getPassword_clear().equals(passwordMatch)) {
        PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
            valMs.getString("error.password.match"));
        return;
      }
      UserDTO oldUser = userBean.getCurrentUser();
      if (!oldUser.getEmail().equals(user.getEmail())
          || !oldUser.getUserName().equals(user.getUserName())) {
        if (!userFacade.verifyUnique(user.getUserName(), user.getEmail())) {
          PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
              valMs.getString("error.nameemail.unqiue"));
          return;
        }
      }
      user.setLocale(language.getLocale());
     
      LOGGER.info("LANGUAGE: " + user.getLocale());
      userFacade.updateUser(user);
      smartWg.setCurrent_locale(language.getLocale());
      smartWg.changeLanguage();
      editable = false;

      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
          valMs.getString("info.changedata.successfull"));
      userBean.setCurrentUser(user);

    } else {
      editable = true;
    }

  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public List<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDTO> groups) {
    this.groups = groups;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public String getPasswordMatch() {
    return passwordMatch;
  }

  public void setPasswordMatch(String passwordMatch) {
    this.passwordMatch = passwordMatch;
  }

  public List<NotificationType> getNotifications() {
    return Arrays.asList(NotificationType.BILL, NotificationType.SHOPPING_LIST,
        NotificationType.ACTIVITY);
  }

  public Language[] getLanguages() {
    return Language.values();
  }

  public Language getLanguage() {
    return language;
  }



  public String getNotification() {
    return notification;
  }

  public void setNotification(String notification) {
    this.notification = notification;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }


}
