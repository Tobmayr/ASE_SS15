package com.smartwg.core.controllers.activities;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.util.Constants;

@Named("activityActions")
@Scope("session")
public class ActivityActionsBean {

  @Inject
  private NavigationBean navigation;
  @Inject
  private ActivityFacade facade;

  public ActivityActionsBean() {

  }

  @PostConstruct
  public void init() {

  }

  public String editActivity(final ActivityDTO activity, String returnPage) {
    return navigation.getPageNewActivity() + Constants.PAGE_REDIRECT + "&actId=" + activity.getId()
        + "&returnPage=" + returnPage;
  }

  public String deleteActivity(final ActivityDTO activity, String returnPage) {
    facade.deleteActivity(activity);
    return returnPage + Constants.PAGE_REDIRECT;
  }
}
