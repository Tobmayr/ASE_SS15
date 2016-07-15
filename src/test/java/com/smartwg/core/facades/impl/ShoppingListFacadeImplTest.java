package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.services.ShoppingListService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class ShoppingListFacadeImplTest {

  @InjectMocks
  private ShoppingListFacade facade;

  @Mock
  private ShoppingListService shoppingListService;

  @Mock
  private ShoppingListDTO shoppingListDTO;
  @Mock
  private ListItemDTO shoppingListPositionDTO;
  @Mock
  private GroupDTO groupDTO;

  @BeforeMethod
  public void setUp() {
    facade = new ShoppingListFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getUserShoppingLists() {
    when(shoppingListService.getUserShoppingLists(23,2)).thenReturn(singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = facade.getUserShoppingLists(23,2);

    assertEquals(result, singletonList(shoppingListDTO));
  }

  public void saveShoppingListForNewEntity() {
    when(shoppingListDTO.getId()).thenReturn(null);

    facade.saveShoppingList(shoppingListDTO, groupDTO);

    verify(shoppingListService, times(1)).saveShoppingList(eq(shoppingListDTO), eq(groupDTO));
  }

  public void saveShoppingListForExistingEntity() {
    when(shoppingListDTO.getId()).thenReturn(23);

    facade.saveShoppingList(shoppingListDTO, null);

    verify(shoppingListService, times(1)).updateShoppingList(eq(shoppingListDTO));
  }

  public void deleteShoppingList() {
    facade.deleteShoppingList(44);

    verify(shoppingListService, times(1)).deleteShoppingList(eq(44));
  }

  public void releaseShoppingListSucceed() {
    facade.releaseShoppingList(23);

    verify(shoppingListService, times(1)).changeShoppingListState(eq(23),
        eq(ShoppingListState.RELEASED));
  }

  public void cancelShoppingListSucceed() {
    facade.cancelShoppingList(23);

    verify(shoppingListService, times(1)).changeShoppingListState(eq(23),
        eq(ShoppingListState.CANCELLED));
  }

  public void getShoppingListsPositions() {
    when(shoppingListService.getShoppingListPositions(25)).thenReturn(
        singletonList(shoppingListPositionDTO));

    final List<ListItemDTO> result = facade.getShoppingListsPositions(25);

    assertEquals(result, singletonList(shoppingListPositionDTO));
  }

  public void takeOverShoppingList() {
    facade.takeOverShoppingList(2, 3);

    verify(shoppingListService, times(1)).takeOverShoppingListState(eq(2), eq(3));
  }

  public void getShoppingListsById() {
    when(shoppingListService.getShoppingListsById(2)).thenReturn(shoppingListDTO);

    final ShoppingListDTO result = facade.getShoppingListsById(2);

    assertEquals(result, shoppingListDTO);
  }

  public void getGroupShoppingLists() {
    when(shoppingListService.getGroupShoppingLists(2, singletonList(ShoppingListState.DONE)))
        .thenReturn(singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result =
        facade.getGroupShoppingLists(2, singletonList(ShoppingListState.DONE));

    assertEquals(result, singletonList(shoppingListDTO));
  }

  public void getShoppingListsAssignedToUser() {
    when(shoppingListService.getShoppingListsAssignedToUser(2,44)).thenReturn(
        singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = facade.getShoppingListsAssignedToUser(2,44);

    assertEquals(result, singletonList(shoppingListDTO));
  }
}
