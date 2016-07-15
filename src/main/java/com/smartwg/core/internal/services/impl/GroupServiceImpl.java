package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.GroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class GroupServiceImpl implements GroupService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

  @Inject
  private GroupRepository repository;
  @Inject
  private UserGroupRepository usergrouprepository;
  @Inject
  private UserRepository userRepository;
  @Inject
  private EntityConverter factory;

  @Override
  public void createGroup(final GroupDTO groupDTO, final Integer userId) {
    final Group group = factory.createGroup(groupDTO);
    LOGGER.info(String.format("User %s is creating Group: %s", userId.toString(),
        groupDTO.toString()));

    final User user = userRepository.findById(userId);
    final UserGroup userGroup = new UserGroup();
    userGroup.setRole(Role.ADMIN);
    userGroup.setUser(user);
    userGroup.setGroup(group);
    group.getRoles().add(userGroup);

    repository.save(group);
  }

  @Override
  public void updateGroup(final GroupDTO groupDTO) {
    final Group group = repository.findById(groupDTO.getId());
    group.setName(groupDTO.getName());
    group.setCity(groupDTO.getCity());
    group.setStreet(groupDTO.getStreet());
    group.setStreet2(groupDTO.getStreet2());
    group.setZip(groupDTO.getZip());

    repository.merge(group);
  }

  @Override
  public List<GroupDTO> getAllGroups() {
    return repository.findGroups();
  }

  @Override
  public List<GroupDTO> getAllGroupsByUserID(final int id) {
    return repository.findGroupsByUserID(id);
  }

  @Override
  public void deleteGroup(final Integer groupId) {
    final Group group = repository.findById(groupId);
    if (group != null) {
      LOGGER.info(String.format("Group with the id '%d' has been removed.", groupId));
      repository.delete(group);
    } else {
      LOGGER.error(String.format("Group with the id '%d' could not be found.", groupId));
    }
  }

  @Override
  public GroupDTO findById(int id) {
    return new GroupDTO(repository.findById(id));
  }



  @Override
  public boolean isAdmin(final int groupId, final int userId) {
    return repository.isAdmin(groupId, userId);
  }

  @Override
  public List<GroupDTO> getAdminstratedGroups(final Integer userId) {
    final List<UserGroup> administratedGroups = usergrouprepository.findAdministratedGroups(userId);
    final List<GroupDTO> result = new ArrayList<>();
    for (final UserGroup userGroup : administratedGroups) {
      result.add(new GroupDTO(userGroup.getGroup()));
    }
    return result;
  }

  @Override
  public List<GroupDTO> searchGroups(String searchparameter) {
    return repository.searchGroups(searchparameter);
  }
}
