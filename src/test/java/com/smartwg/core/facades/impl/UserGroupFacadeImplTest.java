package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.UserGroupFacade;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.services.UserGroupService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupFacadeImplTest {

  @InjectMocks
  private UserGroupFacade facade;

  @Mock
  private UserGroupService userGroupService;
  @Mock
  private UserGroup userGroup;
  @Mock
  private UserGroupDTO userGroupDTO;

  @BeforeMethod
  public void setUp() {
    facade = new UserGroupFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findUserGroupByUsernameGroupId() {
    when(userGroupService.findUserGroupByUsernameGroupId("kamil", 2)).thenReturn(userGroup);

    final UserGroup result = facade.findUserGroupByUsernameGroupId("kamil", 2);

    assertEquals(result, userGroup);
  }

  public void createUserGroup() {
    facade.createUserGroup(userGroupDTO);

    verify(userGroupService, times(1)).createUserGroup(eq(userGroupDTO));
  }
}
