package com.smartwg.core.controllers.shoppinglist;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Kamil Sierzant (ks)
 */
@Named(value = "userShoppingList")
@Scope("view")
public class UserShoppingListBean {

  // validation messages
  private static final String VALIDATION_INFO_LIST_RELEASED =
      "Shopping list %s successfully released.";
  private static final String VALIDATION_INFO_LIST_DELETED =
      "Shopping list %s successfully deleted.";
  private static final String VALIDATION_INFO_LIST_CANCELLED =
      "Shopping list %s successfully cancelled.";

  private static final List<ShoppingListState> ALL_LIST_STATES = Arrays.asList(
      ShoppingListState.NEW, ShoppingListState.CANCELLED, ShoppingListState.RELEASED,
      ShoppingListState.ASSIGNED, ShoppingListState.DONE);
  private static final String REDIRECT = "?faces-redirect=true";

  // user shopping list overview
  private List<ShoppingListDTO> myShoppingLists;
  private List<ShoppingListDTO> shoppingListsAssignedToMe;
  private ShoppingListDTO currentShoppingList;
  private List<ShoppingListDTO> filteredUserShoppingLists;
  private ShoppingListState userFilterShoppingListState;

  @Inject
  private ShoppingListFacade shoppingListFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;


  @PostConstruct
  public void init() {
    myShoppingLists =
        shoppingListFacade.getUserShoppingLists(userBean.getCurrentUser().getId(), userBean
            .getCurrentGroup().getId());
    shoppingListsAssignedToMe =
        shoppingListFacade.getShoppingListsAssignedToUser(userBean.getCurrentUser().getId(),
            userBean.getCurrentGroup().getId());
  }

  public String createShoppingList() {
    return navigation.getPageNewShoppingList();
  }

  public String viewShoppingList(final ShoppingListDTO shoppingList, final String returnPage) {
    return navigation.getPageShoppingListOverview() + REDIRECT + "&shoppingListId="
        + shoppingList.getId() + "&returnPage=" + returnPage;
  }

  public String editShoppingList(final ShoppingListDTO shoppingList) {
    return navigation.getPageNewShoppingList() + REDIRECT + "&shoppingListId="
        + shoppingList.getId();
  }

  public String deleteShoppingList(final ShoppingListDTO selectedShoppingList) {
    final ShoppingListState currentState = selectedShoppingList.getState();

    if (Arrays.asList(ShoppingListState.NEW, ShoppingListState.CANCELLED, ShoppingListState.DONE)
        .contains(currentState)) {
      shoppingListFacade.deleteShoppingList(selectedShoppingList.getId());
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
          String.format(VALIDATION_INFO_LIST_DELETED, selectedShoppingList.getName()));
    } else if (ShoppingListState.RELEASED.equals(currentState)) {
      shoppingListFacade.cancelShoppingList(selectedShoppingList.getId());
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
          String.format(VALIDATION_INFO_LIST_CANCELLED, selectedShoppingList.getName()));
    }

    return navigation.getPageUserShoppingLists();
  }

  public String releaseShoppingList(final ShoppingListDTO selectedShoppingList) {
    shoppingListFacade.releaseShoppingList(selectedShoppingList.getId());
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
        String.format(VALIDATION_INFO_LIST_RELEASED, selectedShoppingList.getName()));
    return navigation.getPageUserShoppingLists();
  }

  public List<ShoppingListState> getAllShoppingListStates() {
    return ALL_LIST_STATES;
  }

  // getters and setters
  public List<ShoppingListDTO> getMyShoppingLists() {
    return myShoppingLists;
  }

  public void setMyShoppingLists(List<ShoppingListDTO> myShoppingLists) {
    this.myShoppingLists = myShoppingLists;
  }

  public List<ShoppingListDTO> getShoppingListsAssignedToMe() {
    return shoppingListsAssignedToMe;
  }

  public void setShoppingListsAssignedToMe(List<ShoppingListDTO> shoppingListsAssignedToMe) {
    this.shoppingListsAssignedToMe = shoppingListsAssignedToMe;
  }

  public ShoppingListDTO getCurrentShoppingList() {
    return currentShoppingList;
  }

  public void setCurrentShoppingList(ShoppingListDTO currentShoppingList) {
    this.currentShoppingList = currentShoppingList;
  }

  public List<ShoppingListDTO> getFilteredUserShoppingLists() {
    return filteredUserShoppingLists;
  }

  public void setFilteredUserShoppingLists(List<ShoppingListDTO> filteredUserShoppingLists) {
    this.filteredUserShoppingLists = filteredUserShoppingLists;
  }

  public ShoppingListState getUserFilterShoppingListState() {
    return userFilterShoppingListState;
  }

  public void setUserFilterShoppingListState(ShoppingListState userFilterShoppingListState) {
    this.userFilterShoppingListState = userFilterShoppingListState;
  }
}
