package com.smartwg.core.internal.repositories;

import java.util.List;

import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.dtos.GroupDTO;

/**
 * This repository provides methods for CRUD-Operations for Group-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * Group-objects the methods provided by this interface directly return GroupDTOs to avoid
 * later typecasting or converting.
 * 
 * @author Kamil Sierzant (ks)
 */
public interface GroupRepository extends GenericRepository<Group> {
  /**
   * Retrieves all Groups.
   * 
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> findGroups();

  /**
   * Return all Groups which are associated with a certain userId (aka. all groups a user with a
   * certain is id member of). In case of no matching groups an empty list is returned;
   * 
   * @param id Id of the user whose groups are wanted
   * @return a list of matching groups ad GroupDTO
   */
  List<GroupDTO> findGroupsByUserID(int id);

  /**
   * Checks whether the passed user (per id) is an admin of the pass group (per id) or not
   * 
   * @param groupId Group which should be checked
   * @param userId User which should be checked
   * @return true if the user is an admin, false if he is a normal user or no member of the group
   */
  boolean isAdmin(int groupId, int userId);

  /**
   * Search groups with given parameter
   * 
   * @param searchparameter Given String to search
   * @return List of Groups as GroupDTOs
   */
  List<GroupDTO> searchGroups(String searchparameter);
}
