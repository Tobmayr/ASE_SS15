package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.GroupService;
import com.smartwg.core.internal.services.UserService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class GroupFacadeImplTest {

  @InjectMocks
  private GroupFacade facade;
  @Mock
  private GroupService groupService;
  @Mock
  private GroupDTO groupDTO;
  @Mock
  private EmailService emailService;
  @Mock
  private UserService userService;

  @BeforeMethod
  public void beforeMethod() {
    facade = new GroupFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void saveGroup_NewGroup() {
    final GroupDTO groupDTO = new GroupDTO();
    final UserDTO userDTO = new UserDTO();
    userDTO.setId(2);
    facade.saveGroup(groupDTO, userDTO);
    verify(groupService, times(1)).createGroup(groupDTO, userDTO.getId());
    verify(groupService, times(0)).updateGroup(groupDTO);
  }

  public void saveGroup_ExistingGroup() {
    final GroupDTO groupDTO = new GroupDTO();
    groupDTO.setId(2);
    final UserDTO userDTO = new UserDTO();
    userDTO.setId(2);
    facade.saveGroup(groupDTO, userDTO);
    verify(groupService, times(0)).createGroup(groupDTO, userDTO.getId());
    verify(groupService, times(1)).updateGroup(groupDTO);
  }

  public void saveGroup_Null() {
    facade.saveGroup(null, null);
    verify(groupService, times(0)).createGroup(null, null);
    verify(groupService, times(0)).updateGroup(null);
  }

  public void saveGroup_NewGroup_NullUser() {
    final GroupDTO groupDTO = new GroupDTO();
    facade.saveGroup(groupDTO, null);
    verify(groupService, times(0)).createGroup(groupDTO, null);
    verify(groupService, times(0)).updateGroup(groupDTO);
  }
  
  public void saveGroup_NewGroup_NullUserId() {
    final GroupDTO groupDTO = new GroupDTO();
    final UserDTO userDTO = new UserDTO();
    facade.saveGroup(groupDTO, userDTO);
    verify(groupService, times(0)).createGroup(groupDTO, null);
    verify(groupService, times(0)).updateGroup(groupDTO);
  }

  public void getAllGroupsByUserID() {
    final List<GroupDTO> expected = Arrays.asList(groupDTO);
    when(groupService.getAllGroupsByUserID(4)).thenReturn(expected);
    final List<GroupDTO> result = facade.getAllGroupsByUserID(4);
    verify(groupService, times(1)).getAllGroupsByUserID(4);
    assertEquals(result, expected);
  }

  public void deleteGroup() {
    final Set<String> groupMembers = new HashSet<>();

    when(groupDTO.getId()).thenReturn(5);
    when(userService.findGroupMembersEmailsByGroupId(5)).thenReturn(groupMembers);

    facade.deleteGroup(groupDTO);

    verify(groupService, times(1)).deleteGroup(5);
    verify(emailService, times(1)).createAndSendGroupDeletedInfoMail(groupDTO, groupMembers);
  }

  public void findById() {
    when(groupService.findById(12)).thenReturn(groupDTO);
    final GroupDTO result = facade.findById(12);
    verify(groupService, times(1)).findById(12);
    assertEquals(result, groupDTO);
  }

  public void isAdmin() {
    when(groupService.isAdmin(25, 16)).thenReturn(true);
    final boolean result = facade.isAdmin(25, 16);
    verify(groupService, times(1)).isAdmin(25, 16);
    assertEquals(result, true);
  }


  public void findAdministratedGroups() {
    final List<GroupDTO> expected = Arrays.asList(groupDTO);
    when(groupService.getAdminstratedGroups(23)).thenReturn(expected);
    final List<GroupDTO> result = facade.getAdministratedGroups(23);
    verify(groupService, times(1)).getAdminstratedGroups(23);
    assertEquals(result, expected);
  }

  public void getAllGroups() {
    final List<GroupDTO> expected = Arrays.asList(groupDTO);
    when(groupService.getAllGroups()).thenReturn(expected);
    final List<GroupDTO> result = facade.getGroups();
    verify(groupService, times(1)).getAllGroups();
    assertEquals(result, expected);
  }

  public void searchGroups() {
    final List<GroupDTO> expected = Arrays.asList(groupDTO);
    when(groupService.searchGroups("Test Street 123")).thenReturn(expected);
    final List<GroupDTO> result = facade.searchGroups("Test Street 123");
    verify(groupService, times(1)).searchGroups("Test Street 123");
    assertEquals(result, expected);
  }
  
  public void join_GroupWithoutAdmins() {
    final GroupDTO groupDTO = new GroupDTO();
    groupDTO.setId(2);
    final UserDTO userDTO = new UserDTO();
    userDTO.setId(2);
    
    final Set<String> groupMembers = new HashSet<>();

    when(userService.findGroupMembersEmailsByGroupId(2)).thenReturn(groupMembers);

    facade.joinGroup(groupDTO, userDTO);
    verify(userService, times(1)).findAllAdmins(2);
    verify(emailService, times(1)).createAndSendJoinRequestMail(userDTO, groupMembers);
  }
}
