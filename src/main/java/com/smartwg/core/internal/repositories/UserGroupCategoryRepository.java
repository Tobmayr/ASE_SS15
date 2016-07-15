package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.entities.UserGroupCategory;

import java.util.List;

/**
 * This repository provides methods for CRUD-Operations for UserGroupCategory-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * UserGroupCategory-objects the methods provided by this interface directly return UserGroupCategoryDTOs to avoid later
 * typecasting or converting.
 *
 * @author Anna Sadriu (as)
 */
public interface UserGroupCategoryRepository extends GenericRepository<UserGroupCategory> {

  /**
   * Returns a List of all user group categories (as UserGroupCategoryDTO) for the given category (ID)
   * 
   * @param categoryId id of the category
   * @return a list with matching user group categories or an empty in case of no matching user group categories were found
   */
  List<UserGroupCategoryDTO> findByCategory(Integer categoryId);
}
