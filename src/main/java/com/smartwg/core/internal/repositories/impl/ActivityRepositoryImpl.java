package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.ActivityRepository;
import com.smartwg.core.util.Constants;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * @author Kamil Sierzant (ks) , Oezde Simsek (os)
 */
@Named
public class ActivityRepositoryImpl extends GenericRepositoryImpl<Activity> implements
    ActivityRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(ActivityRepositoryImpl.class);

  @Override
  @SuppressWarnings("unchecked")
  public List<ActivityDTO> getActivitiesForGroup(Integer groupID) {
    final Query query =
        em.createNamedQuery(Constants.QUERY_ACTIVITY_BY_GROUP_ID).setParameter("groupId", groupID);
    LOGGER.info("find activities for group with id %d", groupID);
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<UserActivityDTO> getRatingsForActivity(Integer activityID) {
    final Query query =
        em.createNamedQuery(Constants.QUERY_RATINGS_BY_ACTIVITY_ID).setParameter("activityId",
            activityID);
    LOGGER.info("find ratings for activity with id %d", activityID);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Activity> getActivitiesBetweenTimespan(Date start, Date end, Integer groupID) {
    final Query query = em.createNamedQuery(Constants.QUERY_ACTIVITIES_BY_TIMESPAN);
    query.setParameter("start", start);
    query.setParameter("end", end);
    query.setParameter("group", groupID);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public ActivityDTO findActivityById(Integer id) {
    Query query = em.createNamedQuery(Constants.QUERY_FIND_ACTIVITY_BY_ID);
    List<ActivityDTO> temp = query.setParameter("id", id).getResultList();
    if (!temp.isEmpty()) {
      return temp.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ActivityDTO> getAssignedActivitesBetweenTimespan(Date start, Date end,
      Integer groupID, Integer userID) {
    Query query = em.createNamedQuery(Constants.QUERY_FIND_ASSIGNED_ACTIVITES_TIMESPAN);
    query.setMaxResults(10);
    return query.setParameter("start", start).setParameter("end", end)
        .setParameter("group", groupID).setParameter("user", userID).getResultList();

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ActivityDTO> findByParameters(Integer groupId, Integer assigneeId, Date start,
      Date end, String isDone, Integer activityId) {
    List<ActivityDTO> list = new ArrayList<>();
    Query query;
    if (groupId == null) {
      return list;
    }
    String queryString = "SELECT a FROM Activity a where a.group.id=" + groupId;
    if (activityId != -1) {
      queryString += " AND a.id=" + activityId;
    }
    if (assigneeId != -1) {
      queryString += " AND a.assignedTo.id=" + assigneeId;
    }
    if (isDone.equals("true")) {
      queryString += " AND a.isDone=" + true;
    } else if (isDone.equals("false")) {
      queryString += " AND a.isDone=" + false;
    }
    if (start != null && end != null) {
      queryString += " AND ( a.date BETWEEN :start AND :end) ORDER BY a.date DESC";
      query = em.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
      query.setParameter("end", end, TemporalType.DATE);
    } else if (start != null) {
      queryString += " AND ( a.date >= :start) ORDER BY a.date DESC";
      query = em.createQuery(queryString);
      query.setParameter("start", start, TemporalType.DATE);
    } else if (end != null) {
      queryString += " AND ( a.date <= :end) ORDER BY a.date DESC";
      query = em.createQuery(queryString);
      query.setParameter("end", end, TemporalType.DATE);
    } else {
      queryString += " ORDER BY a.date DESC";
      query = em.createQuery(queryString);
    }

    for (Activity a : (List<Activity>) query.getResultList()) {
      ActivityDTO act = new ActivityDTO(a);

      if (a.getRatings().size() != 0) {
        int i = 0;
        for (UserActivity u : a.getRatings()) {
          i += u.getRating();
        }

        act.setRating(i / a.getRatings().size());
      }
      list.add(act);
    }
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ActivityDTO> findAllDTOs() {
    final Query query = em.createNamedQuery(Constants.QUERY_ACTIVITIES);

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ActivityDTO> findPlanedActivites(Integer groupId) {
    Query query = em.createNamedQuery(Constants.QUERY_ACTIVITES_PLANED);
    DateTime dEnd = new DateTime();
    Date start = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
    Date end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
    query.setParameter("group", groupId).setParameter("start", start).setParameter("end", end);
    return query.getResultList();
  }
}
