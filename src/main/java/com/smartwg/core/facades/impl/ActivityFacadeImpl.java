package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.services.ActivityService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Matthias Höllthaler (mh), Tobias Ortmayr (to), Anna Sadriu (as)
 */
@Named
public class ActivityFacadeImpl implements ActivityFacade {

  @Inject
  private ActivityService activityService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#createActivity(com.smartwg.core.internal.domain.dtos.ActivityDTO)
   */

  @Override
  public void createActivity(ActivityDTO activityDTO) {
    activityService.createActivity(activityDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#updateActivity(com.smartwg.core.internal.domain.dtos.ActivityDTO,
   *      java.lang.Integer)
   */

  @Override
  public void updateActivity(ActivityDTO activityDTO, Integer userId) {
    activityService.updateActivity(activityDTO, userId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#deleteActivity(com.smartwg.core.internal.domain.dtos.ActivityDTO)
   */

  @Override
  public void deleteActivity(final ActivityDTO activityDTO) {
    activityService.deleteActivity(activityDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#getAllActivities()
   */

  @Override
  public List<Activity> getAllActivities() {
    return activityService.getAllActivities();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#getActivitiesForGroup(java.lang.Integer)
   */

  @Override
  public List<ActivityDTO> getActivitiesForGroup(final Integer groupId) {
    return activityService.getActivitiesForGroup(groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#getRatingsForActivity(java.lang.Integer)
   */

  @Override
  public List<UserActivityDTO> getRatingsForActivity(final Integer activityId) {
    return activityService.getRatingsForActivity(activityId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#getActivitiesBetweenTimespan(java.util.Date,
   *      java.util.Date, java.lang.Integer)
   */

  @Override
  public List<ActivityDTO> getActivitiesBetweenTimespan(final Date start, final Date end,
      final Integer groupId) {
    return activityService.getActivitiesBetweenTimespan(start, end, groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#findById(java.lang.Integer)
   */

  @Override
  public ActivityDTO findById(final Integer id) {
    return activityService.findById(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#getAssignedActivitesBetweenTimespan(java.util.Date,
   *      java.util.Date, java.lang.Integer, java.lang.Integer)
   */

  @Override
  public List<ActivityDTO> getAssignedActivitesBetweenTimespan(final Date start, final Date end,
      final Integer groupId, final Integer userId) {
    return activityService.findAssignedActivitesBetweenTimespan(start, end, groupId, userId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#findByParameters(java.lang.Integer,
   *      java.lang.Integer, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer)
   */

  @Override
  public List<ActivityDTO> findByParameters(final Integer groupId, final Integer assigneeId,
      final Date start, final Date end, final String isDone, final Integer activityId) {
    return activityService.findByParameters(groupId, assigneeId, start, end, isDone, activityId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ActivityFacade#findPlanedActivites(java.lang.Integer)
   */

  @Override
  public List<ActivityDTO> findPlanedActivites(final Integer groupId) {
    return activityService.findPlanedActivites(groupId);
  }

}
