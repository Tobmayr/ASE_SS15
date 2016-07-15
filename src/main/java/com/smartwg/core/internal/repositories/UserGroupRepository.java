package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;

import java.util.List;

/**
 * This repository provides methods for CRUD-Operations for UserGroup-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * UserGroup-objects the methods provided by this interface directly return UserGroupDTOs to avoid
 * later typecasting or converting.
 *
 * @author Anna Sadriu (as)
 */
public interface UserGroupRepository extends GenericRepository<UserGroup> {

  /**
   * Returns the user group with the given id
   * 
   * @param id the user group id
   * @return the matching user group or NULL in case of no matching user group was found
   */
  UserGroup findById(UserGroupId id);

  /**
   * Returns the user groups which are administrated from the specified user
   * 
   * @param userid username of the user
   * @return List of the matching user groups or empty list in case of no matching user groups were
   *         found
   */
  List<UserGroup> findAdministratedGroups(Integer userid);
}
