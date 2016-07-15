package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class UserActivityRepositoryImpl extends GenericRepositoryImpl<UserActivity> implements
    UserActivityRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserActivityRepositoryImpl.class);

  @Override
  public UserActivity findByUserIdActivityId(Integer userId, Integer activityId) {
    final Query query =
        em.createNamedQuery(Constants.QUERY_FIND_USER_ACTIVITY).setParameter("userId", userId)
            .setParameter("activityId", activityId);

    LOGGER.info(String.format("find userActivities(s) for user with id %d and activity id %d",
        userId, activityId));

    final List<UserActivity> userActivities = query.getResultList();

    return userActivities.isEmpty() ? null : userActivities.get(0);
  }
}
