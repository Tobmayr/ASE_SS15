package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;

import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with shopping list. e.g.
 * persisting and updating shopping list in the database etc. The main purpose of these methods is
 * to wrap the functionality of the underlying ShoppingListRepository and convert the entities to
 * dtos for use in higher-tiered layers
 * 
 * @author Kamil Sierzant (ks)
 */
public interface ShoppingListService {

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) for the given user (ID);
   * 
   * @param userId id of the user who created the shopping list
   * @param groupId 
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> getUserShoppingLists(Integer userId, Integer groupId);

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) for the given group (ID), which have
   * state from the specifieds;
   * 
   * @param groupId id of the group
   * @param listStates a list with the possible ShoppingListStates
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> getGroupShoppingLists(Integer groupId, List<ShoppingListState> listStates);

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) assigned to the given user (ID);
   * 
   * @param userId id of the user who is assigned to
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> getShoppingListsAssignedToUser(Integer userId,Integer groupId);

  /**
   * Persists a new shopping list in the database
   * 
   * @param newShoppingList shopping list object wich should be stored
   * @param groupDTO the group to which the shopping list should be saved
   * @throws NullPointerException if the passed parameter is null
   */
  void saveShoppingList(ShoppingListDTO shoppingListDTO, GroupDTO groupDTO);

  /**
   * Updates an existing shopping list in the database
   * 
   * @param newShoppingList shopping list object wich should be stored
   * @param groupDTO the group to which the shopping list should be saved
   * @throws NullPointerException if the passed parameter is null
   */
  void updateShoppingList(ShoppingListDTO shoppingListDTO);

  /**
   * Deletes a shopping list with a certain id of the database. If the value is null the database
   * will remain unchanged
   * 
   * @param shoppingListId Id of the wanted shopping list
   */
  void deleteShoppingList(Integer shoppingListId);

  /**
   * Updates the state of the given shopping list
   * 
   * @param shoppingListId shopping list which should be changed
   * @param state the new state of the shopping state
   * @throws NullPointerException if the passed parameter is null
   */
  void changeShoppingListState(Integer shoppingListId, ShoppingListState state);

  /**
   * Search the items from a shopping list with the specified id.
   * 
   * @param shoppingListId Id of the shopping list
   * @return a list with matching shopping list items or an empty in case of no matching shopping
   *         list items were found
   */
  List<ListItemDTO> getShoppingListPositions(Integer shoppingListId);

  /**
   * An user with the specified user ID takes the shopping list with the specified id over.
   * 
   * @param selectedShoppingListId Id of the shopping list
   * @param userId Id of the user id, who takes the shopping list over
   */
  void takeOverShoppingListState(Integer shoppingListId, Integer userId);

  /**
   * Searches for a certain shopping list in the database using its id.
   * 
   * @param id Id of the wanted shopping list
   * @return found shopping list as ShoppingListDTO, null if the passed id doesnt exist
   */
  ShoppingListDTO getShoppingListsById(Integer listId);
}
