package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.UserGroupFacade;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.services.UserGroupService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author ozdesimsek
 */
@Named
public class UserGroupFacadeImpl implements UserGroupFacade {

  @Inject
  private UserGroupService userGroupService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserGroupFacade#findUserGroupByUsernameGroupId(java.lang.String,
   *      java.lang.Integer)
   */

  @Override
  public UserGroup findUserGroupByUsernameGroupId(final String username, final Integer groupId) {
    return userGroupService.findUserGroupByUsernameGroupId(username, groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserGroupFacade#createUserGroup(com.smartwg.core.internal.domain.dtos.UserGroupDTO)
   */

  @Override
  public void createUserGroup(final UserGroupDTO userGroupDTO) {
    userGroupService.createUserGroup(userGroupDTO);
  }
}
