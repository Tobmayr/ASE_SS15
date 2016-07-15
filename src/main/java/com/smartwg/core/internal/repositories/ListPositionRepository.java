package com.smartwg.core.internal.repositories;

import java.util.List;

import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.entities.ListPosition;

/**
 * This repository provides methods for CRUD-Operations for ListItem-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * ListItem-objects the methods provided by this interface directly return ListItemDTOs to avoid
 * later typecasting or converting.
 * 
 * @author Matthias Höllthaler (mh), Tobias Ortmayr (to), Oezde Simsek (os)
 */

public interface ListPositionRepository extends GenericRepository<ListPosition> {
  /**
   * Search the items from a shopping list with the specified id.
   * 
   * @param shoppingListId Id of the shopping list
   * @return a list with matching shopping list items or an empty in case of no matching shopping
   *         list items were found
   */
  List<ListItemDTO> findShoppingListPositions(Integer shoppingListId);
}
