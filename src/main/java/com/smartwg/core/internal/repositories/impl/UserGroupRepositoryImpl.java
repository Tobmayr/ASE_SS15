package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;
import com.smartwg.core.internal.repositories.UserGroupRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

@Named
public class UserGroupRepositoryImpl extends GenericRepositoryImpl<UserGroup> implements
    UserGroupRepository {

  @Override
  public UserGroup findById(final UserGroupId id) {
    return em.find(UserGroup.class, id);
  }

  @Override
  public List<UserGroup> findAdministratedGroups(final Integer userId) {
    final List<UserGroup> result = new ArrayList<>();
    for (UserGroup userGroup : findAll()) {
      if (userId.equals(userGroup.getUser().getId()) && Role.ADMIN.equals(userGroup.getRole())) {
        result.add(userGroup);
      }
    }
    return result;
  }
}
