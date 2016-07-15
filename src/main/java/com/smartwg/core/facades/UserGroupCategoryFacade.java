package com.smartwg.core.facades;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;

@Transactional
/**
 * Central access point for service logic which is associated with user group categories.
 * e.g. persisting, updating and searching user group category in the database.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author ozdesimsek
 */
public interface UserGroupCategoryFacade {

  /**
   * Persists a new user group category in the database
   * 
   * @param userGroupCategoryDTO user group category object (UserGroupCategoryDTO) wich should be stored
   * @throws NullPointerException if the passed parameter is null
   */
  void createUserGroupCategory(UserGroupCategoryDTO userGroupCategoryDTO);

  /**
   * Returns a List of all user group categories (as UserGroupCategoryDTO) for the given category (ID)
   * 
   * @param categoryId id of the category
   * @return a list with matching user group categories or an empty in case of no matching user group categories were found
   */
  List<UserGroupCategoryDTO> findByCategory(Integer categoryId);
}
