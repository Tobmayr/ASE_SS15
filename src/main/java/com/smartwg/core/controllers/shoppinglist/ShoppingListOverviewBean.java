package com.smartwg.core.controllers.shoppinglist;

import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;

import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks)
 */
@Named(value = "shoppingListOverview")
@Scope(value = "view")
public class ShoppingListOverviewBean {

  private ShoppingListDTO currentShoppingList;
  private List<ListItemDTO> selectedListPositions;
  private String returnPage;

  @Inject
  private ShoppingListFacade shoppingListFacade;

  @PostConstruct
  public void init() {
    final Map<String, String> requestParameterMap =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String listId = requestParameterMap.get("shoppingListId");
    returnPage = requestParameterMap.get("returnPage");

    if (listId != null) {
      final Integer integer = Integer.valueOf(listId);
      currentShoppingList = shoppingListFacade.getShoppingListsById(integer);
      currentShoppingList.setListItemDTOs(shoppingListFacade.getShoppingListsPositions(integer));
    }

    selectedListPositions = new ArrayList<>();
    for (ListItemDTO shoppingListsPosition : currentShoppingList.getListItemDTOs()) {
      if (shoppingListsPosition.isDone()) {
        this.selectedListPositions.add(shoppingListsPosition);
      }
    }
  }

  public String updateShoppingList() {
    for (ListItemDTO selectedListPosition : selectedListPositions) {
      selectedListPosition.setDone(Boolean.TRUE);
    }
    shoppingListFacade.saveShoppingList(currentShoppingList, null);
    return returnPage;
  }

  public String completeShoppingList() {
    currentShoppingList.setState(ShoppingListState.DONE);
    shoppingListFacade.saveShoppingList(currentShoppingList, null);
    return returnPage;
  }

  public List<ListItemDTO> getFilteredItemList() {
    final List<ListItemDTO> filteredList = new ArrayList<>();

    for (ListItemDTO listItemDTO : this.currentShoppingList.getListItemDTOs()) {
      if (!listItemDTO.isDeleted()) {
        filteredList.add(listItemDTO);
      }
    }
    return filteredList;
  }

  // getters & setters
  public ShoppingListDTO getCurrentShoppingList() {
    return currentShoppingList;
  }

  public void setCurrentShoppingList(ShoppingListDTO currentShoppingList) {
    this.currentShoppingList = currentShoppingList;
  }

  public List<ListItemDTO> getSelectedListPositions() {
    return selectedListPositions;
  }

  public void setSelectedListPositions(List<ListItemDTO> selectedListPositions) {
    this.selectedListPositions = selectedListPositions;
  }

  public String getReturnPage() {
    return returnPage;
  }

  public void setReturnPage(String returnPage) {
    this.returnPage = returnPage;
  }
}
