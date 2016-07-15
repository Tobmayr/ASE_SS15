package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Oezde Simsek (os)
 */
@Transactional
public interface CategoryFacade {


  /**
   * Creates a new category
   * 
   * @param categoryDTO category object which should be stored
   * @return the id of the created category
   */

  Integer saveCategory(CategoryDTO categoryDTO);

  /**
   * Searches for a certain category using its id.
   * 
   * @param id id of the wanted category
   * @return the found category which has the given id
   */
  CategoryDTO findById(int id);

  /**
   * deletes a category with a certain id.
   * 
   * @param categoryDTO category which should be deleted
   */
  void deleteCategory(CategoryDTO categoryDTO);

  /**
   * returns a list of Categories which belongs to the group with the given id
   * 
   * @param groupId id of the group
   * @return a list of categories of that certain group
   */
  List<CategoryDTO> findByGroup(Integer groupId);

}
