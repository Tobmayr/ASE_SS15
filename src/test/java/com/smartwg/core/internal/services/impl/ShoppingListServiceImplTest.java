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

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.smartwg.core.internal.domain.ShoppingListState.ASSIGNED;
import static com.smartwg.core.internal.domain.ShoppingListState.DONE;
import static com.smartwg.core.internal.domain.ShoppingListState.NEW;
import static com.smartwg.core.internal.domain.ShoppingListState.RELEASED;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.joda.time.DateTime.parse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Test
public class ShoppingListServiceImplTest {

  private static final Integer USER_ID = 44;
  private static final Integer SHOPPING_LIST_ID = 55;
  private static final Integer GROUP_ID = 32;
  private static final String EMAIL = "kamil.sierzant@gmail.com";
  private static final Integer ACTIVITY_ID = 44;

  @InjectMocks
  private ShoppingListService service;

  @Mock
  private ShoppingListRepository shoppingListRepository;
  @Mock
  private ListPositionRepository listPosRepository;
  @Mock
  private EntityConverter converter;
  @Mock
  private UserService userService;
  @Mock
  private EmailService emailService;
  @Mock
  private ActivityService activityService;

  @Mock
  private ShoppingListDTO shoppingListDTO;
  @Mock
  private ShoppingList shoppingList;
  @Mock
  private ListItemDTO listItemDTO;
  @Mock
  private ListPosition listPosition;
  @Mock
  private GroupDTO groupDTO;
  @Mock
  private User user;

  @BeforeMethod
  public void setUp() {
    service = new ShoppingListServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getUserShoppingListsWhenNothingFound() {
    when(shoppingListRepository.findUserShoppingLists(USER_ID,GROUP_ID)).thenReturn(new ArrayList<>());

    final List<ShoppingListDTO> result = service.getUserShoppingLists(USER_ID,GROUP_ID);

    assertEquals(result, Collections.EMPTY_LIST);
  }

  public void getUserShoppingListsWhenShoppingListFound() {
    when(shoppingListRepository.findUserShoppingLists(eq(USER_ID),eq(GROUP_ID))).thenReturn(
        singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = service.getUserShoppingLists(USER_ID,GROUP_ID);

    assertEquals(result, singletonList(shoppingListDTO));
  }

  public void getShoppingListsAssignedToUserWhenShoppingListFound() {
    when(shoppingListRepository.findShoppingListsAssignedToUser(USER_ID,GROUP_ID)).thenReturn(
        singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = service.getShoppingListsAssignedToUser(USER_ID,GROUP_ID);

    assertEquals(result, singletonList(shoppingListDTO));
  }

  public void getGroupShoppingListsWhenShoppingListFound() {
    final List<ShoppingListState> listStates = asList(ASSIGNED, RELEASED);

    when(shoppingListRepository.findGroupShoppingLists(eq(GROUP_ID), eq(listStates))).thenReturn(
        singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = service.getGroupShoppingLists(GROUP_ID, listStates);

    assertEquals(result, singletonList(shoppingListDTO));
  }

  public void saveShoppingList() {
    final ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    final ArgumentCaptor<Group> groupArgument = ArgumentCaptor.forClass(Group.class);
    final ArgumentCaptor<Activity> activityArgument = ArgumentCaptor.forClass(Activity.class);
    final ShoppingListDTO dto =
        new ShoppingListDTO(null, "Party", parse("2015-05-22").toDate(), false, USER_ID,
                            null, null, null, null, null, null, null, false, NEW, ACTIVITY_ID,
                            null);

    when(groupDTO.getId()).thenReturn(23);
    when(converter.createUserShoppingList(eq(dto))).thenReturn(shoppingList);

    service.saveShoppingList(dto, groupDTO);

    verify(shoppingList, times(1)).setCreatedBy(userArgument.capture());
    verify(shoppingList, times(1)).setGroup(groupArgument.capture());
    verify(shoppingList, times(1)).setActivity(activityArgument.capture());
    verify(shoppingListRepository).save(shoppingList);
    assertEquals(userArgument.getValue().getId(), USER_ID);
    assertEquals(groupArgument.getValue().getId(), Integer.valueOf(23));
    assertEquals(activityArgument.getValue().getId(), ACTIVITY_ID);
  }

  public void updateShoppingListWithoutListPositions() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(),
                            false, USER_ID, null, null, null, null, null, null, null, false, NEW,
                            null, null);

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(NEW));
    verify(converter, never()).createListPosition(any(ListItemDTO.class));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, never()).setAsDone(anyInt(), anyInt());
    verify(emailService, never())
        .createAndSendShoppingListDoneEmail(any(ShoppingListDTO.class), anySet());
  }

