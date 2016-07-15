package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.GroupService;
import com.smartwg.core.internal.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class GroupFacadeImpl implements GroupFacade {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupFacadeImpl.class);

  @Inject
  private GroupService groupService;
  @Inject
  private EmailService emailService;
  @Inject
  private UserService userService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#saveGroup(com.smartwg.core.internal.domain.dtos.GroupDTO,
   *      com.smartwg.core.internal.domain.dtos.UserDTO)
   */

  @Override
  public void saveGroup(final GroupDTO groupDTO, final UserDTO userDTO) {
    if (groupDTO == null) {
      return;
    }
    if (groupDTO.getId() == null) {
      if (userDTO == null || userDTO.getId() == null) {
        return;
      }

      groupService.createGroup(groupDTO, userDTO.getId());
    } else {
      groupService.updateGroup(groupDTO);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#getAllGroupsByUserID(int)
   */

  @Override
  public List<GroupDTO> getAllGroupsByUserID(int id) {
    return groupService.getAllGroupsByUserID(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#deleteGroup(com.smartwg.core.internal.domain.dtos.GroupDTO)
   */

  @Override
  public void deleteGroup(final GroupDTO group) {
    final Set<String> groupMembers = userService.findGroupMembersEmailsByGroupId(group.getId());

    groupService.deleteGroup(group.getId());
    emailService.createAndSendGroupDeletedInfoMail(group, groupMembers);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#findById(int)
   */

  @Override
  public GroupDTO findById(int id) {
    return groupService.findById(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#isAdmin(int, int)
   */

  @Override
  public boolean isAdmin(int groupId, int userId) {
    return groupService.isAdmin(groupId, userId);
  }


  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#getAdministratedGroups(java.lang.Integer)
   */

  @Override
  public List<GroupDTO> getAdministratedGroups(Integer userid) {
    return groupService.getAdminstratedGroups(userid);
  }


  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#getGroups()
   */

  @Override
  public List<GroupDTO> getGroups() {
    return groupService.getAllGroups();
  }


  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#searchGroups(java.lang.String)
   */

  @Override
  public List<GroupDTO> searchGroups(String searchparameter) {
    return groupService.searchGroups(searchparameter);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.GroupFacade#joinGroup(com.smartwg.core.internal.domain.dtos.GroupDTO,
   *      com.smartwg.core.internal.domain.dtos.UserDTO)
   */

  @Override
  public void joinGroup(GroupDTO groupDTO, UserDTO user) {
    LOGGER.info("join group invoked");
    Set<String> recipients = new HashSet<String>();
    for (UserDTO admin : userService.findAllAdmins(groupDTO.getId())) {
      recipients.add(admin.getEmail());
    }

    emailService.createAndSendJoinRequestMail(user, recipients);
  }
}
