package com.smartwg.core.controllers.group;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;

@Named("groupList")
@Scope("view")
public class GroupListBean {
  @Inject
  private GroupFacade facade;

  @Inject
  private UserFacade userFacade;
  @Inject
  private UserBean userBean;

  private List<GroupDTO> groups;
  private List<UserDTO> users;
  private GroupDTO group;

  @PostConstruct
  public void init() {
    groups = facade.getAllGroupsByUserID(userBean.getCurrentUser().getId());
    if (groups != null && !groups.isEmpty()) {
      group = groups.get(0);
      users = userFacade.findUsersByGroupID(group.getId());
    }
  }

  public void groupChanged() {
    users = userFacade.findUsersByGroupID(group.getId());
  }

  public List<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDTO> groups) {
    this.groups = groups;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public GroupDTO getGroup() {
    return group;
  }

  public void setGroup(GroupDTO group) {
    this.group = group;
  }


}
