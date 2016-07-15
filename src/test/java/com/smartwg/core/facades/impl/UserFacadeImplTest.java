package com.smartwg.core.facades.impl;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.services.AbsenceService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.UserService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserFacadeImplTest {

  private static final Integer USER_ID = 2;
  private static final Integer GROUP_ID = 3;
  private static final String EMAIL = "sierzant.kamil@gmail.com";
  private static final String PASSWORD = "password";
  private static final String PAGE_LINK = "localhost:8080/fairShare";

  @InjectMocks
  private UserFacade facade;

  @Mock
  private UserService userService;
  @Mock
  private AbsenceService absenceService;
  @Mock
  private EmailService emailService;

  @Mock
  private UserDTO userDTO;
  @Mock
  private UserGroupDTO userGroupDTO;
  @Mock
  private GroupDTO groupDTO;

  @BeforeMethod
  public void setUp() {
    facade = new UserFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createUser() {
    facade.createUser(userDTO);

    verify(userService, times(1)).createUser(eq(userDTO));
  }

  public void confirmUserRegistration() {
    when(userService.confirmUserRegistration(eq(USER_ID), eq("token"))).thenReturn(true);

    final boolean result = facade.confirmUserRegistration(USER_ID, "token");

    assertTrue(result);
  }

  public void getAllUsers() {
    when(userService.getAllUsersDTO()).thenReturn(singletonList(userDTO));
    final List<UserDTO> result = facade.getAllUsers();

    assertEquals(result, singletonList(userDTO));
  }

  public void login() {
    when(userService.login(EMAIL, PASSWORD)).thenReturn(userDTO);

    final UserDTO result = facade.login(EMAIL, PASSWORD);

    assertEquals(result, userDTO);
  }

  public void verifyUnique() {
    when(userService.verifyUnique(EMAIL, PASSWORD)).thenReturn(true);

    final boolean result = facade.verifyUnique(EMAIL, PASSWORD);

    assertTrue(result);
  }

  public void isNotificationActivated() {
    when(userService.isNotificationActivated(USER_ID, NotificationType.BILL)).thenReturn(true);

    final boolean result = facade.isNotificationActivated(USER_ID, NotificationType.BILL);

    assertTrue(result);
  }

  public void sendRegistrationConfirmationMail() {
    facade.sendRegistrationConfirmationMail(userDTO, PAGE_LINK);

    verify(emailService, times(1)).createAndSendRegistrationConfirmationEmail(userDTO, PAGE_LINK);
  }

  public void findUsersByGroupID() {
    when(userService.findUsersByGroupID(GROUP_ID)).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = facade.findUsersByGroupID(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }

  public void findByEmail() {
    when(userService.findByEmail(EMAIL)).thenReturn(userDTO);

    final UserDTO result = facade.findByEmail(EMAIL);

    assertEquals(result, userDTO);
  }

  public void sendPassworResetMail() {
    facade.sendPassworResetMail(userDTO, PAGE_LINK);

    verify(emailService, times(1)).createAndSendPasswordResetEmail(userDTO, PAGE_LINK);
  }

  public void findById() {
    when(userService.findById(USER_ID)).thenReturn(userDTO);

    final UserDTO result = facade.findById(USER_ID);

    assertEquals(result, userDTO);
  }

  public void updateUser() {
    facade.updateUser(userDTO);

    verify(userService, times(1)).updateUser(eq(userDTO));
  }

  public void updateUserGroup() {
    facade.updateUserGroup(userGroupDTO);

    verify(userService, times(1)).updateUserGroup(eq(userGroupDTO));
  }

  public void removeUserGroup() {
    facade.removeUserGroup(userGroupDTO);

    verify(userService, times(1)).removeUserGroup(eq(userGroupDTO));
  }

  public void findAllAdmins() {
    when(userService.findAllAdmins(GROUP_ID)).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = facade.findAllAdmins(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }

  public void findByUserNameOrMail() {
    when(userService.findByUserNameOrMail(EMAIL)).thenReturn(userDTO);

    final UserDTO result = facade.findByUserNameOrMail(EMAIL);

    assertEquals(result, userDTO);
  }

  public void sendUserAddedInfoMail() {
    final Set<String> recipients = new HashSet<>();
    when(userService.findGroupMembersEmailsByGroupId(eq(GROUP_ID))).thenReturn(recipients);

    facade.sendUserAddedInfoMail(userDTO, groupDTO);

    verify(emailService, times(1)).createAndSendUserAddedToGroupInfoMail(eq(userDTO), eq(groupDTO),
        eq(recipients));
  }
}
