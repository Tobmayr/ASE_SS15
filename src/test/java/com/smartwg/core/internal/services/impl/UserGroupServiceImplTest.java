package com.smartwg.core.internal.services.impl;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserGroupService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupServiceImplTest {
  private static final Integer USER_ID = 2;
  private static final Integer GROUP_ID = 4;

  @InjectMocks
  private UserGroupService service;
  @Mock
  private UserGroupRepository userGroupRepository;
  @Mock
  private UserGroupDTO userGroupDTO;
  @Mock
  private UserGroup userGroup;
  @Mock
  private Group group;
  @Mock
  private User user;
  @Mock
  private EntityConverter entityFactory;

  @BeforeMethod
  public void setUp() {
    service = new UserGroupServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findUserGroupByUsernameGroupIdWhenNothingFound() {
    when(userGroupRepository.findAll()).thenReturn(singletonList(userGroup));
    when(userGroup.getGroup()).thenReturn(group);
    when(userGroup.getUser()).thenReturn(user);
    when(group.getId()).thenReturn(3);
    when(user.getUserName()).thenReturn("Michi");

    final UserGroup result = service.findUserGroupByUsernameGroupId("Kamil", 2);

    assertNull(result);
  }

  public void findUserGroupByUsernameGroupIdSucceed() {
    when(userGroupRepository.findAll()).thenReturn(singletonList(userGroup));
    when(userGroup.getGroup()).thenReturn(group);
    when(userGroup.getUser()).thenReturn(user);
    when(group.getId()).thenReturn(2);
    when(user.getUserName()).thenReturn("Kamil");

    final UserGroup result = service.findUserGroupByUsernameGroupId("Kamil", 2);

    assertNotNull(result);
    assertEquals(result, userGroup);
  }
}
