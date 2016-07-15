package com.smartwg.core.internal.repositories.impl;


import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.NotificationDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.util.Constants;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Kamil Sierzant (ks), Tobias Ortmayr (to)
 */
@Named
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

  @SuppressWarnings("unchecked")
  @Override
  public User findConfirmedUser(final String name) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_CONFIRMED_USER);
    query.setParameter("name", name);

    List<User> users = query.getResultList();
    if (users.isEmpty()) {
      return null;
    }
    return users.get(0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<User> findUsers(final String name, final String email) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_USER_NAME_EMAIL);
    query.setParameter("name", name);
    query.setParameter("email", email);
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<UserDTO> findAllDTOs() {
    final Query query = em.createNamedQuery(Constants.FIND_ALL_USERS);

    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<UserDTO> findUsersByGroupID(final Integer groupId) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_USERS_BY_GROUP);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }


  @SuppressWarnings("unchecked")
  @Override
  public UserDTO findByMail(final String email) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_USER_BY_EMAIL);
    query.setParameter("mail", email);
    final List<UserDTO> result = query.getResultList();
    return !result.isEmpty() ? result.get(0) : null;
  }

  @Override
  public List<NotificationType> findActivatedNotifications(final Integer userId) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_USER_NOTIFICATIONS);
    query.setParameter("userId", userId);
    final String singleResult = (String) query.getSingleResult();

    final List<NotificationType> result = new ArrayList<>();
    if (StringUtils.isNotBlank(singleResult)) {
      for (String notificationType : singleResult.split(";")) {
        result.add(NotificationType.valueOf(notificationType));
      }
    }

    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<NotificationDTO> findUsersEmailsByGroupId(final Integer groupId) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_USER_EMAILS_BY_GROUP_ID);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<String> findGroupMembersEmails(final Integer groupId) {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_GROUP_MEMBERS_EMAILS);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<UserDTO> findAllAdmins(final Integer groupId) {
    Query query = em.createNamedQuery(Constants.QUERY_ALL_ADMINS);
    return query.setParameter("group", groupId).getResultList();
  }
}
