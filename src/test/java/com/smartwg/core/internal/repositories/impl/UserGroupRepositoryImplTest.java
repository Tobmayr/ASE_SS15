package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;
import com.smartwg.core.internal.repositories.UserGroupRepository;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupRepositoryImplTest {

  @InjectMocks
  private UserGroupRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private CriteriaQuery query;
  @Mock
  private UserGroup userGroup;
  @Mock
  private CriteriaBuilder criteriaBuilder;
  @Mock
  private Root rootQuery;
  @Mock
  private TypedQuery typedQuery;
  @Mock
  private User user;
  @Mock
  private UserGroupId userGroupId;

  @BeforeMethod
  public void setUp() {
    repository = new UserGroupRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findById() {
    when(entityManager.find(UserGroup.class, userGroupId)).thenReturn(userGroup);

    final UserGroup result = repository.findById(userGroupId);

    assertEquals(result, userGroup);
  }

  public void findAdministratedGroups() {
    when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(UserGroup.class)).thenReturn(query);
    when(query.from(UserGroup.class)).thenReturn(rootQuery);
    when(entityManager.createQuery(query)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(Collections.singletonList(userGroup));
    when(userGroup.getUser()).thenReturn(user);
    when(userGroup.getRole()).thenReturn(Role.ADMIN);
    when(user.getId()).thenReturn(2);

    final List<UserGroup> result = repository.findAdministratedGroups(2);

    assertEquals(result, Collections.singletonList(userGroup));
  }
}
