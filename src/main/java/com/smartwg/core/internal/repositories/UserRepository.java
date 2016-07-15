package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.NotificationDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.entities.User;

import java.util.List;

/**
 * This repository provides methods for CRUD-Operations for User-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * User-objects the methods provided by this interface directly return UserDTOs to avoid later
 * typecasting or converting.
 *
 * @author Kamil Sierzant (ks)
 */
public interface UserRepository extends GenericRepository<User> {
  User findConfirmedUser(String name);

  List<User> findUsers(String name, String email);

  List<UserDTO> findAllDTOs();

  List<UserDTO> findUsersByGroupID(Integer groupID);

  UserDTO findByMail(String email);

  List<NotificationType> findActivatedNotifications(Integer userId);

  List<NotificationDTO> findUsersEmailsByGroupId(Integer groupId);

  List<String> findGroupMembersEmails(Integer groupId);

  List<UserDTO> findAllAdmins(Integer groupID);

}
