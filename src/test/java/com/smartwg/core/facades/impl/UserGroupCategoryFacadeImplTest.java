package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.services.UserGroupCategoryService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupCategoryFacadeImplTest {

  @InjectMocks
  private UserGroupCategoryFacade facade;

  @Mock
  private UserGroupCategoryService service;
  @Mock
  private UserGroupCategoryDTO userGroupCategoryDTO;

  @BeforeMethod
  public void setUp() {
    facade = new UserGroupCategoryFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createUserGroupCategory() {
    facade.createUserGroupCategory(userGroupCategoryDTO);

    verify(service, times(1)).createUserGroupCategory(eq(userGroupCategoryDTO));
  }

  public void findByCategory() {
    when(service.findByCategory(2)).thenReturn(singletonList(userGroupCategoryDTO));

    final List<UserGroupCategoryDTO> result = facade.findByCategory(2);

    assertEquals(result, singletonList(userGroupCategoryDTO));
  }
}
