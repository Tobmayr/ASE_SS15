package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.entities.UserGroupCategory;
import com.smartwg.core.internal.repositories.UserGroupCategoryRepository;
import com.smartwg.core.util.Constants;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class UserGroupCategoryRepositoryImpl extends GenericRepositoryImpl<UserGroupCategory>
    implements UserGroupCategoryRepository {

  @SuppressWarnings("unchecked")
  @Override
  public List<UserGroupCategoryDTO> findByCategory(Integer categoryId) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_UGC_BY_CATEGORY);
    return query.setParameter("catId", categoryId).getResultList();
  }
}
