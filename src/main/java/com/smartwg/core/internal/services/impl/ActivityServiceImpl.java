package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.repositories.ActivityRepository;
import com.smartwg.core.internal.repositories.UserActivityRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.ActivityService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Matthias Höllthaler (mh), Tobias Ortmayr(to), Anna Sadriu (as)
 */
@Named
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Inject
    private ActivityRepository repository;
    @Inject
    private UserActivityRepository userActivityRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private EntityConverter factory;
    @Inject
    private UserService userService;
    @Inject
    private EmailService emailService;

    @Override
    public void createActivity(final ActivityDTO activityDTO) {
        Activity activity = factory.createActivity(activityDTO);
        if (activityDTO.getId() == null) {
            sendEmailNotification(activityDTO);
        }
        LOGGER.info(String.format("Creating Activity: %s", activity.toString()));
        activity.setCreatedBy(userRepository.findById(activity.getCreatedBy().getId()));
        if (activity.getAssignedTo() != null && activity.getAssignedTo().getId() != null) {
            activity.setAssignedTo(userRepository.findById(activity.getAssignedTo().getId()));
        }
        activity = repository.save(activity);

        final UserActivity userActivity = new UserActivity();
        userActivity.setActivity(activity);
        userActivity.setUser(activity.getCreatedBy());
        userActivity.setRating(activityDTO.getRatingFromUser(activity.getCreatedBy().getUserName()));
        LOGGER.info(String.format("Save UserActivity: %s", userActivity.toString()));
        userActivityRepository.save(userActivity);
    }

    private void sendEmailNotification(final ActivityDTO activityDTO) {
        final HashSet<String> recipients = new HashSet<>();
        for (String recipient : userService.findUsersEmailsByGroupId(activityDTO.getGroupId(),
                NotificationType.ACTIVITY)) {
            recipients.add(recipient);
        }
        emailService.createAndSendActivityCreatedEmail(activityDTO, recipients);
    }

    @Override
    public void updateActivity(final ActivityDTO activityDTO, final Integer userId) {
        Activity activity = factory.createActivity(activityDTO);

        LOGGER.info(String.format("Update Activity: %s", activity.toString()));
        repository.merge(activity);

        User user = null;
        if (userId != null && userId != 0) {
            user = userRepository.findById(userId);
        }
        if (user != null) {
            Activity act = repository.findById(activity.getId());
            if (act != null) {
                UserActivity userActivity = new UserActivity();
                userActivity.setActivity(act);
                userActivity.setUser(user);
                userActivity.setRating(activityDTO.getRatingFromUser(user.getUserName()));
                LOGGER.info(String.format("Update UserActivity: %s", userActivity.toString()));
                userActivityRepository.merge(userActivity);
            }
        }
    }

    @Override
    public void deleteActivity(final ActivityDTO activityDTO) {
        final Activity activity = new Activity();
        activity.setId(activityDTO.getId());

        LOGGER.info(String.format("Delete UserActivities: %s", activityDTO.getRatings()));
        for (UserActivityDTO uaDTO : activityDTO.getRatings()) {
            final UserActivity userActivity =
                    userActivityRepository.findByUserIdActivityId(uaDTO.getUserId(), uaDTO.getActivityId());

            LOGGER.info(String.format("Delete UserActivity: %s", userActivity));
            userActivityRepository.delete(userActivity);
        }

        LOGGER.info(String.format("Delete Activity: %s", activity.getId()));
        repository.delete(activity);
    }

    @Override
    public List<Activity> getAllActivities() {
        return repository.findAll();
    }

    @Override
    public List<ActivityDTO> getActivitiesForGroup(final Integer groupID) {
        return repository.getActivitiesForGroup(groupID);
    }

    @Override
    public List<UserActivityDTO> getRatingsForActivity(final Integer activityID) {
        return repository.getRatingsForActivity(activityID);
    }

    @Override
    public List<ActivityDTO> getActivitiesBetweenTimespan(final Date start, final Date end,
                                                          final Integer groupID) {
        final List<ActivityDTO> activityDTOs = new ArrayList<ActivityDTO>();
        for (Activity activity : repository.getActivitiesBetweenTimespan(start, end, groupID)) {
            activityDTOs.add(new ActivityDTO(activity));
        }
        return activityDTOs;
    }

    @Override
    public ActivityDTO findById(final Integer id) {
        return repository.findActivityById(id);
    }

    @Override
    public List<ActivityDTO> findAssignedActivitesBetweenTimespan(final Date start, final Date end,
                                                                  final Integer groupID, final Integer userID) {
        return repository.getAssignedActivitesBetweenTimespan(start, end, groupID, userID);
    }

    @Override
    public List<ActivityDTO> findByParameters(final Integer groupId, final Integer assigneeId,
                                              final Date start, final Date end, final String isDone, final Integer activityId) {
        return repository.findByParameters(groupId, assigneeId, start, end, isDone, activityId);
    }

    @Override
    public List<ActivityDTO> findPlanedActivites(Integer groupId) {
        return repository.findPlanedActivites(groupId);
    }

    @Override
    public void setAsDone(final Integer activityId, Integer userId) {
        final ActivityDTO activity = repository.findActivityById(activityId);
        if (activity == null) {
            return;
        }
        activity.setIsDone(Boolean.TRUE);
        activity.setAssignedToUserId(userId);
        updateActivity(activity, userId);
    }
}
