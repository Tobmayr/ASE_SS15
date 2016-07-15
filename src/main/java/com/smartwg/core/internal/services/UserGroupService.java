package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;

/**
 * This Interface provides the basic service methods which are associated with UserGroup. e.g.
 * persisting and updating UserGroup in the database etc. The main purpose of these methods is to
 * wrap the functionality of the underlying UserGroupRepository and convert the entities to dtos for
 * use in higher-tiered layers
 * 
 * @author ozdesimsek
 */
public interface UserGroupService {

  /**
   * Finds user groups by id which are assigned to the given user
   *
   * @param username {@link String} of the user
   * @param groupId {@link Integer} with group id
   * @return {@link UserGroup} or <code>null</code> if no user group has been found
   */
  UserGroup findUserGroupByUsernameGroupId(String username, Integer groupId);

  /**
   * Persists a new user group in the database
   * 
   * @param userGroupDTO user group object (UserGroupDTO) wich should be stored
   * @throws NullPointerException if the passed parameter is null
   */
  void createUserGroup(UserGroupDTO userGroupDTO);
}