  public void updateShoppingListWithNewListPositions() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(),
                            false, USER_ID, null, null, null, null, null, null, null, false, NEW,
                            null, null);
    final ListItemDTO alcohol =
        new ListItemDTO(null, "Bier 0.5l", 23, "Alcohol", false, false, 24, false);
    dto.setListItemDTOs(singletonList(alcohol));

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);
    when(converter.createListPosition(eq(alcohol))).thenReturn(listPosition);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(NEW));
    verify(listPosition, times(1)).setShoppingList(eq(shoppingList));
    verify(shoppingList, times(1)).addListPosition(eq(listPosition));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, never()).setAsDone(anyInt(), anyInt());
    verify(emailService, never())
        .createAndSendShoppingListDoneEmail(any(ShoppingListDTO.class), anySet());
  }

  public void updateShoppingListWithChangedListPositions() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(),
                            false, USER_ID, null, null, null, null, null, null, null, false, NEW,
                            null, null);
    final ListItemDTO alcohol =
        new ListItemDTO(666, "Bier 0.5l", 23, "Alcohol", false, false, 24, false);
    dto.setListItemDTOs(singletonList(alcohol));

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);
    when(shoppingList.getListPositions()).thenReturn(singletonList(listPosition));
    when(listPosition.getId()).thenReturn(666);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(NEW));
    verify(listPosition, times(1)).setName(eq("Bier 0.5l"));
    verify(listPosition, times(1)).setCategory(any(Category.class));
    verify(listPosition, times(1)).setAmount(eq(24));
    verify(converter, never()).createListPosition(any(ListItemDTO.class));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, never()).setAsDone(anyInt(), anyInt());
    verify(emailService, never())
        .createAndSendShoppingListDoneEmail(any(ShoppingListDTO.class), anySet());
  }

  public void updateShoppingListWithDeletedListPositions() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(),
                            false, USER_ID, null, null, null, null, null, null, null, false, NEW,
                            null, null);
    final ListItemDTO alcohol =
        new ListItemDTO(666, "Bier 0.5l", 23, "Alcohol", false, false, 24, false);
    alcohol.setDeleted(Boolean.TRUE);
    dto.setListItemDTOs(singletonList(alcohol));

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);
    when(shoppingList.getListPositions()).thenReturn(new ArrayList<ListPosition>() {
      {
        add(listPosition);
      }
    });
    when(listPosition.getId()).thenReturn(666);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(NEW));
    verify(listPosition, never()).setName(anyString());
    verify(listPosition, never()).setCategory(any(Category.class));
    verify(listPosition, never()).setAmount(anyInt());
    verify(converter, never()).createListPosition(any(ListItemDTO.class));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, never()).setAsDone(anyInt(), anyInt());
    verify(emailService, never())
        .createAndSendShoppingListDoneEmail(any(ShoppingListDTO.class), anySet());
  }

  public void updateShoppingListWhenAllPositionsAreDone() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(), true, USER_ID,
                            null, null, null, null, null, null, null, false, NEW, null, null);
    dto.setListItemDTOs(
        singletonList(new ListItemDTO(666, "Bier 0.5l", 23, "Alcohol", false, false, 24, true)));
    Set<String> recipients = new HashSet<String>() {{
      add(EMAIL);
    }};

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);
    when(shoppingList.getListPositions()).thenReturn(singletonList(listPosition));
    when(shoppingList.isEmailNotification()).thenReturn(true);
    when(listPosition.getId()).thenReturn(666);
    when(userService.findUserById(eq(USER_ID))).thenReturn(user);
    when(user.getEmail()).thenReturn(EMAIL);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(DONE));
    verify(listPosition, times(1)).setDone(eq(true));
    verify(converter, never()).createListPosition(any(ListItemDTO.class));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, never()).setAsDone(anyInt(), anyInt());
    verify(emailService, times(1)).createAndSendShoppingListDoneEmail(eq(dto), eq(recipients));
  }

  public void updateShoppingListWhenAllPositionsAreDoneAndLinkedActivity() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(SHOPPING_LIST_ID, "Party", parse("2015-05-22").toDate(), true, USER_ID,
                            null, null, null, USER_ID, null, null, null, false, NEW, ACTIVITY_ID,
                            null);
    dto.setListItemDTOs(
        singletonList(new ListItemDTO(666, "Bier 0.5l", 23, "Alcohol", false, false, 24, true)));
    Set<String> recipients = new HashSet<String>() {{
      add(EMAIL);
    }};

    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);
    when(shoppingList.getListPositions()).thenReturn(singletonList(listPosition));
    when(shoppingList.isEmailNotification()).thenReturn(true);
    when(listPosition.getId()).thenReturn(666);
    when(userService.findUserById(eq(USER_ID))).thenReturn(user);
    when(user.getEmail()).thenReturn(EMAIL);

    service.updateShoppingList(dto);

    verify(shoppingList, times(1)).setName(eq("Party"));
    verify(shoppingList, times(1)).setDeadline(eq(parse("2015-05-22").toDate()));
    verify(shoppingList, times(1)).setPrivateList(eq(false));
    verify(shoppingList, times(1)).setState(eq(DONE));
    verify(listPosition, times(1)).setDone(eq(true));
    verify(converter, never()).createListPosition(any(ListItemDTO.class));
    verify(shoppingListRepository).save(shoppingList);
    verify(activityService, times(1)).setAsDone(eq(ACTIVITY_ID), eq(USER_ID));
    verify(emailService, times(1)).createAndSendShoppingListDoneEmail(eq(dto), eq(recipients));
  }

  public void deleteShoppingListFailsWhenListDoesNotExists() {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(null);

    service.deleteShoppingList(SHOPPING_LIST_ID);

    verify(shoppingListRepository, never()).delete(any(ShoppingList.class));
  }

  public void deleteShoppingListSucceed() {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);

    service.deleteShoppingList(SHOPPING_LIST_ID);

    verify(shoppingListRepository, times(1)).delete(eq(shoppingList));
  }

  @Test(dataProvider = "shoppingListStates")
  public void changeShoppingListStateFailsWhenListDoesNotExists(final ShoppingListState state) {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(null);

    service.changeShoppingListState(SHOPPING_LIST_ID, state);

    verify(shoppingList, never()).setState(any(ShoppingListState.class));
    verify(shoppingListRepository, never()).save(any(ShoppingList.class));
  }

  @Test(dataProvider = "shoppingListStates")
  public void changeShoppingListStateSucceed(final ShoppingListState state) {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);

    service.changeShoppingListState(SHOPPING_LIST_ID, state);

    verify(shoppingList, times(1)).setState(eq(state));
    verify(shoppingListRepository, times(1)).save(eq(shoppingList));
  }

  @DataProvider(name = "shoppingListStates")
  private Object[][] getShoppingListStates() {
    return new Object[][]{{NEW}, {RELEASED},
                          {ShoppingListState.CANCELLED}, {ShoppingListState.DONE}};
  }

  public void getShoppingListPositionsWhenNothingFound() {
    when(listPosRepository.findShoppingListPositions(eq(SHOPPING_LIST_ID))).thenReturn(
        new ArrayList<>());

    final List<ListItemDTO> result = service.getShoppingListPositions(SHOPPING_LIST_ID);

    assertEquals(result, Collections.EMPTY_LIST);
  }

  public void getShoppingListPositionsWhenShoppingListFound() {
    when(listPosRepository.findShoppingListPositions(eq(SHOPPING_LIST_ID))).thenReturn(
        singletonList(listItemDTO));

    final List<ListItemDTO> result = service.getShoppingListPositions(SHOPPING_LIST_ID);

    assertEquals(result, singletonList(listItemDTO));
  }

  public void getShoppingListsById() {
    when(shoppingListRepository.findShoppingListById(33)).thenReturn(shoppingListDTO);

    final ShoppingListDTO result = service.getShoppingListsById(33);

    assertEquals(result, shoppingListDTO);
  }

  public void takeOverShoppingListState() {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(shoppingList);

    service.takeOverShoppingListState(SHOPPING_LIST_ID, USER_ID);

    verify(shoppingListRepository, times(1)).save(eq(shoppingList));

  }

  public void takeOverShoppingListStateWithWrongId() {
    when(shoppingListRepository.findById(eq(SHOPPING_LIST_ID))).thenReturn(null);

    service.takeOverShoppingListState(SHOPPING_LIST_ID, USER_ID);

    verify(shoppingListRepository, times(0)).save(eq(shoppingList));
  }
}
