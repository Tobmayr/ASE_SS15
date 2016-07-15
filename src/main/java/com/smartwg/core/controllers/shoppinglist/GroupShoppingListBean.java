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
@Named(value = "groupShoppingList")
@Scope("view")
public class GroupShoppingListBean {

  // validation messages
  private static final String VALIDATION_INFO_LIST_RELEASED =
      "Shopping list %s successfully released.";
  private static final String VALIDATION_INFO_LIST_TAKEN_OVER =
      "Shopping list %s successfully taken over by user %s.";

  private static final String REDIRECT = "?faces-redirect=true";
  private static final List<ShoppingListState> GROUP_LIST_STATES = Arrays.asList(
      ShoppingListState.RELEASED, ShoppingListState.ASSIGNED, ShoppingListState.DONE);

  // group shopping list overview
  private List<ShoppingListDTO> groupShoppingLists;
  private List<ShoppingListDTO> filteredGroupShoppingLists;
  private ShoppingListState groupFilterShoppingListState;
  private ShoppingListDTO currentShoppingList;

  @Inject
  private ShoppingListFacade shoppingListFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;

  @PostConstruct
  public void init() {
    groupShoppingLists =
        shoppingListFacade.getGroupShoppingLists(userBean.getCurrentUserGroup().getGroupId(),
            GROUP_LIST_STATES);
  }

  // actions
  public String viewShoppingList(final ShoppingListDTO shoppingList, final String returnPage) {
    return navigation.getPageShoppingListOverview() + REDIRECT + "&shoppingListId="
        + shoppingList.getId() + "&returnPage=" + returnPage;
  }

  public String releaseShoppingList(final ShoppingListDTO selectedShoppingList) {
    shoppingListFacade.releaseShoppingList(selectedShoppingList.getId());
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO,
        String.format(VALIDATION_INFO_LIST_RELEASED, selectedShoppingList.getName()));
    return navigation.getPageGroupShoppingLists();
  }

  public String takeOverShoppingList(final ShoppingListDTO selectedShoppingList) {
    shoppingListFacade.takeOverShoppingList(selectedShoppingList.getId(), userBean.getCurrentUser()
        .getId());
    PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO, String.format(
        VALIDATION_INFO_LIST_TAKEN_OVER,
        selectedShoppingList.getName(),
        selectedShoppingList.getAssignedToFirstName()
            + selectedShoppingList.getAssignedToLastName()));
    return navigation.getPageGroupShoppingLists();
  }

  // getters & setters
  public List<ShoppingListState> getShoppingListStates() {
    return GROUP_LIST_STATES;
  }

  public List<ShoppingListDTO> getGroupShoppingLists() {
    return groupShoppingLists;
  }

  public void setGroupShoppingLists(List<ShoppingListDTO> groupShoppingLists) {
    this.groupShoppingLists = groupShoppingLists;
  }

  public List<ShoppingListDTO> getFilteredGroupShoppingLists() {
    return filteredGroupShoppingLists;
  }

  public void setFilteredGroupShoppingLists(List<ShoppingListDTO> filteredGroupShoppingLists) {
    this.filteredGroupShoppingLists = filteredGroupShoppingLists;
  }

  public ShoppingListState getGroupFilterShoppingListState() {
    return groupFilterShoppingListState;
  }

  public void setGroupFilterShoppingListState(ShoppingListState groupFilterShoppingListState) {
    this.groupFilterShoppingListState = groupFilterShoppingListState;
  }

  public ShoppingListDTO getCurrentShoppingList() {
    return currentShoppingList;
  }

  public void setCurrentShoppingList(ShoppingListDTO currentShoppingList) {
    this.currentShoppingList = currentShoppingList;
  }
}
