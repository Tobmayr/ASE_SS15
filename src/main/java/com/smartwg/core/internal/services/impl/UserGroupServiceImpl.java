package com.smartwg.core.internal.services.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;

import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserGroupService;

/**
 * @author ozdesimsek
 */
@Named
public class UserGroupServiceImpl implements UserGroupService {

  @Inject
  private UserGroupRepository userGroupRepository;
  @Inject
  private EntityConverter converter;
  @Inject
  private UserRepository userRepository;
  @Inject
  private GroupRepository groupRepository;

  @Override
  public UserGroup findUserGroupByUsernameGroupId(final String username, final Integer groupId) {
    final List<UserGroup> userGroups = userGroupRepository.findAll();
    for (UserGroup userGroup : userGroups) {
      if (groupId.equals(userGroup.getGroup().getId())
          && username.equals(userGroup.getUser().getUserName())) {
        return userGroup;
      }
    }
    return null;
  }

  @Override
  public void createUserGroup(UserGroupDTO userGroupDTO) {
    UserGroup ug = converter.createUserGroup(userGroupDTO);
    ug.setUser(userRepository.findById(ug.getUser().getId()));
    ug.setGroup(groupRepository.findById(ug.getGroup().getId()));
    ug.setJoinDate(DateTime.now().toDate());

    userGroupRepository.save(ug);
  }
}
