package com.smartwg.core.controllers.activities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;

/**
 * @author Matthias Höllthaler (mh), Tobias Ortmayr(to), Anna Sadriu (as)
 */

@Named("activityList")
@Scope("view")
public class ActivityListBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(ActivityListBean.class);

  @Inject
  private ActivityFacade facade;
  @Inject
  private UserBean userBean;

  private List<ActivityDTO> activities;
  private ActivitySelect activitySelect = ActivitySelect.CURRENT_MONTH;
  private double overviewTotal;
  private List<ActivityDTO> filteredActivities;
  private ActivityDTO selectedActivity;
  private List<UserDTO> users;
  private Date start = new Date();
  private Date end = new Date();

  @PostConstruct
  public void init() {
    users = userBean.getUsersOfCurrentGroup();
    activitySelectChanged(null);
  }

  public ActivitySelect[] getActivitySelectTypes() {
    return ActivitySelect.values();
  }

  public void activitySelectChanged(AjaxBehaviorEvent event) {

    DateTime dEnd = DateTime.now();
    DateTime dStart;
    Date start = new Date();
    Date end = new Date();

    switch (activitySelect) {
      case CURRENT_MONTH:

        start = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case LAST_MONTH:
        dStart = dEnd.minusMonths(1);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dStart.getYear(), dStart.getMonthOfYear() + 1, 1, 0, 0, 0).toDate();
        break;
      case LAST_HALFYEAR:
        dStart = dEnd.minusMonths(6);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case LAST_YEAR:
        dStart = dEnd.minusMonths(12);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case CUSTOM:
        start = this.start;
        end = this.end;
        break;
    }

    activities =
        facade
            .getActivitiesBetweenTimespan(start, end, userBean.getCurrentUserGroup().getGroupId());
    setMedians(activities);
    calculateTotal(userBean.getCurrentUser().getUserName());
  }

  private void calculateTotal(String username) {
    overviewTotal = 0;
    for (ActivityDTO a : activities) {
      for (UserActivityDTO ua : a.getRatings())
        if (ua.getUser().getUserName().equals(username))
          overviewTotal = overviewTotal + ua.getRating(); // calculateMedian(ua.getActivityId());
    }
  }

  private double calculateMedian(Integer activityID) {
    List<UserActivityDTO> userActivities = facade.getRatingsForActivity(activityID);
    int ratings[] = new int[userActivities.size()];
    int i = 0;
    for (UserActivityDTO ua : userActivities) {
      ratings[i] = ua.getRating();
      i++;
    }

    Arrays.sort(ratings);
    LOGGER.info("Sorted Ratings: ");
    for (int x : ratings) {
      LOGGER.info(x + " ");
    }
    LOGGER.info("");

    // Calculate median (middle number)
    double median = 0;
    double pos1 = Math.floor((ratings.length - 1.0) / 2.0);
    double pos2 = Math.ceil((ratings.length - 1.0) / 2.0);
    if (pos1 == pos2) {
      median = ratings[(int) pos1];
    } else {
      median = (ratings[(int) pos1] + ratings[(int) pos2]) / 2.0;
    }

    LOGGER.info("Median: " + median);

    return median;
  }

  private void setMedians(List<ActivityDTO> activities) {
    for (ActivityDTO activity : activities) {
      activity.setMedian(calculateMedian(activity.getId()));
    }
  }

  public List<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(List<ActivityDTO> activities) {
    this.activities = activities;
  }

  public ActivitySelect getActivitySelect() {
    return activitySelect;
  }

  public void setActivitySelect(ActivitySelect activitySelect) {
    this.activitySelect = activitySelect;
  }

  public double getOverviewTotal() {
    return overviewTotal;
  }

  public void setOverviewTotal(double overviewTotal) {
    this.overviewTotal = overviewTotal;
  }

  public List<ActivityDTO> getFilteredActivities() {
    return filteredActivities;
  }

  public void setFilteredActivities(List<ActivityDTO> filteredActivities) {
    this.filteredActivities = filteredActivities;
  }

  public ActivityDTO getSelectedActivity() {
    return selectedActivity;
  }

  public void setSelectedActivity(ActivityDTO selectedActivity) {
    this.selectedActivity = selectedActivity;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public enum ActivitySelect {
    CURRENT_MONTH("sCurrentMonth"), LAST_MONTH("sLastMonth"), LAST_HALFYEAR("sLast6Months"), LAST_YEAR(
        "sLastYear"), CUSTOM("cCustom");

    private String ms_id;
    
    private ResourceBundle ms = SmartWG.getMessageBundle();

    ActivitySelect(String ms_id) {
      this.ms_id = ms_id;
    }

    public String getName() {
      return ms.getString(ms_id);
    }
  }
}
