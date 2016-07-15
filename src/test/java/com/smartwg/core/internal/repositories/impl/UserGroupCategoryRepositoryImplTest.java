package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.repositories.UserGroupCategoryRepository;
import com.smartwg.core.util.Constants;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupCategoryRepositoryImplTest {

  @InjectMocks
  private UserGroupCategoryRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private UserGroupCategoryDTO userGroupCategoryDTO;

  @BeforeMethod
  public void setUp() {
    repository = new UserGroupCategoryRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findByCategory() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_UGC_BY_CATEGORY)).thenReturn(query);
    when(query.setParameter("catId", 3)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(userGroupCategoryDTO));

    final List<UserGroupCategoryDTO> result = repository.findByCategory(3);

    assertEquals(result, Collections.singletonList(userGroupCategoryDTO));
  }
}
