package com.smartwg.core.controllers.user;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.facades.UserGroupFacade;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;


/**
 * @author Özde Simsek (os), Tobias Ortmayr (to)
 */
@Named("userBean")
@Scope("session")
public class UserBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);

  @Inject
  private NavigationBean navigation;
  @Inject
  private UserGroupFacade userGroupFacade;
  @Inject
  private UserFacade userFacade;
  @Inject
  private SmartWG smartWg;
  private UserDTO currentUser;
  private UserGroupDTO currentUserGroup;
  private GroupDTO currentGroup;
  private List<UserDTO> usersOfCurrentGroup;
  private List<GroupDTO> groupsOfCurrentUser;


  @PostConstruct
  public void init() {
    currentUser = new UserDTO();
    currentUser.setLocale("en");
  }

  public void changeGroup() {
    currentUserGroup =
        new UserGroupDTO(userGroupFacade.findUserGroupByUsernameGroupId(currentUser.getUserName(),
            currentGroup.getId()));
    usersOfCurrentGroup = userFacade.findUsersByGroupID(currentGroup.getId());
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    try {
      context.redirect(HttpUtil.getBaseURL() + navigation.getPageHome() + ".jsf");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String logout() {
    LOGGER.info("logout");
    HttpSession session = HttpUtil.getSession();
    session.invalidate();
    smartWg.removeOnlineUser(currentUser);
    currentUserGroup = null;
    usersOfCurrentGroup = null;
    this.currentUser = null;
    return navigation.getPageIndex();
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

  public List<UserDTO> getUsersOfCurrentGroup() {
    return usersOfCurrentGroup;
  }

  public void setUsersOfCurrentGroup(List<UserDTO> usersOfCurrentGroup) {
    this.usersOfCurrentGroup = usersOfCurrentGroup;
  }


  public List<GroupDTO> getGroupsOfCurrentUser() {
    return groupsOfCurrentUser;
  }


  public void setGroupsOfCurrentUser(List<GroupDTO> groupsOfCurrentUser) {
    this.groupsOfCurrentUser = groupsOfCurrentUser;
  }


  public GroupDTO getCurrentGroup() {
    return currentGroup;
  }


  public void setCurrentGroup(GroupDTO currentGroup) {
    this.currentGroup = currentGroup;
  }



}
