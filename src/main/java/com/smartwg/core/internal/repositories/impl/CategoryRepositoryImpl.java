package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.repositories.CategoryRepository;
import com.smartwg.core.util.Constants;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamil Sierzant (ks) , Oezde Simsek (os)
 */
@Named
public class CategoryRepositoryImpl extends GenericRepositoryImpl<Category> implements
    CategoryRepository {



  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @SuppressWarnings("unchecked")
  public List<CategoryDTO> findAllDTOs() {
    final Query query = entityManager.createNamedQuery(Constants.QUERY_ALL_CATEGORIES);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CategoryDTO> findByGroup(Integer groupId) {
    final Query query = entityManager.createNamedQuery(Constants.QUERY_FIND_CATEGORY_BY_GROUP);
    return query.setParameter("groupId", groupId).getResultList();

  }
}
