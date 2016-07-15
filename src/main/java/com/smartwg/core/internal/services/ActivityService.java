package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;

import java.util.Date;
import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with activity. e.g.
 * persisting and updating activities in the database etc. The main purpose of these methods is to
 * wrap the functionality of the underlying ActivityRepository and convert the entities to dtos for
 * use in higher-tiered layers
 *
 * @author Matthias Höllthaler (mh) , Oezde Simsek (os)
 */
public interface ActivityService {
  /**
   * Creates a new activity entity based on provided information from activityDTO
   *
   * @param activityDTO activityDTO {@link ActivityDTO}
   */
  void createActivity(ActivityDTO activityDTO);

  /**
   * Updates an activity entity based on provided information from activityDTO
   *
   * @param activityDTO activityDTO {@link ActivityDTO}
   */
  void updateActivity(ActivityDTO activityDTO, Integer userId);

  /**
   * Retrieves all activities which are stored in the database
   *
   * @return Activities as ActivityDTO or an empty list
   */
  List<Activity> getAllActivities();

  /**
   * Retrieves all activities which are associated with a certain group
   *
   * @param groupID Group of which the activities should be found
   * @return Matching activites as ActivityDTO or an empty list
   */
  List<ActivityDTO> getActivitiesForGroup(Integer groupID);

  /**
   * Retrieves all ratings (UserActivity) for a certain activity
   *
   * @param activityID Activity of which the ratings should be retrieved
   * @return Matching UserActivities as UserActivityDTO or an empty list
   */
  List<UserActivityDTO> getRatingsForActivity(Integer activityID);

  /**
   * Deletes an activity with a certain id of the database. If the value is null the database will
   * remain unchanged
   *
   * @param activityDTO Activity which should be deleted
   */
  void deleteActivity(ActivityDTO activityDTO);

  /**
   * Retrieves all activites which are due in a certain timespan for a certain group from the
   * database
   *
   * @param start Timespan-Startpoint
   * @param end Timespan-Endpoint
   * @param groupID Group
   * @return Matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> getActivitiesBetweenTimespan(Date start, Date end, Integer groupID);

  /**
   * Searches for a certain activity in the database using its id.
   *
   * @param id Id of the wanted activity
   * @return found Activity as ActivityDTO, null if the passed id doesnt exist
   */
  ActivityDTO findById(Integer id);

  /**
   * Retrieves all activites which are due in a certain timespan for a certain group from the
   * database. In addition the activites must be assigned to a certain user
   *
   * @param start Timespan-Startpoint
   * @param end Timespan-Endpoint
   * @param groupID Group
   * @param userID User to which the activities must be assigned
   * @return Matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> findAssignedActivitesBetweenTimespan(Date start, Date end, Integer groupID,
      Integer userID);

  /**
   * Returns a List of all Activities with the given parameters; the id of the group, the id of the
   * user who is assigned to the activity, timespan with start and end date, value for done
   * activities, the id of a certain activity.
   *
   * @param group_id id of the group
   * @param assignee_id id of the user who is assigned to the activity
   * @param start start point
   * @param end end point
   * @param isDone value for done activities
   * @param activity_id the id of a certain activity
   * @return a list with matching activities or an empty in case of no matching activity were found
   */
  List<ActivityDTO> findByParameters(Integer group_id, Integer assignee_id, Date start, Date end,
      String isDone, Integer activity_id);

  /**
   * Retrieves all activities for a certain group which are planed (due every month and therefore
   * recurring) from the database
   *
   * @param groupId Group of which the activities should be retrieved
   * @return Matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> findPlanedActivites(Integer groupId);

  /**
   * Set the activity with given Id as done by given user
   *
   * @param activityId {@link Integer} of the activity to be marked as done
   * @param userId     {@link Integer} of the user, who has finished the activity
   */
  void setAsDone(Integer activityId, Integer userId);
}
