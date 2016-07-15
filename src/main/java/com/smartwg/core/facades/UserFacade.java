package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
/**
 * Central access point for service logic which is associated with users.
 * e.g. persisting, updating and searching users in the database.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author Kamil Sierzant (ks)
 */
public interface UserFacade {

  /**
   * Persists a new user in the database
   * 
   * @param user user object (UserDTO) wich should be stored
   * @throws NullPointerException if the passed parameter is null
   */
  UserDTO createUser(UserDTO user);

  /**
   * Confirm the registration of an user with the specified ID and token
   * 
   * @param userId the ID of the user wich should be confirmed
   * @param token confirmation token
   * @return TRUE in case of successful confirmation, otherwise FALSE
   */
  boolean confirmUserRegistration(Integer userId, String token);

  /**
   * Returns a List of all users
   * 
   * @return a list with all the users from the data base
   */
  List<UserDTO> getAllUsers();

  /**
   * Returns a List of all users (as UserDTO) for the given group (ID)
   * 
   * @param groupID id of the group
   * @return a list with matching users or an empty in case of no matching users were found
   */
  List<UserDTO> findUsersByGroupID(Integer groupID);

  /**
   * Returns a loged in user (as UserDTO) after login with email and password
   * 
   * @param email email of the user to login
   * @param password password of the user to login
   * @return the user (as UserDTO) who have been loged in
   */
  UserDTO login(String email, String password);

  /**
   * Check whether the given username and e-mail address are uniyue
   * 
   * @param userName userName of the user to login
   * @param email email of the user to login
   * @return TRUE in case the data is unique, otherwise FALSE
   */
  boolean verifyUnique(String userName, String email);

  /**
   * Search for a user with the specfied e-mail address
   * 
   * @param email email of the user to search in the data base
   * @return the user (as UserDTO) in case the given e-mail was found
   */
  UserDTO findByEmail(String email);

  /**
   * Send an e-mail to the specified user in order to reset its password
   * 
   * @param user the user (as UserDTO) for which the password should be reseted
   * @param pageLink an URL for the password reset
   */
  void sendPassworResetMail(UserDTO user, String pageLink);

  /**
   * Search for a user with the specfied ID
   * 
   * @param userId ID of the user to search in the data base
   * @return the user (as UserDTO) in case the given ID was found
   */
  UserDTO findById(Integer userId);

  /**
   * Check whether the specified type of notifications are activated for the specified user
   * 
   * @param userId ID of the user
   * @param notificationType type of the notification
   * @return TRUE in case the notifications are activated, otherwise FALSE
   */
  boolean isNotificationActivated(Integer userId, NotificationType notificationType);

  /**
   * Send an e-mail to the specified user in order to confirm the registration
   * 
   * @param user the user (as UserDTO) who was registered
   * @param pageLink an URL for the confirmation
   */
  void sendRegistrationConfirmationMail(UserDTO user, String pageLink);

  /**
   * Removes an user group. If the value is null the database will remain unchanged
   * 
   * @param usergroup usergroup object (as UserGroupDTO) to be removed
   */
  void removeUserGroup(UserGroupDTO usergroup);

  /**
   * Updates an user group. If the value is null the database will remain unchanged
   * 
   * @param usergroup usergroup object (as UserGroupDTO) to be updated
   */
  void updateUserGroup(UserGroupDTO usergroup);

  /**
   * Updates an user. If the value is null the database will remain unchanged
   * 
   * @param edituser user object (as UserDTO) to be updated
   */
  void updateUser(UserDTO edituser);

  /**
   * Returns a List of all ADMIN users for the specified group
   * 
   * @param groupID ID of the group to search in
   * @return a list with all the ADMIN users for the group from the data base
   */
  List<UserDTO> findAllAdmins(Integer groupID);

  /**
   * ??? Search for a user with the specfied e-mail address or username
   * 
   * @param unameMail ??? username or email of the user to search in the data base
   * @return the user (as UserDTO) in case the given e-mail was found
   */
  UserDTO findByUserNameOrMail(String unameMail);

  /**
   * Send an e-mail to the ADMINs of the specified group to inform them, that the specified user was
   * added to the group
   * 
   * @param user the user (as UserDTO) who was added
   * @param group the group (as GroupDTO) where the user was added
   */
  void sendUserAddedInfoMail(UserDTO user, GroupDTO group);
}
