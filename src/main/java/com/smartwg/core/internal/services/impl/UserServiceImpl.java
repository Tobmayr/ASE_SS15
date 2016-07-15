package com.smartwg.core.internal.services.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.NotificationDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.PasswordEncryptionService;
import com.smartwg.core.internal.services.UserService;

/**
 * @author Kamil Sierzant (ks), Oezde Simsek (os)
 */
@Named
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Inject
  private UserRepository repository;
  @Inject
  private GroupRepository grouprepository;
  @Inject
  private UserGroupRepository usergrouprepository;
  @Inject
  private PasswordEncryptionService passwordEncryptionService;
  @Inject
  private EntityConverter factory;

  @Override
  public UserDTO createUser(final UserDTO userDTO) {
    byte[] salt = createSalt();
    byte[] encryptedPassword = createPassword(userDTO.getPassword_clear(), salt);

    LOGGER.info("Password encrypted null?= " + encryptedPassword);
    userDTO.setPassword(encryptedPassword);
    userDTO.setSalt(salt);

    final User user = factory.createUser(userDTO);

    LOGGER.info(String.format("Creating user: %s", user.toString()));

    return new UserDTO(repository.save(user));
  }

  private byte[] createPassword(final String password, final byte[] salt) {
    byte[] encryptedPassword = null;
    try {
      encryptedPassword = passwordEncryptionService.getEncryptedPassword(password, salt);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
      e1.printStackTrace();
    }
    return encryptedPassword;
  }

  private byte[] createSalt() {
    byte[] salt = null;
    try {
      salt = passwordEncryptionService.generateSalt();

    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    }
    return salt;
  }

  @Override
  public boolean confirmUserRegistration(final Integer userId, final String token) {
    final User user = repository.findById(userId);

    if (user == null || user.isConfirmed() || !user.getConfirmCode().equals(token)) {
      return false;
    }

    user.setConfirmed(true);
    repository.merge(user);
    LOGGER.info(String.format("Updating user: %s", user.toString()));
    return true;
  }

  @Override
  public List<User> getAllUsers() {
    return repository.findAll();
  }

  @Override
  public List<UserDTO> getAllUsersDTO() {
    return repository.findAllDTOs();
  }

  @Override
  public UserDTO login(final String email, final String password) {
    final User user = repository.findConfirmedUser(email);
    if (user != null) {
      try {
        final boolean authenticateSucceed =
            passwordEncryptionService.authenticate(password, user.getPassword(), user.getSalt());
        if (authenticateSucceed) {
          return new UserDTO(user);
        } else {
          LOGGER.info("Login FAILED");
          return null;
        }
      } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public boolean verifyUnique(final String username, final String email) {
    return repository.findUsers(username, email).isEmpty();
  }

  @Override
  public User findUserById(final Integer id) {
    return repository.findById(id);
  }

  @Override
  public List<UserDTO> findUsersByGroupID(final Integer groupID) {
    return repository.findUsersByGroupID(groupID);
  }

  @Override
  public UserDTO findByEmail(final String email) {
    return repository.findByMail(email);
  }

  @Override
  public UserDTO findById(final Integer userId) {
    User user;
    if ((user = repository.findById(userId)) != null) {
      return new UserDTO(user);
    }
    return null;
  }

  public boolean isNotificationActivated(final Integer userId,
      final NotificationType notificationType) {
    return repository.findActivatedNotifications(userId).contains(notificationType);
  }

  @Override
  public List<String> findUsersEmailsByGroupId(final Integer groupId,
      final NotificationType notificationType) {
    final List<NotificationDTO> usersEmailsByGroupId = repository.findUsersEmailsByGroupId(groupId);
    final List<String> result = new ArrayList<>();

    for (final NotificationDTO notificationDTO : usersEmailsByGroupId) {
      if (notificationDTO.getActiveNotificationTypes().contains(notificationType.name())) {
        result.add(notificationDTO.getEmail());
      }
    }

    return result;
  }

  @Override
  public Set<String> findGroupMembersEmailsByGroupId(final Integer groupId) {
    final List<String> groupMembersEmails = repository.findGroupMembersEmails(groupId);
    final Set<String> result = new HashSet<>();
    result.addAll(groupMembersEmails);
    return result;
  }

  public void updateUser(final UserDTO userDTO) {
    if (StringUtils.isNotBlank(userDTO.getPassword_clear())) {
      byte[] salt = createSalt();
      byte[] encryptedPassword = createPassword(userDTO.getPassword_clear(), salt);
      userDTO.setPassword(encryptedPassword);
      userDTO.setSalt(salt);
    }
    User user = factory.createUser(userDTO);
    repository.merge(user);
  }

  @Override
  public void updateUserGroup(final UserGroupDTO usergroupDTO) {
    UserGroup usergroup = factory.createUserGroup(usergroupDTO);
    usergroup.setUser(repository.findById(usergroup.getUser().getId()));
    usergroup.setGroup(grouprepository.findById(usergroup.getGroup().getId()));
    usergroup.setJoinDate(usergroupDTO.getJoinDate());
    usergroup.setLeaveDate(usergroupDTO.getLeaveDate());
    usergroup.setDeleted(usergroupDTO.isDeleted());
    usergrouprepository.merge(usergroup);
  }

  @Override
  public void removeUserGroup(final UserGroupDTO usergroupDTO) {
    UserGroup usergroup = factory.createUserGroup(usergroupDTO);
    usergroup.setUser(repository.findById(usergroup.getUser().getId()));
    usergroup.setGroup(grouprepository.findById(usergroup.getGroup().getId()));
    usergroup.setDeleted(true);
    usergrouprepository.merge(usergroup);
  }

  @Override
  public List<UserDTO> findAllAdmins(final Integer groupId) {
    return repository.findAllAdmins(groupId);

  }

  @Override
  public UserDTO findByUserNameOrMail(final String userNameEmail) {
    final User user = repository.findConfirmedUser(userNameEmail);
    return user == null ? null : new UserDTO(user);
  }
}
