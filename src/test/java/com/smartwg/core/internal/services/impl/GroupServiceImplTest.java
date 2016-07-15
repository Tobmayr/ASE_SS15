package com.smartwg.core.internal.services.impl;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.entities.Country;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.GroupRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.repositories.UserRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.GroupService;
import com.smartwg.core.internal.services.pipeline.impl.ImageBillDTO;

@Test
public class GroupServiceImplTest {

  private static final Integer USER_ID = 77;
  private static final Integer GROUP_ID = 11;

  @InjectMocks
  private GroupService service;

  @Mock
  private GroupRepository groupRepository;
  @Mock
  private UserGroupRepository userGroupRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private EntityConverter entityFactory;

  @Spy
  private Group group;
  @Mock
  private User user;
  @Mock
  private GroupDTO groupDTO;
  @Mock
  private UserGroup userGroup;
  @Mock
  private Country country;

  @BeforeMethod
  public void setUp() {
    service = new GroupServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createGroupSucceed() {
    when(entityFactory.createGroup(groupDTO)).thenReturn(group);
    when(group.getRoles()).thenReturn(new ArrayList<>());
    when(userRepository.findById(USER_ID)).thenReturn(user);
    when(user.getId()).thenReturn(USER_ID);
    service.createGroup(groupDTO, USER_ID);

    verify(groupRepository, times(1)).save(eq(group));
    assertEquals(group.getRoles().size(), 1);
    assertEquals(group.getRoles().get(0).getUser().getId(), USER_ID);
    assertEquals(group.getRoles().get(0).getRole(), Role.ADMIN);
  }

  public void deleteGroupSucceed() {
    when(groupRepository.findById(eq(GROUP_ID))).thenReturn(group);

    service.deleteGroup(GROUP_ID);

    verify(groupRepository, times(1)).delete(eq(group));
  }

  public void updateGroupSucceed() {
    final Group group = new Group();
    final GroupDTO dto =
        createGroupDTO(GROUP_ID, "groupName", new Country(2, "US", "USA"), "Wien", "1010",
            "Stephanspl.", "");
    dto.setCountry(null);
    when(groupRepository.findById(GROUP_ID)).thenReturn(group);
    ArgumentCaptor<Group> arg = ArgumentCaptor.forClass(Group.class);

    service.updateGroup(dto);

    verify(groupRepository, times(1)).merge(arg.capture());
    assertEquals(group.getName(),dto.getName());
    assertEquals(group.getCity(), dto.getCity());
    assertEquals(group.getStreet(), dto.getStreet());
    assertEquals(group.getZip(), dto.getZip());
    assertEquals(group.getStreet2(), dto.getStreet2());
  }

  public void getAllGroups() {
    when(groupRepository.findGroups()).thenReturn(singletonList(groupDTO));

    final List<GroupDTO> result = service.getAllGroups();

    assertEquals(result, singletonList(groupDTO));
  }

  public void getAllGroupsByUserID() {
    when(groupRepository.findGroupsByUserID(eq(USER_ID))).thenReturn(singletonList(groupDTO));

    final List<GroupDTO> result = service.getAllGroupsByUserID(USER_ID);

    assertEquals(result, singletonList(groupDTO));
  }

  public void findById() {
    when(groupRepository.findById(eq(GROUP_ID))).thenReturn(group);
    when(group.getId()).thenReturn(2);
    when(group.getName()).thenReturn("GroupName");
    when(group.getCountry()).thenReturn(country);
    when(group.getCity()).thenReturn("Wien");
    when(group.getZip()).thenReturn("1120");
    when(group.getStreet()).thenReturn("Meidlingerhaupstrasse");
    when(group.getStreet2()).thenReturn("");

    final GroupDTO result = service.findById(GROUP_ID);

    assertNotNull(result);
    assertEquals(result.getId(), Integer.valueOf(2));
    assertEquals(result.getName(), "GroupName");
    assertEquals(result.getCity(), "Wien");
    assertEquals(result.getZip(), "1120");
    assertEquals(result.getStreet(), "Meidlingerhaupstrasse");
    assertEquals(result.getStreet2(), "");
  }

  public void isAdmin() {
    when(groupRepository.isAdmin(eq(GROUP_ID), eq(USER_ID))).thenReturn(true);

    final boolean result = service.isAdmin(GROUP_ID, USER_ID);

    assertTrue(result);
  }

  public void getAdminstratedGroups() {
    when(userGroupRepository.findAdministratedGroups(eq(USER_ID))).thenReturn(
        singletonList(userGroup));
    when(userGroup.getGroup()).thenReturn(group);
    when(group.getId()).thenReturn(2);
    when(group.getName()).thenReturn("GroupName");
    when(group.getCountry()).thenReturn(country);
    when(group.getCity()).thenReturn("Wien");
    when(group.getZip()).thenReturn("1120");
    when(group.getStreet()).thenReturn("Meidlingerhaupstrasse");
    when(group.getStreet2()).thenReturn("");

    final List<GroupDTO> result = service.getAdminstratedGroups(USER_ID);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getId(), Integer.valueOf(2));
    assertEquals(result.get(0).getName(), "GroupName");
    assertEquals(result.get(0).getCity(), "Wien");
    assertEquals(result.get(0).getZip(), "1120");
    assertEquals(result.get(0).getStreet(), "Meidlingerhaupstrasse");
    assertEquals(result.get(0).getStreet2(), "");
  }

  public void searchGroups() {
    when(groupRepository.searchGroups(eq("Teststrasse 123"))).thenReturn(
        singletonList(groupDTO));
    when(groupDTO.getId()).thenReturn(2);
    when(groupDTO.getName()).thenReturn("GroupName");
    when(groupDTO.getCity()).thenReturn("Wien");
    when(groupDTO.getZip()).thenReturn("1234");
    when(groupDTO.getStreet()).thenReturn("Teststrasse 123");
    when(groupDTO.getStreet2()).thenReturn("");

    final List<GroupDTO> result = service.searchGroups("Teststrasse 123");

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getId(), Integer.valueOf(2));
    assertEquals(result.get(0).getName(), "GroupName");
    assertEquals(result.get(0).getCity(), "Wien");
    assertEquals(result.get(0).getZip(), "1234");
    assertEquals(result.get(0).getStreet(), "Teststrasse 123");
    assertEquals(result.get(0).getStreet2(), "");
  }

  private GroupDTO createGroupDTO(final Integer id, final String name, final Country country,
      final String city, final String zip, final String street, final String street2) {
    final GroupDTO groupDTO = new GroupDTO();
    groupDTO.setId(id);
    groupDTO.setName(name);
    groupDTO.setCountry(new CountryDTO(country));
    groupDTO.setCity(city);
    groupDTO.setZip(zip);
    groupDTO.setStreet(street);
    groupDTO.setStreet2(street2);
    return groupDTO;
  }
}
