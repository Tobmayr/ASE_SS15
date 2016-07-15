package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;

import java.util.List;

/**
 * @author Kamil Sierzant (ks) , Oezde Simsek (os)
 */
public interface CategoryService {

  /**
   * Retrieves a list of all Categories
   * 
   * @return a list of Categories
   */
  List<CategoryDTO> getAllCategories();

  /**
   * Persists a new category in the database
   * 
   * @param categoryDTO category object which should be stored
   * @return the id of the created category
   */
  Integer createCategory(CategoryDTO categoryDTO);

  /**
   * Searches for a certain category in the database using its id.
   * 
   * @param id id of the wanted category
   * @return the found category which has the given id
   */
  CategoryDTO findById(int id);

  /**
   * removes a category with a certain id from the database. If the value is null the database will
   * remain unchanged
   * 
   * @param categoryDTO category which should be deleted
   */
  void deleteCategory(CategoryDTO categoryDTO);

  /**
   * Updates the values of an existing category in the database. If the value is null the database
   * stays unchanged
   * 
   * @param categoryDTO the category with existing id but changed attributes
   * @return the id of the changed category
   */
  Integer editCategory(CategoryDTO categoryDTO);

  /**
   * Retrieves a list of Categories which belongs to the group with the given id
   * 
   * @param groupId id of the group
   * @return a list of categories of that certain group
   */
  List<CategoryDTO> findByGroup(Integer groupId);

}
