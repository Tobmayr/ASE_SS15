package com.smartwg.core.controllers.calendar;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.activities.ActivityEditorBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.util.Constants;

@Named("activityPlaner")
@Scope("view")
public class ActivityPlanerBean {
  
  private ResourceBundle ms = SmartWG.getMessageBundle();
  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private ActivityEditorBean activityEditor;
  private List<ActivityDTO> activities;
  private DefaultScheduleModel eventModel;

  @PostConstruct
  public void init() {

    createCalendarEvents();
  }

  private void createCalendarEvents() {
    //TODO revert;
    activities= activityFacade.getActivitiesForGroup(userBean.getCurrentUserGroup().getGroupId());
    //activities = activityFacade.findPlanedActivites(userBean.getCurrentUserGroup().getGroupId());
    eventModel = new DefaultScheduleModel();
    for (ActivityDTO activity : activities) {
      DefaultScheduleEvent event =
          new DefaultScheduleEvent(activity.getName(), activity.getDate(), activity.getDate(), true);
      event.setDescription(ms.getString("activity") + activity.getId().toString());
      event.setStyleClass("user2");
      eventModel.addEvent(event);
    }

  }

  public void onDateSelect(SelectEvent selectEvent) {
    activityEditor.setActivity(new ActivityDTO());
    activityEditor.setDate((Date) selectEvent.getObject());

  }

  public void createRecurringActivity() {
    //TODO: revert
    //activityEditor.createRecurringActivity();
    activityEditor.createActivity(false);
    createCalendarEvents();


  }

  public void onActivitySelect(SelectEvent selectEvent) {
    ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
    activityEditor.setActivity(new ActivityDTO());
    for (ActivityDTO activity : activities) {
      if ((ms.getString("activity") + activity.getId()).equals(event.getDescription())) {
        activityEditor.setActivity(activity);
        break;
      }
    }
  }

  public DefaultScheduleModel getEventModel() {
    return eventModel;
  }

  public void setEventModel(DefaultScheduleModel eventModel) {
    this.eventModel = eventModel;
  }



}
