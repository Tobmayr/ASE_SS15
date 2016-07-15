package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.internal.services.UserActivityService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class UserActivityServiceImpl implements UserActivityService {

  @Inject
  private UserActivityRepository userActivityRepository;

  @Override
  public UserActivity findUserActivityByUsernameActivityId(final String username,
      final Integer activityId) {
    final List<UserActivity> list = userActivityRepository.findAll();
    for (UserActivity userActivity : list) {
      if (activityId.equals(userActivity.getActivity().getId())
          && username.equals(userActivity.getUser().getUserName())) {
        return userActivity;
      }
    }
    return null;
  }
}
