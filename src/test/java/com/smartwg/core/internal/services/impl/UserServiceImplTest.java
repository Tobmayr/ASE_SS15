package com.smartwg.core.internal.services.impl;

import static com.smartwg.core.internal.domain.NotificationType.ACTIVITY;
import static com.smartwg.core.internal.domain.NotificationType.BILL;
import static com.smartwg.core.internal.domain.NotificationType.SHOPPING_LIST;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.NotificationDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.PasswordEncryptionService;
import com.smartwg.core.internal.services.UserService;

@Test
public class UserServiceImplTest {

  private static final Integer USER_ID = 3;
  private static final Integer GROUP_ID = 4;
  private static final String TOKEN = "token";
  private static final String EMAIL = "sierzant.kamil@gmail.com";
  private static final String SECOND_EMAIL = "test@test.com";

  @InjectMocks
  private UserService service;

  @Mock
  private UserRepository userRepository;
  @Mock
  private GroupRepository groupRepository;
  @Mock
  private UserGroupRepository userGroupRepository;

  @Mock
  private PasswordEncryptionService passwordEncryptionService;
  @Mock
  private EntityConverter factory;
  @Mock
  private UserDTO userDTO;
  @Mock
  private User user;
  @Mock
  private NotificationDTO notificationDTO;
  @Mock
  private NotificationDTO secondNotificationDTO;
  @Mock
  private UserGroupDTO userGroupDTO;
  @Mock
  private UserGroup userGroup;
  @Mock
  private Group group;

  @BeforeMethod
  public void setUp() {
    service = new UserServiceImpl();

    MockitoAnnotations.initMocks(this);
  }

  public void createUserSucceed() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final byte[] salt = new byte[2];
    final byte[] password = new byte[2];
    final String pass = "PW";

    when(userDTO.getPassword_clear()).thenReturn(pass);
    when(passwordEncryptionService.generateSalt()).thenReturn(salt);
    when(passwordEncryptionService.getEncryptedPassword(pass, salt)).thenReturn(password);
    when(factory.createUser(eq(userDTO))).thenReturn(user);
    when(userRepository.save(eq(user))).thenReturn(user);
    when(user.getFirstName()).thenReturn("first name");
    when(user.getEmail()).thenReturn("sierzant.kamil@gmail.com");

    final UserDTO result = service.createUser(userDTO);

