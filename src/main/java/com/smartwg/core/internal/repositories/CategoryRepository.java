package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.entities.Category;

import java.util.List;

/**
 * @author Kamil Sierzant (ks) , Oezde Simsek (os)
 */
public interface CategoryRepository extends GenericRepository<Category> {

  /**
   * Retrieves a list of all Categories
   * 
   * @return a list of Categories
   */
  List<CategoryDTO> findAllDTOs();

  /**
   * Retrieves a list of Categories which belongs to the group with the given id
   * 
   * @param groupId id of the group
   * @return a list of categories of that certain group
   */
  List<CategoryDTO> findByGroup(Integer groupId);
}
