package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.ActivityRepository;
import com.smartwg.core.util.Constants;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ActivityRepositoryImplTest {

  private static final Integer GROUP_ID = 1;
  private static final Integer ACTIVITY_ID = 2;
  private static final Integer USER_ID = 3;

  @InjectMocks
  private ActivityRepository repository;

  @Mock
  private EntityManager entityManager;

  @Mock
  private Query query;
  @Mock
  private ActivityDTO activityDTO;
  @Mock
  private UserActivityDTO userActivityDTO;
  @Mock
  private Activity activity;
  @Mock
  private UserActivity userActivity;
  @Mock
  private User user;
  @Mock
  private Group group;

  @BeforeMethod
  public void setUp() {
    repository = new ActivityRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getActivitiesForGroup() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_ACTIVITY_BY_GROUP_ID)))
        .thenReturn(query);
    when(query.setParameter("groupId", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activityDTO));

    final List<ActivityDTO> result = repository.getActivitiesForGroup(GROUP_ID);

    assertEquals(result, Collections.singletonList(activityDTO));
  }

  public void getRatingsForActivity() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_RATINGS_BY_ACTIVITY_ID))).thenReturn(
        query);
    when(query.setParameter("activityId", ACTIVITY_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(userActivityDTO));

    final List<UserActivityDTO> result = repository.getRatingsForActivity(ACTIVITY_ID);

    assertEquals(result, Collections.singletonList(userActivityDTO));
  }

  public void getActivitiesBetweenTimeSpan() {
    final Date start = new Date();
    final Date end = new Date();

    when(entityManager.createNamedQuery(eq(Constants.QUERY_ACTIVITIES_BY_TIMESPAN))).thenReturn(
        query);
    when(query.setParameter("start", start)).thenReturn(query);
    when(query.setParameter("end", end)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activity));

    final List<Activity> result = repository.getActivitiesBetweenTimespan(start, end, GROUP_ID);

    assertEquals(result, Collections.singletonList(activity));
  }

  public void findActivityByIdSucceed() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ACTIVITY_BY_ID))).thenReturn(query);
    when(query.setParameter("id", ACTIVITY_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activityDTO));

    final ActivityDTO result = repository.findActivityById(ACTIVITY_ID);

    assertEquals(result, activityDTO);
  }

  public void findActivityByIdFailsWhenActivityNotFound() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ACTIVITY_BY_ID))).thenReturn(query);
    when(query.setParameter("id", ACTIVITY_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.EMPTY_LIST);

    final ActivityDTO result = repository.findActivityById(ACTIVITY_ID);

    assertNull(result);
  }

  public void getAssignedActivitesBetweenTimespan() {
    final Date start = new Date();
    final Date end = new Date();

    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_ASSIGNED_ACTIVITES_TIMESPAN)))
        .thenReturn(query);
    when(query.setParameter("start", start)).thenReturn(query);
    when(query.setParameter("end", end)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.setParameter("user", USER_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activityDTO));

    final List<ActivityDTO> result =
        repository.getAssignedActivitesBetweenTimespan(start, end, GROUP_ID, USER_ID);

    assertEquals(result, Collections.singletonList(activityDTO));
  }

  @Test(dataProvider = "parameters")
  public void findByParametersWithAllParameters(Date start, Date end, String isDone,
      String expectedQuery) {

    when(entityManager.createQuery(eq(expectedQuery))).thenReturn(query);
    when(query.setParameter("start", start, TemporalType.DATE)).thenReturn(query);
    when(query.setParameter("end", end, TemporalType.DATE)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activity));
    when(activity.getRatings()).thenReturn(Collections.singletonList(userActivity));
    when(activity.getCreatedBy()).thenReturn(user);
    when(activity.getAssignedTo()).thenReturn(user);
    when(activity.getGroup()).thenReturn(group);
    when(userActivity.getUser()).thenReturn(user);
    when(userActivity.getActivity()).thenReturn(activity);

    final List<ActivityDTO> result =
        repository.findByParameters(GROUP_ID, 5, start, end, isDone, 6);

    assertEquals(result.get(0).getRatings().get(0).getUser(), user);
  }

  @DataProvider(name = "parameters")
  private Object[][] getParameters() {
    return new Object[][] {
        {
            new Date(),
            new Date(),
            "true",
            "SELECT a FROM Activity a where a.group.id="
                + GROUP_ID
                + " AND a.id=6 AND a.assignedTo.id=5 AND a.isDone=true AND ( a.date BETWEEN :start AND :end) ORDER BY a.date DESC"},
        {
            new Date(),
            new Date(),
            "false",
            "SELECT a FROM Activity a where a.group.id="
                + GROUP_ID
                + " AND a.id=6 AND a.assignedTo.id=5 AND a.isDone=false AND ( a.date BETWEEN :start AND :end) ORDER BY a.date DESC"},
        {
            new Date(),
            null,
            "false",
            "SELECT a FROM Activity a where a.group.id="
                + GROUP_ID
                + " AND a.id=6 AND a.assignedTo.id=5 AND a.isDone=false AND ( a.date >= :start) ORDER BY a.date DESC"},
        {
            null,
            new Date(),
            "false",
            "SELECT a FROM Activity a where a.group.id="
                + GROUP_ID
                + " AND a.id=6 AND a.assignedTo.id=5 AND a.isDone=false AND ( a.date <= :end) ORDER BY a.date DESC"},
        {
            null,
            null,
            "false",
            "SELECT a FROM Activity a where a.group.id=" + GROUP_ID
                + " AND a.id=6 AND a.assignedTo.id=5 AND a.isDone=false ORDER BY a.date DESC"},};
  }

  public void findByParametersWhenGroupIdNull() {
    final Date start = new Date();
    final Date end = new Date();

    final List<ActivityDTO> result = repository.findByParameters(null, 5, start, end, "true", 6);

    assertTrue(result.isEmpty());
  }

  public void findAllDTOs() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_ACTIVITIES))).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(activityDTO));

    final List<ActivityDTO> result = repository.findAllDTOs();

    assertEquals(result, Collections.singletonList(activityDTO));
  }
}
