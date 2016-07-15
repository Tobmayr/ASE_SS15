package com.smartwg.core.facades;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;

@Transactional
/**
 * Central access point for service logic which is associated with activities. In case of implementing
 * a REST-API in the future its methods will be bounded on the facade-interfaces
 * 
 * @author ASE GROUP 04
 *
 */
public interface ActivityFacade {
  /**
   * Persists a new activity into the database
   * 
   * @param AbsenceDTO Absence object which should be stored
   * 
   */
  void createActivity(ActivityDTO activityDTO);

  /**
   * Updates an existing activity in the database
   * 
   * @param AbsenceDTO Absence object which should be updated
   * 
   */
  void updateActivity(ActivityDTO activityDTO, Integer userId);

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
   * Retrieves all activities which are stored in the database
   * 
   * @return Activities as ActivityDTO or an empty list
   */
  List<Activity> getAllActivities();

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
   * Retrieves all activites which are due in a certain timespan for a certain group from the
   * database. In addition the activites must be assigned to a certain user
   * 
   * @param start Timespan-Startpoint
   * @param end Timespan-Endpoint
   * @param groupID Group
   * @param userID User to which the activities must be assigned
   * @return Matching activities as ActivityDTO or an empty list
   */
  List<ActivityDTO> getAssignedActivitesBetweenTimespan(Date start, Date end, Integer groupID,
      Integer userID);

  /**
   * Searches for a certain activity in the database using its id.
   * 
   * @param id Id of the wanted activity
   * @return found Activity as ActivityDTO, null if the passed id doesnt exist
   */
  ActivityDTO findById(Integer id);

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

}
