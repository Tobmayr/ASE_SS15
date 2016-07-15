package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;

import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with UserGroupCategory.
 * e.g. persisting and updating UserGroupCategory in the database etc. The main purpose of these
 * methods is to wrap the functionality of the underlying UserGroupCategoryRepository and convert
 * the entities to dtos for use in higher-tiered layers
 * 
 * @author ozdesimsek
 */
public interface UserGroupCategoryService {

  /**
   * Persists a new user group category in the database
   * 
   * @param userGroupCategoryDTO user group category object (UserGroupCategoryDTO) wich should be
   *        stored
   * @throws NullPointerException if the passed parameter is null
   */
  void createUserGroupCategory(UserGroupCategoryDTO userGroupCategoryDTO);

  /**
   * Returns a List of all user group categories (as UserGroupCategoryDTO) for the given category
   * (ID)
   * 
   * @param categoryId id of the category
   * @return a list with matching user group categories or an empty in case of no matching user
   *         group categories were found
   */
  List<UserGroupCategoryDTO> findByCategory(Integer categoryId);
}
