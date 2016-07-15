package com.smartwg.core.internal.services;


import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.User;

import java.util.List;
import java.util.Set;

/**
 * This Interface provides the basic service methods which are associated with Users. e.g.
 * persisting and updating Users in the database etc. The main purpose of these methods is to wrap
 * the functionality of the underlying UserRepository and convert the entities to dtos for use in
 * higher-tiered layers
 * 
 * @author Kamil Sierzant (ks)
 */
public interface UserService {

  /**
   * Create new user entity based on provided information from userDTO
   *
   * @param userDTO {@link UserDTO}
   */
  UserDTO createUser(UserDTO userDTO);

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
  List<User> getAllUsers();

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
  boolean verifyUnique(String username, String email);

  /**
   * Search for a user with the specfied ID
   * 
   * @param userId ID of the user to search in the data base
   * @return the user in case the given ID was found
   */
  User findUserById(Integer id);

  /**
   * Returns a List of all users
   * 
   * @return a list with all the users (as UserDTOs) from the data base
   */
  List<UserDTO> getAllUsersDTO();

  /**
   * Returns a List of all users (as UserDTO) for the given group (ID)
   * 
   * @param groupID id of the group
   * @return a list with matching users or an empty in case of no matching users were found
   */
  List<UserDTO> findUsersByGroupID(Integer groupID);

  /**
   * Search for a user with the specfied e-mail address
   * 
   * @param email email of the user to search in the data base
   * @return the user (as UserDTO) in case the given e-mail was found
   */
  UserDTO findByEmail(String email);

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
   * Search for a user with the specfied e-mail address in a group
   * 
   * @param groupId ID of the group
   * @param notificationType type of the notification
   * @return List of e-mails of the found users
   */
  List<String> findUsersEmailsByGroupId(Integer groupId, NotificationType notificationType);

  /**
   * Search for a user with the specfied e-mail address in a group
   * 
   * @param groupId ID of the group
   * @return Set of e-mails of the found users
   */
  Set<String> findGroupMembersEmailsByGroupId(Integer groupId);

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
  void updateUser(UserDTO user);

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
}
