package com.smartwg.core.facades;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;

@Transactional
/**
 * Central access point for service logic which is associated with user groups.
 * e.g. persisting, updating and searching user group in the database.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author ozdesimsek
 */
public interface UserGroupFacade {

  /**
   * Returns the user group with the given user and group
   * 
   * @param username username of the user
   * @param groupId ID of the group
   * @return the matching user group or NULL in case of no matching user group was found
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
