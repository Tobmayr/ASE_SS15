package com.smartwg.core.internal.repositories.impl;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.NotificationDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.util.Constants;

@Test
public class UserRepositoryImplTest {

  private static final String NAME = "name";
  private static final String EMAIL = "email";
  private static final Integer GROUP_ID = 3;
  private static final Integer USER_ID = 1;

  @InjectMocks
  private UserRepository repository;
  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;

  @Mock
  private User user;
  @Mock
  private UserDTO userDTO;
  @Mock
  private NotificationDTO notificationDTO;

  @BeforeMethod
  public void setUp() {
    repository = new UserRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findConfirmedUser_Succeed() {
    final String name = "CONFIRMED";

    when(entityManager.createNamedQuery(Constants.QUERY_FIND_CONFIRMED_USER)).thenReturn(query);
    when(query.setParameter(NAME, name)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(user));

    final User result = repository.findConfirmedUser(name);
    assertEquals(result, user);

  }

  public void findConfirmedUserFail() {
    final String name = "UNCONFIRMED";

    when(entityManager.createNamedQuery(Constants.QUERY_FIND_CONFIRMED_USER)).thenReturn(query);
    when(query.setParameter(NAME, name)).thenReturn(query);
    when(query.getResultList()).thenReturn(new ArrayList<User>());

    final User result = repository.findConfirmedUser(name);
    assertEquals(result, null);
  }

  public void findUsers() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_NAME_EMAIL)).thenReturn(query);
    when(query.setParameter(NAME, NAME)).thenReturn(query);
    when(query.setParameter(EMAIL, EMAIL)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(user));

    final List<User> result = repository.findUsers(NAME, EMAIL);
    assertEquals(result, singletonList(user));
  }

  public void findAllDTOs() {
    when(entityManager.createNamedQuery(Constants.FIND_ALL_USERS)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(user));

    final List<UserDTO> result = repository.findAllDTOs();

    assertEquals(result, singletonList(user));
  }

  public void findUsersByGroupID() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USERS_BY_GROUP)).thenReturn(query);
    when(query.setParameter("groupId", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = repository.findUsersByGroupID(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }

  public void findByMailSucceed() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_BY_EMAIL)).thenReturn(query);
    when(query.setParameter("main", EMAIL)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(userDTO));

    final UserDTO result = repository.findByMail(EMAIL);

    assertEquals(result, userDTO);
  }

  public void findByMailWhenUserNotFound() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_BY_EMAIL)).thenReturn(query);
    when(query.setParameter("main", EMAIL)).thenReturn(query);

    final UserDTO result = repository.findByMail(EMAIL);

    assertNull(result);
  }

  public void findActivatedNotifications() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_NOTIFICATIONS)).thenReturn(query);
    when(query.setParameter("userId", USER_ID)).thenReturn(query);
    when(query.getSingleResult()).thenReturn("ACTIVITY;BILL;");

    final List<NotificationType> result = repository.findActivatedNotifications(USER_ID);

    assertEquals(result, Arrays.asList(NotificationType.ACTIVITY, NotificationType.BILL));
  }

  public void findUsersEmailsByGroupId() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_USER_EMAILS_BY_GROUP_ID)).thenReturn(
        query);
    when(query.setParameter("groupId", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(notificationDTO));

    final List<NotificationDTO> result = repository.findUsersEmailsByGroupId(GROUP_ID);

    assertEquals(result, singletonList(notificationDTO));
  }

  public void findGroupMembersEmails() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_GROUP_MEMBERS_EMAILS)).thenReturn(
        query);
    when(query.setParameter("groupId", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(EMAIL));

    final List<String> result = repository.findGroupMembersEmails(GROUP_ID);

    assertEquals(result, singletonList(EMAIL));
  }

  public void findAllAdmins() {
    when(entityManager.createNamedQuery(Constants.QUERY_ALL_ADMINS)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = repository.findAllAdmins(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }
}
