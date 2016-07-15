package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
/**
 * Central access point for service logic which is associated with shopping lists.
 * e.g. persisting, updating and searching shopping lists in the database.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author Kamil Sierzant (ks)
 */
public interface ShoppingListFacade {

  /**
   * Returns a List of all shopping lists (as ShoppingListDTO) for the given user (ID) in a given
   * group(ID);
   * 
   * @param userId id of the user who created the shopping list
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> getUserShoppingLists(Integer userId, Integer GroupId);

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
   * Returns a List of all shopping lists (as ShoppingListDTO) assigned to the given user (ID) in a
   * given group (ID);
   * 
   * @param userId id of the user who is assigned to
   * @return a list with matching shopping lists or an empty in case of no matching shopping lists
   *         were found
   */
  List<ShoppingListDTO> getShoppingListsAssignedToUser(Integer id, Integer GroupId);

  /**
   * Persists a new shopping list or updates an existing shopping list in the database
   * 
   * @param newShoppingList shopping list object wich should be stored
   * @param groupDTO the group to which the shopping list should be saved
   * @throws NullPointerException if the passed parameter is null
   */
  void saveShoppingList(ShoppingListDTO newShoppingList, GroupDTO groupDTO);

  /**
   * Deletes a shopping list with a certain id of the database. If the value is null the database
   * will remain unchanged
   * 
   * @param shoppingListId Id of the wanted shopping list
   */
  void deleteShoppingList(Integer shoppingListId);

  /**
   * Release the shopping list with the specified id.
   * 
   * @param shoppingListId Id of the shopping list
   */
  void releaseShoppingList(Integer shoppingListId);

  /**
   * Cancel the shopping list with the specified id.
   * 
   * @param shoppingListId Id of the shopping list
   */
  void cancelShoppingList(Integer shoppingListId);

  /**
   * Search the items from a shopping list with the specified id.
   * 
   * @param shoppingListId Id of the shopping list
   * @return a list with matching shopping list items or an empty in case of no matching shopping
   *         list items were found
   */

  List<ListItemDTO> getShoppingListsPositions(Integer shoppingListId);

  /**
   * An user with the specified user ID takes the shopping list with the specified id over.
   * 
   * @param selectedShoppingListId Id of the shopping list
   * @param userId Id of the user id, who takes the shopping list over
   */
  void takeOverShoppingList(Integer selectedShoppingListId, Integer userId);

  /**
   * Searches for a certain shopping list in the database using its id.
   * 
   * @param id Id of the wanted shopping list
   * @return found shopping list as ShoppingListDTO, null if the passed id doesnt exist
   */
  ShoppingListDTO getShoppingListsById(Integer id);
}
