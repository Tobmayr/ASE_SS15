package com.smartwg.core.internal.repositories;

import java.util.Date;
import java.util.List;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;

/**
 * This repository provides methods for CRUD-Operations for Activity-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * Activit-objects the methods provided by this interface directly return ActivityDTOs to avoid
 * later typecasting or converting.
 * 
 * @author Matthias Höllthaler (mh), Tobias Ortmayr (to), Oezde Simsek (os)
 */
public interface ActivityRepository extends GenericRepository<Activity> {

  /**
   * Retrieves all activities which are associated with a certain group
   * 
   * @param groupID Group of which the activities should be found
   * @return List of matching activites as ActivityDTO or an empty list
   */
  List<ActivityDTO> getActivitiesForGroup(Integer groupID);

  /**
   * Retrieves all ratings (UserActivityDTOs) for a certain activity
   * 
   * @param activityID Activity of which the ratings should be retrieved
   * @return List of matching UserActivities as UserActivityDTO or an empty list
   */
  List<UserActivityDTO> getRatingsForActivity(Integer activityID);

  /**
   * Retrieves all activites which are due in a certain timespan for a certain group from the
   * database
   * 
   * @param start Timespan-Startpoint
   * @param end Timespan-Endpoint
   * @param groupID Group
   * @return List of matching activities as Activity or an empty list
   */
  List<Activity> getActivitiesBetweenTimespan(Date start, Date end, Integer groupID);

  /**
   * Retrieves all activites which are due in a certain timespan for a certain group from the
   * database. In addition the activites must be assigned to a certain user
   * 
   * @param start Timespan-Startpoint
   * @param end Timespan-Endpoint
   * @param groupID Group
   * @param userID User to which the activities must be assigned
   * @return List of matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> getAssignedActivitesBetweenTimespan(Date start, Date end, Integer groupID,
      Integer userID);

  /**
   * Searches for a certain activity in the database using its id.
   * 
   * @param id Id of the wanted activity
   * @return found Activity as ActivityDTO, null if the passed id doesnt exist
   */
  ActivityDTO findActivityById(Integer id);

  /**
   * Retrieves all activities which are stored in the database
   * 
   * @return List of activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> findAllDTOs();

  /**
   * Retrieves a List of all Activities with the given parameters; the id of the group, the id of
   * the user who is assigned to the activity, timespan with start and end date, value for done
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
   * @return List of matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> findPlanedActivites(Integer groupId);
}
