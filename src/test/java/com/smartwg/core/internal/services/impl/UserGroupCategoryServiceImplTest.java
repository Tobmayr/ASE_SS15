package com.smartwg.core.internal.services.impl;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.UserGroupCategory;
import com.smartwg.core.internal.domain.entities.ids.UserGroupId;
import com.smartwg.core.internal.repositories.CategoryRepository;
import com.smartwg.core.internal.repositories.UserGroupCategoryRepository;
import com.smartwg.core.internal.repositories.UserGroupRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserGroupCategoryService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class UserGroupCategoryServiceImplTest {

  private static final Integer CATEGORY_ID = 3;
  private static final Integer USER_ID = 2;
  private static final Integer GROUP_ID = 4;

  @InjectMocks
  private UserGroupCategoryService service;

  @Mock
  private UserGroupCategoryRepository repository;
  @Mock
  private UserGroupRepository userGroupRepository;
  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private EntityConverter entityFactory;

  @Mock
  private UserGroupCategoryDTO userCategoryDTO;
  @Mock
  private UserGroupCategory userGroupCategory;
  @Mock
  private CategoryDTO categoryDTO;
  @Mock
  private Category category;
  @Mock
  private UserGroup userGroup;
  @Mock
  private Group group;
  @Mock
  private User user;

  @BeforeMethod
  public void setUp() {
    service = new UserGroupCategoryServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createUserGroupCategory() {
    when(entityFactory.createUserGroupCategory(userCategoryDTO)).thenReturn(userGroupCategory);
    when(userCategoryDTO.getCategory()).thenReturn(categoryDTO);
    when(categoryDTO.getId()).thenReturn(CATEGORY_ID);
    when(categoryRepository.findById(eq(CATEGORY_ID))).thenReturn(category);
    when(userGroupCategory.getUserGroup()).thenReturn(userGroup);
    when(userGroup.getGroup()).thenReturn(group);
    when(userGroup.getUser()).thenReturn(user);
    when(user.getId()).thenReturn(USER_ID);
    when(group.getId()).thenReturn(GROUP_ID);
    when(userGroupRepository.findById(new UserGroupId(GROUP_ID, USER_ID))).thenReturn(userGroup);

    service.createUserGroupCategory(userCategoryDTO);

    verify(userGroupCategory, times(1)).setCategory(eq(category));
    verify(userGroupCategory, times(1)).setUserGroup(eq(userGroup));
    verify(repository, times(1)).save(eq(userGroupCategory));
  }

  public void findByCategory() {
    when(repository.findByCategory(eq(CATEGORY_ID))).thenReturn(singletonList(userCategoryDTO));

    final List<UserGroupCategoryDTO> result = service.findByCategory(CATEGORY_ID);

    assertEquals(result, singletonList(userCategoryDTO));
  }
}
