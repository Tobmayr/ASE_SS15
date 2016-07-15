package com.smartwg.core.controllers;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.absence.AbsenceEditorBean;
import com.smartwg.core.controllers.activities.ActivityActionsBean;
import com.smartwg.core.controllers.activities.ActivityEditorBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;

@Named("calendarBean")
@Scope("view")
public class CalendarBean implements Serializable {

 
  private ResourceBundle ms = SmartWG.getMessageBundle();

  private List<ActivityDTO> activities;
  private List<UserDTO> users;
  private ScheduleModel eventModel;
  private List<ActivityDTO[]> rows;
  private List<AbsenceDTO> absences;
  private List<ActivityDTO> activitiesInDb;


  @Inject
  private UserBean userBean;
  @Inject
  private ActivityEditorBean activityEditor;
  @Inject
  private ActivityActionsBean activityActions;
  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private NavigationBean navigation;
  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private AbsenceEditorBean absenceEditorBean;


  /**
   * default empty constructor
   */
  public CalendarBean() {}

  @PostConstruct
  private void init() {
    int groupId = userBean.getCurrentUserGroup().getGroupId();
    users = userBean.getUsersOfCurrentGroup();
    activitiesInDb = activityFacade.getActivitiesForGroup(groupId);
    if (activitiesInDb != null) {
      activities = new ArrayList<>(activitiesInDb);
    } else {
      activitiesInDb = new ArrayList<>();
      activities = new ArrayList<>();
    }
    absences = absenceFacade.getGroupAbsences(groupId);
    if (absences != null) {
      for (AbsenceDTO absence : absences) {
        // Create new "Activities" at start and end date of absence (for Group Calendar)
        activities.add(new ActivityDTO(null,
            ms.getString("mAbsence") + " " + ms.getString("begin"), absence.getAwayFrom(), 0, null,
            null, absence.getUserGroup().getUserId(), false, 0));
        activities.add(new ActivityDTO(null, ms.getString("mAbsence") + " " + ms.getString("end"),
            absence.getAwayTill(), 0, null, null, absence.getUserGroup().getUserId(), false, 0));
      }
    } else {
      absences = new ArrayList<>();
    }
    createGroupCalendarRows();
    createCalendarEvents();
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public List<ActivityDTO[]> getRows() {
    return rows;
  }

  public List<ActivityDTO> getActivities() {
    return activities;
  }

  public ScheduleModel getEventModel() {
    return eventModel;
  }

  public void deleteButtonClicked() {
    // ToDo: Confirmation
    activityActions.deleteActivity(activityEditor.getActivity(), navigation.getPageCalendar());
    init();
  }

  public void saveButtonClicked() {
    activityEditor.createActivity(false);
    init();
  }

  public void onActivitySelect(SelectEvent selectEvent) {
    ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
    absenceEditorBean.setCurrentAbsence(new AbsenceDTO());
    activityEditor.setActivity(new ActivityDTO());
    if (event.getDescription().contains(ms.getString("mAbsence"))) {
      // Absence event selected
      for (AbsenceDTO absence : absences) {
        if ((ms.getString("mAbsence") + absence.getId()).equals(event.getDescription())) {
          absenceEditorBean.setCurrentAbsence(absence);
          // ToDo: Open Absence instead of Dialog to create new Activity...
          break;
        }
      }
    } else {
      // Activity event seleced
      for (ActivityDTO activity : activities) {
        if ((ms.getString("activity") + activity.getId()).equals(event.getDescription())) {
          activityEditor.setActivity(activity);
          break;
        }
      }
    }
  }

  public void onDateSelect(SelectEvent selectEvent) {
    activityEditor.setDate((Date) selectEvent.getObject());
  }


  private void createCalendarEvents() {
    eventModel = new DefaultScheduleModel();
    for (ActivityDTO activity : activitiesInDb) {
      String username = "";
      if (activity.getAssigentTo_Username() != null) {
        username = " (" + activity.getAssigentTo_Username() + ")";
      }
      DefaultScheduleEvent event =
          new DefaultScheduleEvent(activity.getName() + username, activity.getDate(),
              activity.getDate(), true);
      event.setDescription(ms.getString("activity") + activity.getId().toString());
      if (activity.getAssignedToUserId() != null) {
        event.setStyleClass("styleClassUser" + (activity.getAssignedToUserId() % 10 + 1));
      }

      eventModel.addEvent(event);
    }

    for (AbsenceDTO absence : absences) {
      DefaultScheduleEvent event =
          new DefaultScheduleEvent(absence.getUserGroup().getUserName() + " "
              + ms.getString("mAbsence"), absence.getAwayFrom(), absence.getAwayTill(), true);
      event.setDescription(ms.getString("mAbsence") + absence.getId());
      event.setStyleClass("absenceClass");
      eventModel.addEvent(event);
    }
  }

  // ToDo: Make it possible to change month/year like in normal Calendar
  // ToDo: Consider a week view like in normal Calendar
  private void createGroupCalendarRows() {
    rows = new ArrayList<>();
    Calendar calendar = new GregorianCalendar();
    // Create a row foreach Day of the current month
    for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

      // Foreach row create all cells
      ActivityDTO[] columns = new ActivityDTO[users.size() + 1];

      // Foreach row create the according Date and format it to show Number + Name of Day
      String inputDate =
          i + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
      SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
      Date dt1 = new Date();
      try {
        dt1 = format1.parse(inputDate);
      } catch (Exception ex) {

      }
      DateFormat format2 = new SimpleDateFormat("EE");
      String finalDay = format2.format(dt1);

      // The day shall be shown in the first column. An Activity-Object has to be created,
      // because the rows actually are Activity-Arrays
      ActivityDTO dateActivity = new ActivityDTO();
      dateActivity.setName(String.format("%-2s %02d.", finalDay, i));
      columns[0] = dateActivity;

      // Add the new row (at this time it only contains the day number + name)
      rows.add(columns);

      // Now go through all activities to show them in the right cell
      for (ActivityDTO act : activities) {
        // Check if the activity has to be put in the current row
        // = has its date at the current row-day
        if (format1.format(act.getDate()).equals(format1.format(dt1))) {
          // The date of the activity equals the current row-day
          // Now find out, in which column the activity has to been put
          // (=which user is assigned to the activity)
          for (int j = 0; j < users.size(); j++) {
            if (act.getAssignedToUserId() != null
                && act.getAssignedToUserId().equals(users.get(j).getId())) {
              if (columns[j + 1] == null) {
                columns[j + 1] = act;
              } else {
                boolean newRowNeeded = true;
                for (int a = i; a < rows.size(); a++) {
                  if (rows.get(a)[j + 1] == null) {
                    rows.get(a)[j + 1] = act;
                    newRowNeeded = false;
                    break;
                  }
                }
                if (newRowNeeded) {
                  ActivityDTO[] newColumns = new ActivityDTO[users.size() + 1];
                  newColumns[j + 1] = act;
                  rows.add(newColumns);
                }
              }
              break;
            }
          }
        }
      }
    }
  }
}
