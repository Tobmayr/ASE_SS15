package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.util.Constants;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserActivityRepositoryImplTest {

  @InjectMocks
  private UserActivityRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private UserActivity userActivity;

  @BeforeMethod
  public void setUp() {
    repository = new UserActivityRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findByUserIdActivityIdSucceed() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_ACTIVITY)).thenReturn(query);
    when(query.setParameter("userId", 3)).thenReturn(query);
    when(query.setParameter("activityId", 2)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(userActivity));

    final UserActivity result = repository.findByUserIdActivityId(3, 2);

    assertEquals(result, userActivity);
  }

  public void findByUserIdActivityIdWhenNothingFound() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_ACTIVITY)).thenReturn(query);
    when(query.setParameter("userId", 3)).thenReturn(query);
    when(query.setParameter("activityId", 2)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.EMPTY_LIST);

    final UserActivity result = repository.findByUserIdActivityId(3, 2);

    assertNull(result);
  }
}
