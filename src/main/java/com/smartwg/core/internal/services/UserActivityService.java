package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.entities.UserActivity;

/**
 * @author Anna Sadriu (as)
 */
public interface UserActivityService {

  /**
   * Returns the user activity for given user and activity id
   *
   * @param username {@link String} of the user
   * @param activityId {@link Integer} of the activity
   * @return {@link UserActivity} or <code>null</code> if the activity could not by found
   */
  UserActivity findUserActivityByUsernameActivityId(String username, Integer activityId);
}
