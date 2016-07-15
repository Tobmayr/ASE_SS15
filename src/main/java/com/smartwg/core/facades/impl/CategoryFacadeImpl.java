package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.services.CategoryService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks)
 */
@Named
public class CategoryFacadeImpl implements CategoryFacade {

  @Inject
  private CategoryService categoryService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CategoryFacade#saveCategory(com.smartwg.core.internal.domain.dtos.CategoryDTO)
   */

  @Override
  public Integer saveCategory(CategoryDTO categoryDTO) {
    if (categoryDTO.getId() == null) {
      return categoryService.createCategory(categoryDTO);
    } else {
      return categoryService.editCategory(categoryDTO);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CategoryFacade#findById(int)
   */

  @Override
  public CategoryDTO findById(int id) {
    return categoryService.findById(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CategoryFacade#deleteCategory(com.smartwg.core.internal.domain.dtos.CategoryDTO)
   */

  @Override
  public void deleteCategory(CategoryDTO categoryDTO) {
    categoryService.deleteCategory(categoryDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CategoryFacade#findByGroup(java.lang.Integer)
   */

  @Override
  public List<CategoryDTO> findByGroup(Integer groupId) {
    return categoryService.findByGroup(groupId);
  }


}
