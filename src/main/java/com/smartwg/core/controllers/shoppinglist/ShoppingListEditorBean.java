package com.smartwg.core.controllers.shoppinglist;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.GroupFacade;
import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.facades.UserFacade;
import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.util.PrimefacesUtil;

import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant (ks)
 */
@Named(value = "shoppingList")
@Scope(value = "view")
public class ShoppingListEditorBean {

  // validation messages
  private static final String VALIDATION_WARN_NO_POSITION_ADDED =
      "At least one list position has to be added.";

  private ShoppingListDTO currentShoppingList;
  private boolean selectActivity;
  private List<ActivityDTO> activities;
  private GroupDTO currentGroup;

  // shopping list editor - list items
  private List<ListItemDTO> shoppingListsPositions;
  private String itemName;
  private List<CategoryDTO> categories;
  private CategoryDTO itemCategory;
  private int itemAmount = 1;

  @Inject
  private ShoppingListFacade shoppingListFacade;
  @Inject
  private UserFacade userFacade;
  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private GroupFacade groupFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;

  @PostConstruct
  public void init() {
    String listId =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
            .get("shoppingListId");
    if (listId != null) {
      final Integer integer = Integer.valueOf(listId);
      currentShoppingList = shoppingListFacade.getShoppingListsById(integer);
      currentShoppingList.setListItemDTOs(shoppingListFacade.getShoppingListsPositions(integer));
      currentShoppingList.setState(ShoppingListState.NEW);
    
      selectActivity = currentShoppingList.getActivityId() != null;
    } else {
      currentShoppingList = new ShoppingListDTO();
      currentShoppingList.setState(ShoppingListState.NEW);
      currentShoppingList.setEmailNotification(userFacade.isNotificationActivated(userBean
          .getCurrentUserGroup().getUserId(), NotificationType.SHOPPING_LIST));
    }
    if (currentGroup == null) {
      currentGroup = groupFacade.findById(userBean.getCurrentUserGroup().getGroupId());
    }
    if (activities == null) {
      activities = activityFacade.getActivitiesForGroup(currentGroup.getId());
    }
    if (categories == null) {
      categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());
    }
  }

  public String saveShoppingList() {
    if (currentShoppingList.getListItemDTOs().isEmpty() || allListPositionsDeleted()) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_WARN,
          VALIDATION_WARN_NO_POSITION_ADDED);
      return null;
    }
    if(!selectActivity){
      currentShoppingList.setActivityId(null);
      currentShoppingList.setActivityName(null);
    }
    currentShoppingList.setCreatedBy(userBean.getCurrentUser().getId());
    shoppingListFacade.saveShoppingList(currentShoppingList, currentGroup);
    return navigation.getPageUserShoppingLists();
  }

  private boolean allListPositionsDeleted() {
    boolean allPositionsDeleted = true;
    for (ListItemDTO listItemDTO : currentShoppingList.getListItemDTOs()) {
      if (!listItemDTO.isDeleted()) {
        allPositionsDeleted = false;
        break;
      }
    }
    return allPositionsDeleted;
  }

  public void changeSelectActivity(final ValueChangeEvent event) {
    Boolean selectActivity = (Boolean) event.getNewValue();
    if (selectActivity != null) {
      this.selectActivity = selectActivity;
    }
    FacesContext.getCurrentInstance().renderResponse();
  }

  public void addShoppingListItem() {
    final ListItemDTO listItemDTO =
        new ListItemDTO(null, itemName, itemCategory, itemAmount, false);
    currentShoppingList.getListItemDTOs().add(listItemDTO);
    itemName = null;
    itemCategory = null;
    itemAmount = 1;
  }

  public void deleteShoppingListItem(final ListItemDTO listItemDTO) {
    listItemDTO.setDeleted(Boolean.TRUE);
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

  public boolean isSelectActivity() {
    return selectActivity;
  }

  public void setSelectActivity(boolean selectActivity) {
    this.selectActivity = selectActivity;
  }

  public List<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(List<ActivityDTO> activities) {
    this.activities = activities;
  }

  public List<ListItemDTO> getShoppingListsPositions() {
    return shoppingListsPositions;
  }

  public void setShoppingListsPositions(final List<ListItemDTO> shoppingListsPositions) {
    this.shoppingListsPositions = shoppingListsPositions;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public CategoryDTO getItemCategory() {
    return itemCategory;
  }

  public void setItemCategory(CategoryDTO itemCategory) {
    this.itemCategory = itemCategory;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

  public int getItemAmount() {
    return itemAmount;
  }

  public void setItemAmount(int itemAmount) {
    this.itemAmount = itemAmount;
  }
}
