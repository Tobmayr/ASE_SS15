package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.services.ShoppingListService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks)
 */
@Named
public class ShoppingListFacadeImpl implements ShoppingListFacade {

  @Inject
  private ShoppingListService shoppingListService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#getUserShoppingLists(java.lang.Integer)
   */

  @Override
  public List<ShoppingListDTO> getUserShoppingLists(final Integer userId, final Integer groupId) {
    return shoppingListService.getUserShoppingLists(userId, groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#getGroupShoppingLists(java.lang.Integer,
   *      java.util.List)
   */

  @Override
  public List<ShoppingListDTO> getGroupShoppingLists(final Integer groupId,
      final List<ShoppingListState> listStates) {
    return shoppingListService.getGroupShoppingLists(groupId, listStates);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#getShoppingListsAssignedToUser(java.lang.Integer)
   */

  @Override
  public List<ShoppingListDTO> getShoppingListsAssignedToUser(Integer userId, Integer groupId) {
    return shoppingListService.getShoppingListsAssignedToUser(userId,groupId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#saveShoppingList(com.smartwg.core.internal.domain.dtos.ShoppingListDTO,
   *      com.smartwg.core.internal.domain.dtos.GroupDTO)
   */

  @Override
  public void saveShoppingList(final ShoppingListDTO shoppingList, final GroupDTO groupDTO) {
    if (shoppingList.getId() == null) {
      shoppingListService.saveShoppingList(shoppingList, groupDTO);
    } else {
      shoppingListService.updateShoppingList(shoppingList);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#deleteShoppingList(java.lang.Integer)
   */

  @Override
  public void deleteShoppingList(final Integer shoppingListId) {
    shoppingListService.deleteShoppingList(shoppingListId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#releaseShoppingList(java.lang.Integer)
   */

  @Override
  public void releaseShoppingList(final Integer shoppingListId) {
    shoppingListService.changeShoppingListState(shoppingListId, ShoppingListState.RELEASED);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#cancelShoppingList(java.lang.Integer)
   */

  @Override
  public void cancelShoppingList(final Integer shoppingListId) {
    shoppingListService.changeShoppingListState(shoppingListId, ShoppingListState.CANCELLED);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#getShoppingListsPositions(java.lang.Integer)
   */

  @Override
  public List<ListItemDTO> getShoppingListsPositions(final Integer shoppingListId) {
    return shoppingListService.getShoppingListPositions(shoppingListId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#takeOverShoppingList(java.lang.Integer,
   *      java.lang.Integer)
   */

  @Override
  public void takeOverShoppingList(final Integer shoppingListId, final Integer userId) {
    shoppingListService.takeOverShoppingListState(shoppingListId, userId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShoppingListFacade#getShoppingListsById(java.lang.Integer)
   */

  @Override
  public ShoppingListDTO getShoppingListsById(final Integer listId) {
    return shoppingListService.getShoppingListsById(listId);
  }
}
