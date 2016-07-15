package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.services.ActivityService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class ActivityFacadeImplTest {

  private static final Integer GROUP_ID = 2;
  private static final Integer ACTIVITY_ID = 3;
  private static final Integer USER_ID = 4;
  private static final Date START = new Date();
  private static final Date END = new Date();

  @InjectMocks
  private ActivityFacade facade;

  @Mock
  private ActivityService activityService;
  @Mock
  private Activity activity;
  @Mock
  private ActivityDTO activityDTO;
  @Mock
  private UserActivityDTO userActivityDTO;

  @BeforeMethod
  public void setUp() {
    facade = new ActivityFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createActivity() {
    when(activityDTO.getId()).thenReturn(null);

    facade.createActivity(activityDTO);

    verify(activityService, times(1)).createActivity(eq(activityDTO));
  }

  public void updateActivity() {
    when(activityDTO.getId()).thenReturn(2);

    facade.updateActivity(activityDTO, USER_ID);

    verify(activityService, times(1)).updateActivity(eq(activityDTO), eq(USER_ID));
  }

  public void deleteActivity() {
    facade.deleteActivity(activityDTO);

    verify(activityService, times(1)).deleteActivity(eq(activityDTO));
  }

  public void getAllActivities() {
    when(activityService.getAllActivities()).thenReturn(singletonList(activity));

    final List<Activity> result = facade.getAllActivities();

    assertEquals(result, singletonList(activity));
  }

  public void getActivitiesForGroup() {
    when(activityService.getActivitiesForGroup(eq(GROUP_ID)))
        .thenReturn(singletonList(activityDTO));

    final List<ActivityDTO> result = facade.getActivitiesForGroup(GROUP_ID);

    assertEquals(result, singletonList(activityDTO));
  }

  public void getRatingsForActivity() {
    when(activityService.getRatingsForActivity(eq(ACTIVITY_ID))).thenReturn(
        singletonList(userActivityDTO));

    final List<UserActivityDTO> result = facade.getRatingsForActivity(ACTIVITY_ID);

    assertEquals(result, singletonList(userActivityDTO));
  }

  public void getActivitiesBetweenTimeSpan() {
    final Date start = new Date();
    final Date end = new Date();

    when(activityService.getActivitiesBetweenTimespan(eq(start), eq(end), eq(GROUP_ID)))
        .thenReturn(singletonList(activityDTO));

    final List<ActivityDTO> result = facade.getActivitiesBetweenTimespan(start, end, GROUP_ID);

    assertEquals(result, singletonList(activityDTO));
  }

  public void findById() {
    when(activityService.findById(eq(ACTIVITY_ID))).thenReturn(activityDTO);

    final ActivityDTO result = facade.findById(ACTIVITY_ID);

    assertEquals(result, activityDTO);
  }

  public void getAssignedActivitiesBetweenTimeSpan() {

    when(
        activityService.findAssignedActivitesBetweenTimespan(eq(START), eq(END), eq(GROUP_ID),
            eq(USER_ID))).thenReturn(singletonList(activityDTO));

    final List<ActivityDTO> result =
        facade.getAssignedActivitesBetweenTimespan(START, END, GROUP_ID, USER_ID);

    assertEquals(result, singletonList(activityDTO));
  }

  public void findByParameters() {
    when(activityService.findByParameters(GROUP_ID, 1, START, END, "false", 3)).thenReturn(
        singletonList(activityDTO));

    final List<ActivityDTO> result = facade.findByParameters(GROUP_ID, 1, START, END, "false", 3);

    assertEquals(result, singletonList(activityDTO));
  }

  public void findPlanedActivities() {
    when(activityService.findPlanedActivites(eq(GROUP_ID))).thenReturn(singletonList(activityDTO));

    final List<ActivityDTO> result = facade.findPlanedActivites(GROUP_ID);

    assertEquals(result, singletonList(activityDTO));
  }
}
