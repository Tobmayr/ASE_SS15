package com.smartwg.core.controllers.workDistribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.facades.UserGroupFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.util.Constants;

@Named("workDistribBean")
@Scope("view")
public class WorkDistributionBean implements Serializable {
  public class Points {
    private double gainPoints;
    private double alreadyDone;
    private double shouldPoints;


    public Points(double gainPoints, double alreadyDone, double shouldPoints) {
      this.gainPoints = gainPoints;
      this.alreadyDone = alreadyDone;
      this.shouldPoints = shouldPoints;

    }

    public void setGainPoints(double gainPoints) {
      this.gainPoints = gainPoints;
    }

    public double getGainPoints() {
      return formatDouble(this.gainPoints);
    }

    public void setAlreadyDone(double alreadyDone) {
      this.alreadyDone = alreadyDone;
    }

    public double getAlreadyDone() {
      return formatDouble(this.alreadyDone);
    }

    public void setShouldPoints(double shouldPoints) {
      this.shouldPoints = shouldPoints;
    }

    public double getShouldPoints() {
      return formatDouble(this.shouldPoints);
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkDistributionBean.class);

  private ResourceBundle ms = SmartWG.getMessageBundle();



  private BarChartModel model;
  private List<UserDTO> users;
  private List<ActivityDTO> activities;
  private double totalPoints;
  private HashMap<UserDTO, Points> userPoints;
  private HashMap<ActivityDTO, List<UserActivityDTO>> userActivities;
  private HashMap<Integer, Integer> absences;
  private List<String> columns;
  private double pointsShouldBeReached;
  private int monthLength;
  private int allPersistDays;

  @Inject
  private NavigationBean navigationBean;
  @Inject
  private UserBean userBean;
  @Inject
  private UserGroupFacade ugFacade;
  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private SmartWG smartwg;

