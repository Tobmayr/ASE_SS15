package com.smartwg.core.facades.impl;


import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.services.AbsenceService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks), Tobias Ortmayr (to)
 */
@Named
public class UserFacadeImpl implements UserFacade, Serializable {

  @Inject
  private UserService userService;
  @Inject
  private EmailService emailService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#createUser(com.smartwg.core.internal.domain.dtos.UserDTO)
   */

  @Override
  public UserDTO createUser(final UserDTO user) {
    return userService.createUser(user);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#confirmUserRegistration(java.lang.Integer,
   *      java.lang.String)
   */

  @Override
  public boolean confirmUserRegistration(final Integer userId, final String token) {
    return userService.confirmUserRegistration(userId, token);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#getAllUsers()
   */

  public List<UserDTO> getAllUsers() {
    return userService.getAllUsersDTO();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#login(java.lang.String, java.lang.String)
   */

  @Override
  public UserDTO login(final String email, final String password) {
    return userService.login(email, password);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#verifyUnique(java.lang.String, java.lang.String)
   */

  @Override
  public boolean verifyUnique(final String userName, final String email) {
    return userService.verifyUnique(userName, email);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#isNotificationActivated(java.lang.Integer,
   *      com.smartwg.core.internal.domain.NotificationType)
   */

  @Override
  public boolean isNotificationActivated(final Integer userId,
      final NotificationType notificationType) {
    return userService.isNotificationActivated(userId, notificationType);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#sendRegistrationConfirmationMail(com.smartwg.core.internal.domain.dtos.UserDTO,
   *      java.lang.String)
   */

  @Override
  public void sendRegistrationConfirmationMail(final UserDTO user, final String pageLink) {
    emailService.createAndSendRegistrationConfirmationEmail(user, pageLink);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#findUsersByGroupID(java.lang.Integer)
   */

  @Override
  public List<UserDTO> findUsersByGroupID(final Integer groupId) {
    return userService.findUsersByGroupID(groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#findByEmail(java.lang.String)
   */

  @Override
  public UserDTO findByEmail(final String email) {
    return userService.findByEmail(email);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#sendPassworResetMail(com.smartwg.core.internal.domain.dtos.UserDTO,
   *      java.lang.String)
   */

  @Override
  public void sendPassworResetMail(final UserDTO user, final String pageLink) {

    emailService.createAndSendPasswordResetEmail(user, pageLink);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#findById(java.lang.Integer)
   */

  @Override
  public UserDTO findById(final Integer userId) {
    return userService.findById(userId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#updateUser(com.smartwg.core.internal.domain.dtos.UserDTO)
   */

  public void updateUser(final UserDTO user) {
    userService.updateUser(user);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#updateUserGroup(com.smartwg.core.internal.domain.dtos.UserGroupDTO)
   */

  @Override
  public void updateUserGroup(final UserGroupDTO userGroup) {
    userService.updateUserGroup(userGroup);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#removeUserGroup(com.smartwg.core.internal.domain.dtos.UserGroupDTO)
   */

  @Override
  public void removeUserGroup(final UserGroupDTO userGroup) {
    userService.removeUserGroup(userGroup);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#findAllAdmins(java.lang.Integer)
   */

  @Override
  public List<UserDTO> findAllAdmins(final Integer groupID) {
    return userService.findAllAdmins(groupID);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#findByUserNameOrMail(java.lang.String)
   */

  @Override
  public UserDTO findByUserNameOrMail(final String userNameOrEmail) {
    return userService.findByUserNameOrMail(userNameOrEmail);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.UserFacade#sendUserAddedInfoMail(com.smartwg.core.internal.domain.dtos.UserDTO,
   *      com.smartwg.core.internal.domain.dtos.GroupDTO)
   */

  @Override
  public void sendUserAddedInfoMail(final UserDTO user, final GroupDTO group) {
    final Set<String> recipients = userService.findGroupMembersEmailsByGroupId(group.getId());
    emailService.createAndSendUserAddedToGroupInfoMail(user, group, recipients);
  }
}
