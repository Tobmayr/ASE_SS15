package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.ActivityRepository;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.ActivityService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserService;

import org.joda.time.DateTime;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * @author Kamil Sierzant (ks)
 */
@Test
public class ActivityServiceImplTest {

  private static final Integer GROUP_ID = 2;
  private static final Integer USER_ID = 3;
  private static final String USER_NAME = "kamil";
  private static final Date NOW = DateTime.now().toDate();
  private static final Date TOMORROW = DateTime.now().plusDays(1).toDate();
  private static final Integer ACTIVITY_ID = 5;

  @InjectMocks
  private ActivityService service;

  @Mock
  private ActivityRepository repository;
  @Mock
  private UserActivityRepository userActivityRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private EntityConverter factory;
  @Mock
  private UserService userService;
  @Mock
  private EmailService emailService;

  @Mock
  private ActivityDTO activityDTO;
  @Mock
  private Activity activity;
  @Mock
  private Activity secondActivity;
  @Mock
  private UserActivity userActivity;
  @Mock
  private User user;
  @Mock
  private Group group;
  @Mock
  private UserActivityDTO userActivityDTO;


  @BeforeMethod
  public void setUp() {
    service = new ActivityServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createActivitySucceed() {
    final Set<String> recipients = new HashSet<String>() {
      {
        add("sierzant.kamil@gmail.com");
        add("test@email.com");
      }
    };
    final ArgumentCaptor<UserActivity> activityCaptor = ArgumentCaptor.forClass(UserActivity.class);

    when(factory.createActivity(eq(activityDTO))).thenReturn(activity);
    when(repository.save(eq(activity))).thenReturn(activity);
    when(activity.getCreatedBy()).thenReturn(user);
    when(activity.getAssignedTo()).thenReturn(user);
    when(user.getUserName()).thenReturn(USER_NAME);
    when(user.getId()).thenReturn(USER_ID);
    when(userRepository.findById(USER_ID)).thenReturn(user);
    when(activityDTO.getGroupId()).thenReturn(GROUP_ID);
    when(activityDTO.getId()).thenReturn(null);
    when(activityDTO.getRatingFromUser(eq(USER_NAME))).thenReturn(5);
    when(userService.findUsersEmailsByGroupId(eq(GROUP_ID), eq(NotificationType.ACTIVITY)))
        .thenReturn(asList("sierzant.kamil@gmail.com", "test@email.com"));
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);

    service.createActivity(activityDTO);

    verify(emailService, times(1)).createAndSendActivityCreatedEmail(eq(activityDTO),
        eq(recipients));
    verify(userActivityRepository, times(1)).save(activityCaptor.capture());
    assertEquals(activityCaptor.getValue().getUser(), user);
    assertEquals(activityCaptor.getValue().getActivity(), activity);
    assertEquals(activityCaptor.getValue().getRating(), Integer.valueOf(5));
  }

  public void updateActivity() {
    final ArgumentCaptor<UserActivity> activityCaptor = ArgumentCaptor.forClass(UserActivity.class);

    when(factory.createActivity(eq(activityDTO))).thenReturn(activity);
    when(activity.getId()).thenReturn(ACTIVITY_ID);
    when(activity.getCreatedBy()).thenReturn(user);
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(user.getId()).thenReturn(USER_ID);
    when(user.getUserName()).thenReturn(USER_NAME);
    when(repository.findById(eq(ACTIVITY_ID))).thenReturn(activity);
    when(activityDTO.getRatingFromUser(eq(USER_NAME))).thenReturn(5);

    service.updateActivity(activityDTO, USER_ID);

    verify(repository, times(1)).merge(eq(activity));
    verify(userActivityRepository, times(1)).merge(activityCaptor.capture());
    assertEquals(activityCaptor.getValue().getUser(), user);
    assertEquals(activityCaptor.getValue().getActivity(), activity);
    assertEquals(activityCaptor.getValue().getRating(), Integer.valueOf(5));
  }

  public void deleteActivityWithoutRatings() {
    final ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);

    when(activityDTO.getId()).thenReturn(ACTIVITY_ID);

    service.deleteActivity(activityDTO);

