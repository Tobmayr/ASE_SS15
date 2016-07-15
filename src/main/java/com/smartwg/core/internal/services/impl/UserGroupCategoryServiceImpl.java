package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.UserGroupCategory;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;
import com.smartwg.core.internal.repositories.CategoryRepository;
import com.smartwg.core.internal.repositories.UserGroupCategoryRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserGroupCategoryService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author ozdesimsek
 */
@Named
public class UserGroupCategoryServiceImpl implements UserGroupCategoryService {

  @Inject
  private UserGroupCategoryRepository repository;
  @Inject
  private UserGroupRepository userGroupRepository;
  @Inject
  private CategoryRepository categoryRepository;
  @Inject
  private EntityConverter entityFactory;

  @Override
  public void createUserGroupCategory(final UserGroupCategoryDTO userGroupCategoryDTO) {
    final UserGroupCategory entity = entityFactory.createUserGroupCategory(userGroupCategoryDTO);

    final Category category =
        categoryRepository.findById(userGroupCategoryDTO.getCategory().getId());
    final UserGroup userGroup =
        userGroupRepository.findById(new UserGroupId(entity.getUserGroup().getGroup().getId(),
            entity.getUserGroup().getUser().getId()));
    entity.setCategory(category);
    entity.setUserGroup(userGroup);
    repository.save(entity);
  }

  @Override
  public List<UserGroupCategoryDTO> findByCategory(final Integer categoryId) {
    return repository.findByCategory(categoryId);
  }
}
