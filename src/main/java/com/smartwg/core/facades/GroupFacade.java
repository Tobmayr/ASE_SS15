package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Central access point for service logic which is associated with groups. e.g. persisting and
 * updating groups in the database, getAllGroupsByUserId etc. In case of implementing a REST-API in
 * the future its methods will be bounded on the facade-interfaces
 * 
 * @author Anna Sadriu(as), Tobias Ortmayr (to)
 *
 */
@Transactional
public interface GroupFacade {
  /**
   * Persists a new group or updates an existing group in the database
   *
   * @param groupDTO group object which should be stored
   * 
   */
  void saveGroup(GroupDTO groupDTO, UserDTO userDTO);

  /**
   * Deletes removes a group with a certain id of the database. If the value is null the database
   * will remain unchanged
   *
   * @param groupDTO group which should be deleted
   */
  void deleteGroup(GroupDTO groupDTO);

  /**
   * Searches for a certain group in the database using its id.
   * 
   * @param id Id of the wanted group
   * @return found Group as GroupDTO, null if the passed id doesn't exist
   */
  GroupDTO findById(int id);

  /**
   * Return all Groups which are associated with a certain userId (aka. all groups a user with a
   * certain is id member of). In case of no matching groups an empty list is returned;
   * 
   * @param id Id of the user whose groups are wanted
   * @return a list of matching groups ad GroupDTO
   */
  List<GroupDTO> getAllGroupsByUserID(int id);


  /**
   * Checks whether the passed user (per id) is an admin of the pass group (per id) or not
   * 
   * @param groupId Group which should be checked
   * @param userId User which should be checked
   * @return true if the user is an admin, false if he is a normal user or no member of the group
   */
  boolean isAdmin(int groupId, int userId);

  /**
   * Retrieves all Groups of which the passed user (by id) is an administrator.
   * 
   * @param userid Id of the user
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> getAdministratedGroups(Integer userid);

  /**
   * Retrieves all Groups.
   * 
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> getGroups();

  /**
   * Search groups with given parameter
   * 
   * @param searchparameter Given String to search
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> searchGroups(String searchparameter);

  /**
   * Given user wants to join group
   * 
   * @param groupDTO Given group
   * @param userDTO Given user
   */
  void joinGroup(GroupDTO groupDTO, UserDTO userDTO);
}
