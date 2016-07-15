package com.smartwg.core.facades.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.services.CategoryService;


@Test
public class CategoryFacadeImplTest {

  @InjectMocks
  private CategoryFacade facade;
  @Mock
  private CategoryService categoryService;
  @Mock
  private CategoryDTO dto;
  @Mock
  private CategoryDTO dto1;

  @BeforeMethod
  public void beforeMethod() {
    facade = new CategoryFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void saveCategory_newCat() {
    when(categoryService.createCategory(dto)).thenReturn(2);
    when(dto.getId()).thenReturn(null);
    final Integer result = facade.saveCategory(dto);
    verify(categoryService, times(1)).createCategory(dto);
    verify(categoryService, times(0)).editCategory(dto);
    assertEquals(result, new Integer(2));
  }

  public void saveCategory_existingCat() {
    when(categoryService.editCategory(dto)).thenReturn(2);
    when(dto.getId()).thenReturn(2);
    final Integer result = facade.saveCategory(dto);
    verify(categoryService, times(0)).createCategory(dto);
    verify(categoryService, times(1)).editCategory(dto);
    assertEquals(result, new Integer(2));
  }

  public void findById() {
    when(categoryService.findById(24)).thenReturn(dto);
    final CategoryDTO result = facade.findById(24);
    verify(categoryService, times(1)).findById(24);
    assertEquals(result, dto);
  }

  public void deleteCategory() {
    facade.deleteCategory(dto);
    verify(categoryService, times(1)).deleteCategory(dto);
  }

  public void findByGroup() {
    final List<CategoryDTO> expected = Arrays.asList(dto, dto1);
    when(categoryService.findByGroup(121)).thenReturn(expected);
    final List<CategoryDTO> result = facade.findByGroup(121);
    verify(categoryService, times(1)).findByGroup(121);
    assertEquals(result, expected);
  }
}