    verify(repository, times(1)).delete(activityCaptor.capture());
    assertEquals(activityCaptor.getValue().getId(), ACTIVITY_ID);
  }

  public void deleteActivityWithRatings() {
    final ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);

    when(activityDTO.getId()).thenReturn(ACTIVITY_ID);
    when(activityDTO.getRatings()).thenReturn(singletonList(userActivityDTO));
    when(userActivityDTO.getActivityId()).thenReturn(ACTIVITY_ID);
    when(userActivityDTO.getUserId()).thenReturn(USER_ID);
    when(userActivityRepository.findByUserIdActivityId(eq(USER_ID), eq(ACTIVITY_ID))).thenReturn(
        userActivity);

    service.deleteActivity(activityDTO);

    verify(userActivityRepository, times(1)).delete(eq(userActivity));
    verify(repository, times(1)).delete(activityCaptor.capture());
    assertEquals(activityCaptor.getValue().getId(), ACTIVITY_ID);
  }

  public void getAllActivities() {
    when(repository.findAll()).thenReturn(singletonList(activity));

    final List<Activity> result = service.getAllActivities();

    assertEquals(result, singletonList(activity));
  }

  public void getActivitiesForGroup() {
    when(repository.getActivitiesForGroup(eq(GROUP_ID))).thenReturn(singletonList(activityDTO));

    final List<ActivityDTO> result = service.getActivitiesForGroup(GROUP_ID);

    assertEquals(result, singletonList(activityDTO));
  }

  public void getRatingsForActivity() {
    when(repository.getRatingsForActivity(eq(ACTIVITY_ID))).thenReturn(
        singletonList(userActivityDTO));

    final List<UserActivityDTO> result = service.getRatingsForActivity(ACTIVITY_ID);

    assertEquals(result, singletonList(userActivityDTO));
  }

  public void getActivitiesBetweenTimeSpan() {
    when(repository.getActivitiesBetweenTimespan(eq(NOW), eq(TOMORROW), eq(GROUP_ID))).thenReturn(
        asList(activity, secondActivity));
    when(activity.getGroup()).thenReturn(group);
    when(activity.getCreatedBy()).thenReturn(user);
    when(activity.getAssignedTo()).thenReturn(user);
    when(secondActivity.getGroup()).thenReturn(group);
    when(secondActivity.getCreatedBy()).thenReturn(user);
    when(secondActivity.getAssignedTo()).thenReturn(user);

    final List<ActivityDTO> result = service.getActivitiesBetweenTimespan(NOW, TOMORROW, GROUP_ID);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(result.size(), 2);
  }

  public void findById() {
    when(repository.findActivityById(eq(ACTIVITY_ID))).thenReturn(activityDTO);

    final ActivityDTO result = service.findById(ACTIVITY_ID);

    assertEquals(result, activityDTO);
  }

  public void findAssignedActivitiesBetweenTimeSpan() {
    service.findAssignedActivitesBetweenTimespan(NOW, TOMORROW, GROUP_ID, USER_ID);

    verify(repository, times(1)).getAssignedActivitesBetweenTimespan(eq(NOW), eq(TOMORROW),
        eq(GROUP_ID), eq(USER_ID));
  }

  public void findByParameters() {
    service.findByParameters(GROUP_ID, 2, NOW, TOMORROW, "false", 3);

    verify(repository, times(1)).findByParameters(eq(GROUP_ID), eq(2), eq(NOW), eq(TOMORROW),
                                                  eq("false"), eq(3));
  }
  
  public void findPlanedActivites() {
    service.findPlanedActivites(GROUP_ID);

    verify(repository, times(1)).findPlanedActivites(eq(GROUP_ID));
  }

  public void setAsDoneSucceed() {
    when(repository.findActivityById(eq(ACTIVITY_ID))).thenReturn(activityDTO);
    when(factory.createActivity(eq(activityDTO))).thenReturn(activity);
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(activity.getId()).thenReturn(ACTIVITY_ID);
    when(repository.findById(eq(ACTIVITY_ID))).thenReturn(activity);

    service.setAsDone(ACTIVITY_ID, USER_ID);

    verify(activityDTO, times(1)).setIsDone(eq(Boolean.TRUE));
    verify(activityDTO, times(1)).setAssignedToUserId(eq(USER_ID));
    verify(repository, times(1)).merge(activity);
    verify(userActivityRepository, times(1)).merge(any(UserActivity.class));
  }

  public void setAsDoneFailsWhenActivityNotFound() {
    when(repository.findActivityById(eq(ACTIVITY_ID))).thenReturn(null);

    service.setAsDone(ACTIVITY_ID, USER_ID);

    verify(repository, never()).save(any(Activity.class));
  }
}
