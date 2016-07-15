package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.services.UserGroupCategoryService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author ozdesimsek
 */
@Named
public class UserGroupCategoryFacadeImpl implements UserGroupCategoryFacade {

  @Inject
  private UserGroupCategoryService service;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserGroupCategoryFacade#createUserGroupCategory(com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO)
   */

  @Override
  public void createUserGroupCategory(final UserGroupCategoryDTO userGroupCategoryDTO) {
    service.createUserGroupCategory(userGroupCategoryDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserGroupCategoryFacade#findByCategory(java.lang.Integer)
   */

  @Override
  public List<UserGroupCategoryDTO> findByCategory(final Integer categoryId) {
    return service.findByCategory(categoryId);
  }
}