  @PostConstruct
  public void init() {

    totalPoints = 0;
    pointsShouldBeReached = 0;
    columns = new ArrayList<String>();
    userPoints = new HashMap<>();
    absences = new HashMap<>();
    userActivities = new HashMap<>();
    DateTime dt = new DateTime(smartwg.getWorkDistribOverviewSelectedDate());
    Date beginOfCurrentMonth =
        new DateTime(DateTime.now().getYear(), dt.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
    Date endOfCurrentMonth =
        new DateTime(DateTime.now().getYear(), dt.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();

    // Get the number of days in that month
    monthLength = dt.dayOfMonth().getMaximumValue();

    // Load all users of the current group, their activities of the current month and the absence
    // times
    users = new ArrayList<UserDTO>(userBean.getUsersOfCurrentGroup());
    HashMap<Integer, UserDTO> temp = new HashMap<Integer, UserDTO>();

    for (UserDTO u : users) {
      UserGroup ug =
          ugFacade.findUserGroupByUsernameGroupId(u.getUserName(), userBean.getCurrentGroup()
              .getId());
      if (ug.getJoinDate().after(endOfCurrentMonth)
          || (ug.getLeaveDate() != null && ug.getLeaveDate().before(beginOfCurrentMonth))) {
        temp.put(u.getId(), u);
      }
    }
    users.removeAll(temp.values());

    activities =
        activityFacade.getActivitiesBetweenTimespan(beginOfCurrentMonth, endOfCurrentMonth,
            userBean.getCurrentUserGroup().getGroupId());

    for (UserDTO user : users)
      columns.add(user.getUserName());

    for (AbsenceDTO absence : absenceFacade.getGroupAbsences(userBean.getCurrentUserGroup()
        .getGroupId())) {
      if (temp.containsKey(absence.getUserGroup().getUserId())) {
        return;
      }
      DateTime awayFrom = new DateTime(absence.getAwayFrom());
      DateTime awayTill = new DateTime(absence.getAwayTill());
      if (!absence.getAwayFrom().after(endOfCurrentMonth)
          && !absence.getAwayTill().before(beginOfCurrentMonth)) {
        if (absence.getAwayFrom().before(beginOfCurrentMonth)
            && absence.getAwayTill().before(endOfCurrentMonth))
          absences.put(absence.getUserGroup().getUserId(), awayTill.getDayOfMonth());
        else if (absence.getAwayFrom().before(beginOfCurrentMonth)
            && absence.getAwayTill().after(endOfCurrentMonth))
          absences.put(absence.getUserGroup().getUserId(), monthLength);
        else if (absence.getAwayFrom().after(beginOfCurrentMonth)
            && absence.getAwayTill().before(endOfCurrentMonth))
          absences.put(absence.getUserGroup().getUserId(),
              awayTill.getDayOfMonth() - awayFrom.getDayOfMonth() + 1);
        else if (absence.getAwayFrom().after(beginOfCurrentMonth)
            && absence.getAwayTill().after(endOfCurrentMonth))
          absences.put(absence.getUserGroup().getUserId(), monthLength - awayFrom.getDayOfMonth()
              + 1);
      }
    }

    calculateAllPersistDays();
    calculateTotalPoints();
    pointsShouldBeReached = calculatePointsShouldBeReached(0);
    calculateUserPoints();
    createBarModels();
    setUserActivities();

    if (new DateTime(beginOfCurrentMonth).monthOfYear().equals(DateTime.now().monthOfYear())) {
      HashMap<String, Double> currentBalance = new HashMap<String, Double>();
      for (UserDTO u : userPoints.keySet()) {
        for (UserGroupDTO userGroup : userBean.getCurrentGroup().getUserGroups())
          if (userGroup.getUserId() == u.getId())
            currentBalance.put(u.getUserName(), userPoints.get(u).gainPoints
                - userPoints.get(u).shouldPoints + userGroup.getBalance());
      }
      smartwg.setCurrentBalance(currentBalance);
    }
  }

  public void reset() {
    model = null;
    users = null;
    activities = null;
    totalPoints = 0;
    userPoints = null;
    userActivities = null;
    absences = null;
    columns = null;
    pointsShouldBeReached = 0;
    monthLength = 0;
    allPersistDays = 0;
    init();
  }

  public double calculateMedian(Integer activityID) {
    List<UserActivityDTO> userActivities = activityFacade.getRatingsForActivity(activityID);
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

  public String ratingsButtonClicked() {
    return navigationBean.getPageRatings() + Constants.PAGE_REDIRECT;
  }

  public BarChartModel getModel() {
    return model;
  }

  public HashMap<UserDTO, Points> getUserPoints() {
    return this.userPoints;
  }

  public HashMap<ActivityDTO, List<UserActivityDTO>> getUserActivities() {
    return this.userActivities;
  }

  public double getTotalPoints() {
    return totalPoints;
  }

  public double getPointsShouldBeReached() {
    return formatDouble(pointsShouldBeReached);
  }

  public List<String> getColumns() {
    return columns;
  }

  public double formatDouble(double number) {
    number = number * 100;
    number = Math.round(number);
    number = number / 100;
    return number;
  }

  public void decrementMonth() {
    DateTime dt = new DateTime(smartwg.getWorkDistribOverviewSelectedDate());
    smartwg.setWorkDistribOverviewSelectedDate(dt.minusMonths(1).toDate());
    reset();
  }

  public void incrementMonth() {
    DateTime dt = new DateTime(smartwg.getWorkDistribOverviewSelectedDate());
    smartwg.setWorkDistribOverviewSelectedDate(dt.plusMonths(1).toDate());
    reset();
  }

  private BarChartModel initBarModel() {
    BarChartModel model = new BarChartModel();

    ChartSeries donePoints = new ChartSeries();
    donePoints.setLabel(ms.getString("donePoints"));

    ChartSeries allPoints = new ChartSeries();
    allPoints.setLabel(ms.getString("allPoints"));

    for (Entry<UserDTO, Points> up : userPoints.entrySet()) {
      donePoints.set(up.getKey().getUserName(), up.getValue().getAlreadyDone());
      allPoints.set(up.getKey().getUserName(), up.getValue().getGainPoints());
    }

    model.addSeries(donePoints);
    model.addSeries(allPoints);
    model.setAnimate(true);
    model.setLegendRows(0);
    model.setExtender("ext");

    return model;
  }

  private void createBarModels() {
    createBarModel();
  }

  private void createBarModel() {
    model = initBarModel();

    model.setTitle(ms.getString("workDistribOverview"));
    model.setLegendPosition("ne");

    Axis xAxis = model.getAxis(AxisType.X);
    xAxis.setLabel(ms.getString("lhUsername"));

    Axis yAxis = model.getAxis(AxisType.Y);
    yAxis.setLabel(ms.getString("points"));
    yAxis.setMin(0);
    yAxis.setMax(totalPoints);
  }

  private void calculateAllPersistDays() {
    for (Entry<Integer, Integer> absence : absences.entrySet()) {
      LOGGER.info("allPersistDays: " + allPersistDays + " = (" + monthLength + " + "
          + absence.getValue() + ")");
      allPersistDays += (monthLength - absence.getValue());
    }
    allPersistDays += (users.size() - absences.size()) * monthLength;
  }

  private void calculateTotalPoints() {
    for (ActivityDTO activity : activities) {
      totalPoints += calculateMedian(activity.getId());
    }
  }

  private double calculatePointsShouldBeReached(int absenceDays) {
    LOGGER.info("totalPoints: " + totalPoints + ", monthLength: " + monthLength + ", absenceDays: "
        + absenceDays + ", allPersistDays: " + allPersistDays);
    return formatDouble(totalPoints * (monthLength - absenceDays) / allPersistDays);
  }

  private void setUserActivities() {
    for (ActivityDTO activity : activities) {
      List<UserActivityDTO> userActivites = activityFacade.getRatingsForActivity(activity.getId());
      HashMap<Integer, UserActivityDTO> userActivitiesForUser = new HashMap<>();

      for (UserActivityDTO ua : userActivites)
        userActivitiesForUser.put(ua.getUserId(), ua);

      for (UserDTO user : users)
        if (!userActivitiesForUser.containsKey(user.getId()))
          userActivitiesForUser.put(user.getId(), null);

      userActivites = new ArrayList<UserActivityDTO>();
      for (Entry<Integer, UserActivityDTO> entry : userActivitiesForUser.entrySet()) {
        LOGGER.info("user[" + entry.getKey() + "]: " + entry.getValue());
        userActivites.add(entry.getValue());
      }

      activity.setMedian(calculateMedian(activity.getId()));
      this.userActivities.put(activity, userActivites);
    }
  }

  private void calculateUserPoints() {
    for (UserDTO user : users) {
      double alreadyDone = 0;
      double gain = 0;
      for (ActivityDTO activity : activities) {
        if (user.getId().equals(activity.getAssignedToUserId())) {
          if (activity.getIsDone())
            alreadyDone += calculateMedian(activity.getId());
          gain += calculateMedian(activity.getId());
        }
      }

      for (UserGroupDTO userGroup : userBean.getCurrentGroup().getUserGroups())
        if (userGroup.getUserId() == user.getId())
          userPoints.put(user, new Points(gain, alreadyDone, 0));

      for (Entry<Integer, Integer> absence : absences.entrySet())
        if (user.getId() == absence.getKey())
          userPoints.get(user).setShouldPoints(calculatePointsShouldBeReached(absence.getValue()));

      if (userPoints.get(user).getShouldPoints() == 0)
        userPoints.get(user).setShouldPoints(pointsShouldBeReached);

      LOGGER.info("User: " + user.getUserName() + ", gain: " + gain + ", alreadyDone: "
          + alreadyDone);
    }
  }
}
