package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.entities.ShoppingList;

import java.util.List;

/**
 * This repository provides methods for CRUD-Operations for ShoppingList-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * ShoppingList-objects the methods provided by this interface directly return ShoppingListDTOs to
 * avoid later typecasting or converting.
 *
 * @author Kamil Sierzant (ks)
 */
public interface ShoppingListRepository extends GenericRepository<ShoppingList> {

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) for the given user (ID);
   * 
   * @param userId id of the user who created the shopping list
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> findUserShoppingLists(Integer userId, Integer groupId);

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) for the given group (ID), which have
   * state from the specifieds;
   * 
   * @param groupId id of the group
   * @param listStates a list with the possible ShoppingListStates
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> findGroupShoppingLists(Integer groupId, List<ShoppingListState> listStates);

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) assigned to the given user (ID);
   * 
   * @param userId id of the user who is assigned to
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> findShoppingListsAssignedToUser(Integer userId, Integer groupId);

  /**
   * Searches for a certain shopping list in the database using its id.
   * 
   * @param listId Id of the wanted shopping list
   * @return found shopping list as ShoppingListDTO, null if the passed id doesnt exist
   */
  ShoppingListDTO findShoppingListById(Integer listId);
}