    verify(userDTO, times(1)).setPassword(eq(password));
    verify(userDTO, times(1)).setSalt(eq(salt));
    assertEquals(result.getFirstName(), "first name");
    assertEquals(result.getEmail(), EMAIL);
  }

  public void loginSucceed() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String email = "test@mail.at";
    final String password = "LetMeLogin";
    final byte[] pass = new byte[1];
    final byte[] salt = new byte[1];
    final User user = createUser(1, email, "RegisteredUser", pass, salt);

    when(userRepository.findConfirmedUser(email)).thenReturn(user);
    when(passwordEncryptionService.authenticate(password, pass, salt)).thenReturn(true);

    final UserDTO login = service.login(email, password);

    verify(userRepository).findConfirmedUser(email);
    verify(passwordEncryptionService).authenticate(password, pass, salt);
    assertNotNull(login);
    assertEquals(login, new UserDTO(user));
  }

  public void loginInvalidPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String email = "test@mail.at";
    final String passwordFail = "wrongPass";
    final byte[] pass = new byte[1];
    final byte[] salt = new byte[1];
    final User user = createUser(1, email, "RegisteredUser", pass, salt);

    when(userRepository.findConfirmedUser(email)).thenReturn(user);
    when(passwordEncryptionService.authenticate(passwordFail, pass, salt)).thenReturn(false);

    final UserDTO login = service.login(email, passwordFail);

    verify(userRepository).findConfirmedUser(email);
    verify(passwordEncryptionService).authenticate(passwordFail, pass, salt);
    assertEquals(login, null);
  }

  public void loginInvalidEmail() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String emailFail = "test@mail";
    final String password = "LetMeLogin";

    when(userRepository.findConfirmedUser(emailFail)).thenReturn(null);

    final UserDTO login = service.login(emailFail, password);

    verify(userRepository).findConfirmedUser(emailFail);
    assertEquals(login, null);
  }

  public void verifyUnique_succeed() {
    final String username = "name";
    final String email = "email";
    when(userRepository.findUsers(username, email)).thenReturn(singletonList(user));

    boolean result = service.verifyUnique(username, email);
    assertEquals(result, false);
  }

  public void verifyUnique_fail() {
    final String username = "name";
    final String email = "email";
    when(userRepository.findUsers(username, email)).thenReturn(new ArrayList<>());

    boolean result = service.verifyUnique(username, email);

    assertEquals(result, true);
  }

  public void getAllUsers() {
    when(userRepository.findAll()).thenReturn(singletonList(user));

    final List<User> result = service.getAllUsers();

    assertEquals(result, singletonList(user));
  }

  public void getAllUsersDTO() {
    when(userRepository.findAllDTOs()).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = service.getAllUsersDTO();

    assertEquals(result, singletonList(userDTO));
  }

  public void confirmUserRegistrationSucceed() {
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(user.isConfirmed()).thenReturn(false);
    when(user.getConfirmCode()).thenReturn(TOKEN);

    service.confirmUserRegistration(USER_ID, TOKEN);

    verify(user, times(1)).setConfirmed(eq(Boolean.TRUE));
    verify(userRepository, times(1)).merge(eq(user));
  }

  @Test(dataProvider = "confirmationFailed")
  public void confirmUserRegistrationFails(final User user, final boolean isConfirmed,
      final String token) {
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(this.user.isConfirmed()).thenReturn(isConfirmed);
    when(this.user.getConfirmCode()).thenReturn(token);

    service.confirmUserRegistration(USER_ID, TOKEN);

    verify(this.user, never()).setConfirmed(anyBoolean());
    verify(userRepository, never()).merge(any(User.class));
  }

  @DataProvider(name = "confirmationFailed")
  private Object[][] getInvalidConfirmationData() {
    return new Object[][] { {null, false, "invalidToken"}, {user, true, "invalidToken"},
        {user, false, "invalidToken"}};
  }

  private User createUser(int id, String email, String username, byte[] pass, byte[] salt) {
    User user = new User();
    user.setId(id);
    user.setEmail(email);
    user.setUserName(username);
    user.setPassword(pass);
    user.setSalt(salt);
    return user;
  }

  public void findUserById() {
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);

    final User result = service.findUserById(USER_ID);

    assertEquals(result, user);
  }
  
  public void findById() {
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(user.getFirstName()).thenReturn("first name");
    when(user.getEmail()).thenReturn(EMAIL);

    final UserDTO result = service.findById(USER_ID);

    assertEquals(result.getFirstName(), "first name");
    assertEquals(result.getEmail(), EMAIL);
  }

  public void findUsersByGroupID() {
    when(userRepository.findUsersByGroupID(eq(GROUP_ID))).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = service.findUsersByGroupID(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }

  public void findByEmail() {
    when(userRepository.findByMail(eq(EMAIL))).thenReturn(userDTO);

    final UserDTO result = service.findByEmail(EMAIL);

    assertEquals(result, userDTO);
  }

  public void isNotificationActivated() {
    when(userRepository.findActivatedNotifications(eq(USER_ID))).thenReturn(
        asList(ACTIVITY, BILL, SHOPPING_LIST));

    final boolean result = service.isNotificationActivated(USER_ID, ACTIVITY);

    assertTrue(result);
  }

  @Test(dataProvider = "notificationTypes")
  public void findUsersEmailsByGroupId(final NotificationType notificationType,
      final String firstUserNotifications, final String secondUserNotifications,
      final List<String> expectedEmails) {
    when(userRepository.findUsersEmailsByGroupId(eq(GROUP_ID))).thenReturn(
        asList(notificationDTO, secondNotificationDTO));
    when(notificationDTO.getActiveNotificationTypes()).thenReturn(firstUserNotifications);
    when(notificationDTO.getEmail()).thenReturn(EMAIL);
    when(secondNotificationDTO.getActiveNotificationTypes()).thenReturn(secondUserNotifications);
    when(secondNotificationDTO.getEmail()).thenReturn(SECOND_EMAIL);

    final List<String> result = service.findUsersEmailsByGroupId(GROUP_ID, notificationType);

    assertEquals(result, expectedEmails);
  }

  @DataProvider(name = "notificationTypes")
  private Object[][] getNotificationTypes() {
    return new Object[][] {
        {ACTIVITY, "ACTIVITY;BILL;SHOPPING_LIST", "ACTIVITY;BILL;SHOPPING_LIST",
            asList(EMAIL, SECOND_EMAIL)},
        {ACTIVITY, "", "", Collections.EMPTY_LIST},
        {ACTIVITY, "ACTIVITY;", "ACTIVITY;", asList(EMAIL, SECOND_EMAIL)},
        {ACTIVITY, "BILL;", "ACTIVITY;", singletonList(SECOND_EMAIL)},
        {ACTIVITY, "ACTIVITY;", "BILL;", singletonList(EMAIL)},

        {BILL, "ACTIVITY;BILL;SHOPPING_LIST", "ACTIVITY;BILL;SHOPPING_LIST",
            asList(EMAIL, SECOND_EMAIL)},
        {BILL, "", "", Collections.EMPTY_LIST},
        {BILL, "BILL;", "BILL;", asList(EMAIL, SECOND_EMAIL)},
        {BILL, "BILL;", "ACTIVITY;", singletonList(EMAIL)},
        {BILL, "ACTIVITY;", "BILL;", singletonList(SECOND_EMAIL)},

        {SHOPPING_LIST, "ACTIVITY;BILL;SHOPPING_LIST", "ACTIVITY;BILL;SHOPPING_LIST",
            asList(EMAIL, SECOND_EMAIL)}, {SHOPPING_LIST, "", "", Collections.EMPTY_LIST},
        {SHOPPING_LIST, "SHOPPING_LIST", "SHOPPING_LIST", asList(EMAIL, SECOND_EMAIL)},
        {SHOPPING_LIST, "SHOPPING_LIST", "ACTIVITY;", singletonList(EMAIL)},
        {SHOPPING_LIST, "ACTIVITY;", "SHOPPING_LIST", singletonList(SECOND_EMAIL)}};
  }

  public void updateUserSucceed() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final byte[] salt = new byte[2];
    final byte[] password = new byte[2];
    final String pass = "PW";

    when(userDTO.getPassword_clear()).thenReturn(pass);
    when(passwordEncryptionService.generateSalt()).thenReturn(salt);
    when(passwordEncryptionService.getEncryptedPassword(pass, salt)).thenReturn(password);
    when(factory.createUser(eq(userDTO))).thenReturn(user);
    when(userRepository.save(eq(user))).thenReturn(user);

    service.updateUser(userDTO);

    verify(userDTO, times(1)).setPassword(eq(password));
    verify(userDTO, times(1)).setSalt(eq(salt));
    verify(userRepository, times(1)).merge(eq(user));
  }

  @Test(dataProvider = "invalidPasswords")
  public void updateUserFailsWhenProvidedPasswordIsInvalid(String pass)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    when(userDTO.getPassword_clear()).thenReturn(pass);
    when(factory.createUser(eq(userDTO))).thenReturn(user);
    when(userRepository.save(eq(user))).thenReturn(user);

    service.updateUser(userDTO);

    verify(passwordEncryptionService, never()).generateSalt();
    verify(passwordEncryptionService, never()).getEncryptedPassword(any(), any());
    verify(userDTO, never()).setPassword(any());
    verify(userDTO, never()).setSalt(any());
    verify(userRepository, times(1)).merge(eq(user));
  }

  @DataProvider(name = "invalidPasswords")
  private Object[][] getInvalidPasswords() {
    return new Object[][] { {null}, {""}, {"   "}};
  }

  public void updateUserGroup() {
    when(factory.createUserGroup(eq(userGroupDTO))).thenReturn(userGroup);
    when(userGroup.getUser()).thenReturn(user);
    when(userGroup.getGroup()).thenReturn(group);
    when(user.getId()).thenReturn(USER_ID);
    when(group.getId()).thenReturn(GROUP_ID);
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(groupRepository.findById(eq(GROUP_ID))).thenReturn(group);

    service.updateUserGroup(userGroupDTO);

    verify(userGroup, times(1)).setUser(eq(user));
    verify(userGroup, times(1)).setGroup(eq(group));
    verify(userGroupRepository, times(1)).merge(userGroup);
  }

  public void removeUserGroup() {
    when(factory.createUserGroup(eq(userGroupDTO))).thenReturn(userGroup);
    when(userGroup.getUser()).thenReturn(user);
    when(userGroup.getGroup()).thenReturn(group);
    when(user.getId()).thenReturn(USER_ID);
    when(group.getId()).thenReturn(GROUP_ID);
    when(userRepository.findById(eq(USER_ID))).thenReturn(user);
    when(groupRepository.findById(eq(GROUP_ID))).thenReturn(group);

    service.removeUserGroup(userGroupDTO);

    verify(userGroup, times(1)).setDeleted(eq(Boolean.TRUE));
    verify(userGroup, times(1)).setUser(eq(user));
    verify(userGroup, times(1)).setGroup(eq(group));
    verify(userGroupRepository, times(1)).merge(userGroup);
  }

  public void findGroupMembersEmailsByGroupId() {
    when(userRepository.findGroupMembersEmails(eq(GROUP_ID))).thenReturn(singletonList(EMAIL));

    final Set<String> result = service.findGroupMembersEmailsByGroupId(GROUP_ID);

    assertEquals(result, singletonList(EMAIL));
  }

  public void findAllAdmins() {
    when(userRepository.findAllAdmins(eq(GROUP_ID))).thenReturn(singletonList(userDTO));

    final List<UserDTO> result = service.findAllAdmins(GROUP_ID);

    assertEquals(result, singletonList(userDTO));
  }

  public void findByUserNameOrMail() {
    service.findByUserNameOrMail(EMAIL);

    verify(userRepository, times(1)).findConfirmedUser(eq(EMAIL));
  }
}
