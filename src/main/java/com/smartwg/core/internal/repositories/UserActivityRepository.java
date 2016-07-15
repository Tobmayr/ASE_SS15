package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.entities.UserActivity;

/**
 * This repository provides methods for CRUD-Operations for UserActivity-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * UserActivity-objects the methods provided by this interface directly return UserActivityDTOs to avoid later
 * typecasting or converting.
 *
 * @author Anna Sadriu (as)
 */
public interface UserActivityRepository extends GenericRepository<UserActivity> {

  /**
   * Retrieves the rating (UserActivity) for a certain activity from the specified user
   * 
   * @param userId the user
   * @param activityID Activity of which the ratings should be retrieved
   * @return The matching UserActivitie or an NULL
   */
  UserActivity findByUserIdActivityId(Integer userId, Integer activityId);
}
