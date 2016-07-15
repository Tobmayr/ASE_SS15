package com.smartwg.core.internal.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Arrays;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.internal.services.UserActivityService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserActivityServiceImplTest {

  @InjectMocks
  private UserActivityService service;

  @Mock
  private UserActivityRepository userActivityRepository;

  @Mock
  private UserActivity userActivity;
  @Mock
  private UserActivity secondUserActivity;
  @Mock
  private User user;
  @Mock
  private User secondUser;
  @Mock
  private Activity activity;
  @Mock
  private Activity secondActivity;

  @BeforeMethod
  public void setUp() {
    service = new UserActivityServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findUserActivityByUsernameActivityIdWhenUserHasNoActivity() {
    when(userActivityRepository.findAll()).thenReturn(
        Arrays.asList(userActivity, secondUserActivity));
    when(userActivity.getUser()).thenReturn(user);
    when(userActivity.getActivity()).thenReturn(activity);
    when(secondUserActivity.getUser()).thenReturn(secondUser);
    when(secondUserActivity.getActivity()).thenReturn(secondActivity);

    final UserActivity result = service.findUserActivityByUsernameActivityId("Kamil", 2);

    assertNull(result);
  }

  public void findUserActivityByUsernameActivityIdSucceed() {
    when(userActivityRepository.findAll()).thenReturn(
        Arrays.asList(userActivity, secondUserActivity));
    when(userActivity.getUser()).thenReturn(user);
    when(userActivity.getActivity()).thenReturn(activity);
    when(secondUserActivity.getUser()).thenReturn(secondUser);
    when(secondUserActivity.getActivity()).thenReturn(secondActivity);
    when(secondUser.getUserName()).thenReturn("Kamil");
    when(secondActivity.getId()).thenReturn(2);

    final UserActivity result = service.findUserActivityByUsernameActivityId("Kamil", 2);

    assertEquals(result, secondUserActivity);
  }
}
