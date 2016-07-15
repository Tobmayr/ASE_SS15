package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.ListPosition;
import com.smartwg.core.internal.domain.entities.ShoppingList;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.repositories.ListPositionRepository;
import com.smartwg.core.internal.repositories.ShoppingListRepository;
import com.smartwg.core.internal.services.ActivityService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.ShoppingListService;
import com.smartwg.core.internal.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import static com.smartwg.core.internal.domain.ShoppingListState.DONE;

/**
 * @author Kamil Sierzant (ks)
 */
@Named
public class ShoppingListServiceImpl implements ShoppingListService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListServiceImpl.class);

  @Inject
  private ShoppingListRepository shoppingListRepository;
  @Inject
  private ListPositionRepository listPosRepository;
  @Inject
  private EntityConverter converter;
  @Inject
  private EmailService emailService;
  @Inject
  private UserService userService;
  @Inject
  private ActivityService activityService;

  @SuppressWarnings("unchecked")
  @Override
  public List<ShoppingListDTO> getUserShoppingLists(final Integer userId, final Integer groupId) {
    final List<ShoppingListDTO> result = shoppingListRepository.findUserShoppingLists(userId,groupId);
    LOGGER.info(String.format("%d shopping lists has been found for user with the id %d",
                              result.size(), userId));
    return result.isEmpty() ? Collections.EMPTY_LIST : result;
  }

  @Override
  public List<ShoppingListDTO> getGroupShoppingLists(final Integer groupId,
                                                     final List<ShoppingListState> listStates) {
    final List<ShoppingListDTO> result =
        shoppingListRepository.findGroupShoppingLists(groupId, listStates);
    LOGGER.info("%d shopping lists has been found for group with the id %d in states (%s)",
                result.size(), groupId, listStates);

    return result.isEmpty() ? Collections.EMPTY_LIST : result;
  }

  @Override
  public List<ShoppingListDTO> getShoppingListsAssignedToUser(Integer userId, Integer groupId) {
    final List<ShoppingListDTO> result =
        shoppingListRepository.findShoppingListsAssignedToUser(userId,groupId);
    LOGGER.info(String.format("%d shopping lists has been found assigned to user with the id %d",
                              result.size(), userId));
    return result.isEmpty() ? Collections.EMPTY_LIST : result;
  }

  @Override
  public void saveShoppingList(final ShoppingListDTO shoppingListDTO, final GroupDTO groupDTO) {
    final ShoppingList shoppingList = converter.createUserShoppingList(shoppingListDTO);
    final User creator = new User();
    creator.setId(shoppingListDTO.getCreatedBy());
    shoppingList.setCreatedBy(creator);

    if (shoppingListDTO.getActivityId() != null) {
      final Activity linkedActivity = new Activity();
      linkedActivity.setId(shoppingListDTO.getActivityId());
      shoppingList.setActivity(linkedActivity);
    }

    if (groupDTO != null) {
      Group group = new Group();
      group.setId(groupDTO.getId());
      shoppingList.setGroup(group);
    }

    shoppingListRepository.save(shoppingList);
  }

  @Override
  public void updateShoppingList(final ShoppingListDTO shoppingListDTO) {
    final ShoppingList shoppingList = shoppingListRepository.findById(shoppingListDTO.getId());
    updateShoppingListBaseData(shoppingListDTO, shoppingList);

    final Map<Integer, ListPosition> listPositions =
        getListPositions(shoppingList.getListPositions());

    int donePositions = 0;
    for (ListItemDTO listItemDTO : shoppingListDTO.getListItemDTOs()) {
      final Integer id = listItemDTO.getId();

      if (id == null) {
        createListPosition(shoppingList, listItemDTO);
      } else if (listItemDTO.isDeleted()) {
        removeListPosition(shoppingList, listPositions.get(id));
      } else if (listItemDTO.isDone()) {
        donePositions++;
        finishListPosition(listPositions.get(id));
      } else {
        updateListPosition(listPositions.get(id), listItemDTO);
      }
    }
    if (DONE.equals(shoppingListDTO.getState())
        || donePositions == shoppingListDTO.getListItemDTOs().size()) {
      shoppingList.setState(DONE);
      shoppingListDTO.setState(DONE);
      if (shoppingListDTO.getActivityId() != null) {
        activityService.setAsDone(shoppingListDTO.getActivityId(), shoppingListDTO.getAssignedTo());
      }
      if (shoppingList.isEmailNotification()) {
        sendEmailNotification(shoppingListDTO);
      }
    }
    shoppingListRepository.save(shoppingList);
  }

  private void updateShoppingListBaseData(final ShoppingListDTO shoppingListDTO,
                                          final ShoppingList shoppingList) {
    shoppingList.setName(shoppingListDTO.getName());
    shoppingList.setDeadline(shoppingListDTO.getDeadline());
    shoppingList.setEmailNotification(shoppingListDTO.isEmailNotification());
    shoppingList.setPrivateList(shoppingListDTO.isPrivateList());
    shoppingList.setState(shoppingListDTO.getState());
    shoppingList.setActivity(getLinkedActivity(shoppingListDTO.getActivityId()));
  }

  private void createListPosition(final ShoppingList shoppingList, final ListItemDTO listItemDTO) {
    final ListPosition listPosition = converter.createListPosition(listItemDTO);
    listPosition.setShoppingList(shoppingList);
    shoppingList.addListPosition(listPosition);
  }

  private boolean removeListPosition(final ShoppingList shoppingList,
                                     final ListPosition listPosition) {
    return shoppingList.getListPositions().remove(listPosition);
  }

  private void finishListPosition(final ListPosition listPosition) {
    listPosition.setDone(Boolean.TRUE);
  }

  private void updateListPosition(final ListPosition listPosition, final ListItemDTO listItemDTO) {
    listPosition.setName(listItemDTO.getName());
    listPosition.setAmount(listItemDTO.getAmount());
    final Category category = new Category();
    category.setId(listItemDTO.getCategory().getId());
    listPosition.setCategory(category);
  }

  private Activity getLinkedActivity(final Integer activityId) {
    final Activity linkedActivity = new Activity();
    if (activityId != null) {
      linkedActivity.setId(activityId);
      return linkedActivity;
    } else {
      return null;
    }
  }

  private Map<Integer, ListPosition> getListPositions(final List<ListPosition> listPositions) {
    final Map<Integer, ListPosition> result = new HashMap<>();
    for (ListPosition listPosition : listPositions) {
      result.put(listPosition.getId(), listPosition);
    }
    return result;
  }

  private void sendEmailNotification(final ShoppingListDTO shoppingList) {
    final User shoppingListOwner = userService.findUserById(shoppingList.getCreatedBy());
    final HashSet<String> recipients = new HashSet<String>() {
      {
        add(shoppingListOwner.getEmail());
      }
    };
    emailService.createAndSendShoppingListDoneEmail(shoppingList, recipients);
  }

  @Override
  public void deleteShoppingList(final Integer shoppingListId) {
    final ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId);
    if (shoppingList != null) {
      LOGGER.info(String.format("Shopping list with the id %d has been removed.", shoppingListId));
      shoppingListRepository.delete(shoppingList);
    } else {

      LOGGER.error(String
                       .format("Shopping list with the id %d could not be found.", shoppingListId));
    }
  }

  @Override
  public void changeShoppingListState(final Integer shoppingListId, final ShoppingListState state) {
    final ShoppingList result = shoppingListRepository.findById(shoppingListId);
    if (result != null) {

      LOGGER.info(String.format("Shopping list state has been changed from '%s' to '%s'",
                                result.getState(), state));
      result.setState(state);
      result.setAssignedTo(null);
      shoppingListRepository.save(result);
    } else {
      LOGGER.error(String
                       .format("Shopping list with the id %d could not be found.", shoppingListId));
    }
  }

  @Override
  public List<ListItemDTO> getShoppingListPositions(final Integer shoppingListId) {
    final List<ListItemDTO> result = listPosRepository.findShoppingListPositions(shoppingListId);
    LOGGER.info(String.format(
        "%d shopping list positions has been found in the shopping list with the id %d",
        result.size(), shoppingListId));

    return result.isEmpty() ? new ArrayList<>() : result;
  }

  @Override
  public void takeOverShoppingListState(final Integer shoppingListId, final Integer userId) {
    final ShoppingList result = shoppingListRepository.findById(shoppingListId);
    if (result != null) {

      LOGGER.info(String.format("Shopping list state has been changed from '%s' to '%s'",
                                result.getState(), ShoppingListState.ASSIGNED));
      result.setState(ShoppingListState.ASSIGNED);
      final User user = new User();
      user.setId(userId);
      result.setAssignedTo(user);
      shoppingListRepository.save(result);
    } else {

      LOGGER.error(String
                       .format("Shopping list with the id %d could not be found.", shoppingListId));
    }
  }

  @Override
  public ShoppingListDTO getShoppingListsById(final Integer listId) {
    return shoppingListRepository.findShoppingListById(listId);
  }
}
