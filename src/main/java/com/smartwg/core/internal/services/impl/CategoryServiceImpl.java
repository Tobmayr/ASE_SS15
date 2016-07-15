package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.repositories.CategoryRepository;
import com.smartwg.core.internal.services.CategoryService;
import com.smartwg.core.internal.services.EntityConverter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks) , Oezde Simsek (os)
 */
@Named
public class CategoryServiceImpl implements CategoryService {

  @Inject
  private CategoryRepository categoryRepository;
  @Inject
  private EntityConverter entityFactory;

  @Override
  public List<CategoryDTO> getAllCategories() {
    return categoryRepository.findAllDTOs();
  }

  @Override
  public Integer createCategory(final CategoryDTO categoryDTO) {
    final Category category = categoryRepository.save(entityFactory.createCategory(categoryDTO));
    return category.getId();
  }

  @Override
  public CategoryDTO findById(final int id) {
    return new CategoryDTO(categoryRepository.findById(id));
  }

  @Override
  public void deleteCategory(final CategoryDTO categoryDTO) {
    final Category cat = categoryRepository.findById(categoryDTO.getId());
    cat.setDeleted(true);
    categoryRepository.merge(cat);
  }

  @Override
  public Integer editCategory(final CategoryDTO categoryDTO) {
    Category category = entityFactory.createCategory(categoryDTO);
    categoryRepository.merge(category);
    return category.getId();
  }

  @Override
  public List<CategoryDTO> findByGroup(final Integer groupId) {
    return categoryRepository.findByGroup(groupId);
  }



}
