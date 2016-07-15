package com.smartwg.core.controllers.activities;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.controllers.workDistribution.WorkDistributionBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.RecurringDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;

/**
 * @author Matthias Höllthaler (mh), Tobias Ortmayr(to), Anna Sadriu (as)
 */

@Named("activityEditor")
@Scope("view")
public class ActivityEditorBean {
    @Inject
    private NavigationBean navigation;
    @Inject
    private UserBean userBean;
    @Inject
    private WorkDistributionBean workDistributionBean;
    @Inject
    private ActivityFacade facade;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityEditorBean.class);
    private ActivityDTO activity;
    private List<UserActivityDTO> userActivities;
    private List<UserDTO> users;
    private String returnPage = null;
    private Integer userRating = 0;

    public ActivityEditorBean() {

    }

    @PostConstruct
    public void init() {
        final Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String actId = requestParameterMap.get("actId");
        returnPage = requestParameterMap.get("returnPage");
        if (actId != null) {
            final Integer id = Integer.valueOf(actId);
            activity = facade.findById(id);
        } else {
            activity = new ActivityDTO();
        }
        userActivities = activity.getRatings();
        users = userBean.getUsersOfCurrentGroup();
    }

    public void reset() {
        activity = new ActivityDTO();
    }

    public String createActivity(boolean docontinue) {
        LOGGER.info("createActivity() invoked: " + this.userRating);

        if (activity.getCreatedByUserId() == null) {
            activity.setCreatedByUserId(userBean.getCurrentUser().getId());
            activity.setCreatedBy_Username(userBean.getCurrentUser().getUserName());
        }
        if (activity.getGroupId() == null) {
            activity.setGroupId(userBean.getCurrentUserGroup().getGroupId());
        }
        if (activity.getAssignedToUserId() != null && activity.getAssigentTo_Username() == null) {
            for (UserDTO u : users) {
                if (u.getId().equals(activity.getAssignedToUserId())) {
                    activity.setAssigentTo_Username(u.getUserName());
                    break;
                }
            }
        }
        activity.setCreatedByUserId(userBean.getCurrentUser().getId());
        activity.setGroupId(userBean.getCurrentUserGroup().getGroupId());

        activity.setRatingFromUser(this.userRating, userBean.getCurrentUser().getUserName());

        if (activity.getId() == null) {
            facade.createActivity(activity);
        } else {
            facade.updateActivity(activity, userBean.getCurrentUser().getId());
        }

        if (docontinue) {
            return navigation.getPageNewActivity() + Constants.PAGE_REDIRECT;
        } else {
            return navigation.getPageActivityOverview();
        }
    }

    public void createRecurringActivity() {

        if (activity.getCreatedByUserId() == null) {
            activity.setCreatedByUserId(userBean.getCurrentUser().getId());
            activity.setCreatedBy_Username(userBean.getCurrentUser().getUserName());
        }
        if (activity.getGroupId() == null) {
            activity.setGroupId(userBean.getCurrentUserGroup().getGroupId());
        }
        activity.setCreatedByUserId(userBean.getCurrentUser().getId());
        activity.setGroupId(userBean.getCurrentUserGroup().getGroupId());
        activity.setRatingFromUser(this.userRating, userBean.getCurrentUser().getUserName());
        if (activity.getRecurring() == null) {
            RecurringDTO recurringDTO = new RecurringDTO();
            recurringDTO.setDate(new Date());
            recurringDTO.setEndDate(null);
            recurringDTO.setTimes(0);
            recurringDTO.setValue(1);
            recurringDTO.setRecurringType(RecurringType.MONTH);
            activity.setRecurring(recurringDTO);
        }

        if (activity.getId() == null) {
            facade.createActivity(activity);
        } else {
            facade.updateActivity(activity, userBean.getCurrentUser().getId());
        }
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
        userActivities = activity.getRatings();
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    public void setDate(Date date) {
        reset();
        this.activity.setDate(date);
    }

    public Integer getUserRating() {
        userRating = 0;
        for (UserActivityDTO ua : userActivities) {
            if (ua.getUserId().equals(userBean.getCurrentUser().getId())) {
                userRating = ua.getRating();
                break;
            }
        }
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        LOGGER.info("size: " + userActivities.size() + " : " + userBean.getCurrentUser().getUserName() + " : " + activity.getName());
        boolean userNotVotedYet = true;
        if (userActivities.size() != 0) {
            for (UserActivityDTO ua : userActivities) {
                if (ua.getUserId().equals(userBean.getCurrentUser().getId())) {
                    userNotVotedYet = false;
                    ua.setRating(userRating);
                    break;
                }
            }
        }

        if (userActivities.size() == 0 || userNotVotedYet) {
            activity.setRatingFromUser(userRating, userBean.getCurrentUser().getUserName());
        }

        this.userRating = userRating;
    }

    public void setDone(final ActivityDTO activity, boolean isDone) {
        reset();
        if (activity.getRatingFromUser(userBean.getCurrentUser().getUserName()) == 0 && !isDone) {
            activity.setRatingFromUser((int) workDistributionBean.calculateMedian(activity.getId()), userBean.getCurrentUser().getUserName());
        }
        activity.setIsDone(!isDone);
        facade.updateActivity(activity, userBean.getCurrentUser().getId());
    }
}
