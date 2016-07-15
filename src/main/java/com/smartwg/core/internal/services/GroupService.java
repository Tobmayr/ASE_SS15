package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.GroupDTO;

import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with groups.The main
 * purpose of these methods is to wrap the functionality of the underlying GroupRepostitory and
 * convert the entities to dtos for user in higher-tiered layers
 * 
 * @author Anna Sadriu (as)
 */
public interface GroupService {

  /**
   * Creates a new group entity based on provided information from groupDTO
   *
   * @param groupDTO groupDTO {@link GroupDTO}
   * @param userId
   */
  void createGroup(GroupDTO groupDTO, Integer userId);

  /**
   * Update the specified group
   *
   * @param groupDTO group object which should be updated
   * 
   */
  void updateGroup(GroupDTO groupDTO);

  /**
   * Retrieves all Groups.
   * 
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> getAllGroups();

  /**
   * Deletes removes a group with a certain id of the database. If the value is null the database
   * will remain unchanged
   *
   * @param groupDTO group which should be deleted
   */
  void deleteGroup(Integer groupId);

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
  List<GroupDTO> getAdminstratedGroups(Integer userid);

  /**
   * Search groups with given parameter
   * 
   * @param searchparameter Given String to search
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> searchGroups(String searchparameter);
}
