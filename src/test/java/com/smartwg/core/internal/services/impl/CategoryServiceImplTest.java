package com.smartwg.core.internal.services.impl;

import static java.util.Arrays.asList;
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
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.repositories.CategoryRepository;
import com.smartwg.core.internal.services.CategoryService;
import com.smartwg.core.internal.services.EntityConverter;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class CategoryServiceImplTest {

  private static final Integer CATEGORY_ID = 1;
  private static final Integer GROUP_ID = 3;

  @InjectMocks
  private CategoryService service;

  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private EntityConverter entityFactory;

  @Mock
  private CategoryDTO categoryDTO;
  @Mock
  private CategoryDTO secondCategoryDTO;
  @Mock
  private Category category;

  @BeforeMethod
  public void setUp() {
    service = new CategoryServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getAllCategories() {
    when(categoryRepository.findAllDTOs()).thenReturn(asList(categoryDTO, secondCategoryDTO));

    final List<CategoryDTO> result = service.getAllCategories();

    assertEquals(result, asList(categoryDTO, secondCategoryDTO));
  }

  public void createCategory() {
    when(entityFactory.createCategory(eq(categoryDTO))).thenReturn(category);
    when(categoryRepository.save(eq(category))).thenReturn(category);
    when(category.getId()).thenReturn(CATEGORY_ID);

    final Integer result = service.createCategory(categoryDTO);

    assertEquals(result, CATEGORY_ID);
  }

  public void findById() {
    when(categoryRepository.findById(eq(CATEGORY_ID))).thenReturn(category);
    when(category.getName()).thenReturn("food");
    when(category.getId()).thenReturn(CATEGORY_ID);

    final CategoryDTO result = service.findById(CATEGORY_ID);

    assertEquals(result.getId(), CATEGORY_ID);
    assertEquals(result.getName(), "food");
  }

  public void deleteCategory() {
    when(categoryDTO.getId()).thenReturn(CATEGORY_ID);
    when(categoryRepository.findById(eq(CATEGORY_ID))).thenReturn(category);

    service.deleteCategory(categoryDTO);

    verify(category, times(1)).setDeleted(eq(Boolean.TRUE));
    verify(categoryRepository, times(1)).merge(category);
  }

  public void editCategory() {
    when(entityFactory.createCategory(categoryDTO)).thenReturn(category);
    when(category.getId()).thenReturn(CATEGORY_ID);

    final Integer result = service.editCategory(categoryDTO);

    verify(categoryRepository, times(1)).merge(eq(category));
    assertEquals(result, CATEGORY_ID);
  }

  public void findByGroup() {
    List<CategoryDTO> categories = asList(categoryDTO, secondCategoryDTO);
    when(categoryRepository.findByGroup(eq(GROUP_ID))).thenReturn(categories);

    final List<CategoryDTO> result = service.findByGroup(GROUP_ID);

    assertEquals(result, categories);
  }


}
