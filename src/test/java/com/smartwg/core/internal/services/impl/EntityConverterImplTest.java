package com.smartwg.core.internal.services.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.dtos.RecurringDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.dtos.UserActivityDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.domain.entities.Absence;
import com.smartwg.core.internal.domain.entities.Activity;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.CostEntry;
import com.smartwg.core.internal.domain.entities.Country;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.ListPosition;
import com.smartwg.core.internal.domain.entities.Payment;
import com.smartwg.core.internal.domain.entities.PaymentUser;
import com.smartwg.core.internal.domain.entities.Recurring;
import com.smartwg.core.internal.domain.entities.Resource;
import com.smartwg.core.internal.domain.entities.Shop;
import com.smartwg.core.internal.domain.entities.ShoppingList;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.domain.entities.UserActivity;
import com.smartwg.core.internal.domain.entities.UserGroup;
import com.smartwg.core.internal.domain.entities.UserGroupCategory;
import com.smartwg.core.internal.services.EntityConverter;

@Test
public class EntityConverterImplTest {

  @InjectMocks
  private EntityConverter converter;


  @BeforeMethod
  public void setUp() {
    converter = new EntityConverterImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createUserShoppingList() {
    final ShoppingListDTO dto =
        new ShoppingListDTO(22, "Party", DateTime.parse("2015-05-22").toDate(), false, 11, null,
            null, null, null, null, null, null, false, ShoppingListState.NEW, null, null);
    final ListItemDTO alcohol =
        new ListItemDTO(null, "Bier 0.5l", 23, "Alcohol", false, false, 24, false);
    dto.setListItemDTOs(Arrays.asList(alcohol));

    final ShoppingList result = converter.createUserShoppingList(dto);
    assertEquals(result.getId(), Integer.valueOf(22));
    assertEquals(result.getName(), "Party");
    assertEquals(result.getDeadline(), DateTime.parse("2015-05-22").toDate());
    assertEquals(result.isPrivateList(), false);
    assertEquals(result.getState(), ShoppingListState.NEW);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createUserShoppingList_NullExcp() {
    converter.createUserShoppingList(null);
  }

  public void createListPosition() {
    final ListItemDTO alcohol =
        new ListItemDTO(null, "Bier 0.5l", 23, "Alcohol", false, false, 24, false);

    final ListPosition result = converter.createListPosition(alcohol);

    assertNull(result.getId());
    assertEquals(result.getName(), "Bier 0.5l");
    assertEquals(result.getCategory().getId(), Integer.valueOf(23));
    assertEquals(result.getAmount(), 24);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createListPosition_NulLExcp() {
    converter.createListPosition(null);
  }

  public void createAbsence() {
    final AbsenceDTO param =
        new AbsenceDTO(23, DateTime.parse("2014-05-22").toDate(), DateTime.parse("2014-08-22")
            .toDate(), true);

    final Absence result = converter.createAbsence(param);
    assertEquals(result.getId(), param.getId());
    assertEquals(result.getAwayFrom(), param.getAwayFrom());
    assertEquals(result.getAwayTill(), param.getAwayTill());
    assertEquals(result.isTemporary(), param.isTemporary());

  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createAbsence_NullExcp() {
    converter.createAbsence(null);
  }

  public void createBill() {
    final BillDTO dto =
        new BillDTO(22, "Party", DateTime.parse("2015-05-22").toDate(), 1, "User", new BigDecimal(
            22.0), false, 1, "EUR", 1, new Shop(), 4);
    final Bill result = converter.createBill(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getName(), dto.getName());
    assertEquals(result.getDate(), dto.getDate());
    assertEquals(result.getCreatedBy().getId(), dto.getCreatedBy_id());
    assertEquals(result.getTotal(), dto.getTotal());
    assertEquals(result.isPrivateBill(), dto.isPrivateBill());
    assertEquals(result.getCurrency().getId(), dto.getCurrency().getId());
    assertEquals(result.getGroup().getId(), dto.getGroupId());
    assertEquals(result.getPayment().getId(), dto.getPaymendId());


  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createBill_NullExcp() {
    converter.createBill(null);
  }

  public void createCostEntry() {
    final CostEntryDTO entry1 =
        new CostEntryDTO(1, "Alkohol", new BigDecimal(22.0), 1, false, 1, "Category", false, false);
    final CostEntry result = converter.createCostEntry(entry1);
    assertEquals(result.getId(), entry1.getId());
    assertEquals(result.getName(), entry1.getName());
    assertEquals(result.getAmount(), entry1.getAmount());
    assertEquals(result.isExcluded(), entry1.isExcluded());
    assertEquals(result.getCategory().getId(), entry1.getCategory().getId());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createCostEntry_NullExcp() {
    converter.createCostEntry(null);
  }

  public void createRecurring() {
    final RecurringDTO dto = new RecurringDTO(1, 2, RecurringType.MONTH, new Date(), new Date(), 2);

    final Recurring result = converter.createRecurring(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getValue(), dto.getValue());
    assertEquals(result.getRecurringType(), dto.getRecurringType());
    assertEquals(result.getDate(), dto.getDate());
    assertEquals(result.getEndDate(), dto.getEndDate());
    assertEquals(result.getTimes(), dto.getTimes());

  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createRecurring_NullExcp() {
    converter.createRecurring(null);
  }

  public void createResource() {
    final ResourceDTO dto = new ResourceDTO(1, "test", "application/pdf");
    dto.setFileURL("noAURL");
    dto.setContent(new byte[10]);
    final Resource result = converter.createResource(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getName(), dto.getName());
    assertEquals(result.getType(), dto.getType());
    assertEquals(result.getContent(), dto.getContent());
    assertEquals(result.getFileUrl(), dto.getFileURL());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createResource_NullExcp() {
    converter.createResource(null);
  }

  public void createUser() {
    final UserDTO dto =
        new UserDTO(1, "Fist", "Last", "user", "email", new Date(), new BigDecimal(100.00),
            NotificationType.ACTIVITY.name() + ";" + NotificationType.BILL.name());

    final User result = converter.createUser(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getFirstName(), dto.getFirstName());
    assertEquals(result.getLastName(), dto.getLastName());
    assertEquals(result.getEmail(), dto.getEmail());
    assertEquals(result.getIncome(), dto.getIncome());
    assertEquals(result.getNotify(),
        NotificationType.ACTIVITY.name() + ";" + NotificationType.BILL.name());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createUser_NullExcp() {
    converter.createUser(null);
  }

  public void createUserGroup() {
    final UserGroupDTO param = new UserGroupDTO();
    param.setGroupId(2);
    param.setUserId(3);
    param.setRole(Role.ADMIN);
    final UserGroup result = converter.createUserGroup(param);
    assertEquals(result.getGroup().getId(), param.getGroupId());
    assertEquals(result.getUser().getId(), param.getUserId());
    assertEquals(result.getRole(), param.getRole());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createUserGroup_NullExcp() {
    converter.createUserGroup(null);
  }

  public void createGroup() {
    final Country country = new Country();
    country.setId(1);
    final GroupDTO dto =
        new GroupDTO(1, "name", country, "city", "sdsa", "street", "street2", null);
    final Group result = converter.createGroup(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getName(), dto.getName());
    assertEquals(result.getCountry().getId(), dto.getCountry().getId());
    assertEquals(result.getCity(), dto.getCity());
    assertEquals(result.getZip(), dto.getZip());
    assertEquals(result.getStreet(), dto.getStreet());
    assertEquals(result.getStreet2(), dto.getStreet2());

  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createGroup_NullExcp() {
    converter.createGroup(null);
  }

  public void createActivity() {
    final ActivityDTO dto = new ActivityDTO(1, "name", new Date(), 5, 1, 1, 1, true, 34);
    final Activity result = converter.createActivity(dto);
    assertEquals(result.getId(), dto.getId());
    assertEquals(result.getName(), dto.getName());
    assertEquals(result.getDate(), dto.getDate());
    assertEquals(result.getPoints(), dto.getPoints());
    assertEquals(result.getAssignedTo().getId(), dto.getAssignedToUserId());
    assertEquals(result.getCreatedBy().getId(), dto.getCreatedByUserId());
    assertEquals(result.getGroup().getId(), dto.getGroupId());
    assertEquals(result.isDone(), dto.getIsDone());
    assertEquals(result.getRating(), dto.getRating());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createActivity_NullExcp() {
    converter.createActivity(null);
  }

  public void createCategory() {
    final UserGroupCategoryDTO ugc = new UserGroupCategoryDTO();
    final CategoryDTO param = new CategoryDTO(2, "TestCategory1", true, false);
    param.setDefaultCategory(true);
    param.setGroup_id(2);
    final Category result = converter.createCategory(param);
    assertEquals(result.getId(), param.getId());
    assertEquals(result.getName(), param.getName());
    assertEquals(result.isDefaultCategory(), param.isDefaultCategory());
    assertEquals(result.isFixedCost(), param.isFixedCost());
    assertEquals(result.getGroup().getId(), param.getGroup_id());

  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createCategory_NullExcp() {
    converter.createCategory(null);
  }

  public void createUserGroupCategory() {
    final CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(23);
    final UserGroupDTO userGroupDTO = new UserGroupDTO();
    userGroupDTO.setGroupId(34);
    userGroupDTO.setUserId(364);
    final UserGroupCategoryDTO param =
        new UserGroupCategoryDTO(new BigDecimal(0.8), categoryDTO, userGroupDTO);

    final UserGroupCategory result = converter.createUserGroupCategory(param);
    assertEquals(result.getCategory().getId(), param.getCategory().getId());
    assertEquals(result.getUserGroup().getGroup().getId(), param.getUserGroupDTO().getGroupId());
    assertEquals(result.getUserGroup().getUser().getId(), param.getUserGroupDTO().getUserId());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createUserGroupCategory_NullExcp() {
    converter.createUserGroupCategory(null);
  }

  public void createPayment() {
    final PaymentDTO param = new PaymentDTO(34, DateTime.parse("2015-05-22").toDate(), new byte[4]);
    final Payment result = converter.createPayment(param);
    assertEquals(result.getId(), param.getId());
    assertEquals(result.getDate(), param.getDate());
    assertEquals(result.getPaymentLog(), param.getPaymentLog());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createPayment_NullExcp() {
    converter.createPayment(null);
  }

  public void createPaymentUser() {
    final UserDTO sender = new UserDTO();
    sender.setId(34);
    final UserDTO receiver = new UserDTO();
    receiver.setId(542);
    final PaymentUserDTO param =
        new PaymentUserDTO(new BigDecimal(34.23), true, sender, receiver, 54);

    final PaymentUser result = converter.createPaymentUser(param);
    assertEquals(result.isConfirmed(), param.isConfirmed());
    assertEquals(result.getAmount(), param.getAmount());
    assertEquals(result.getPayment().getId(), param.getPaymentId());
    assertEquals(result.getSender().getId(), param.getSender().getId());
    assertEquals(result.getReceiver().getId(), param.getReceiver().getId());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createPaymentUser_NullExcp() {
    converter.createPaymentUser(null);
  }

  public void createUserActivity() {
    final User user = new User();
    user.setId(2);
    user.setUserName("TestUser1");
    final UserActivityDTO param = new UserActivityDTO(user, 2, "TestUser1", 2, "MyActivity", 10);
    final UserActivity result = converter.createUserActivity(param);
    assertEquals(result.getRating(), param.getRating());
    assertEquals(result.getActivity().getId(), param.getActivityId());
    assertEquals(result.getUser().getId(), user.getId());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createUserActivity_NullExcp() {
    converter.createUserActivity(null);
  }

  public void createShop() {
    final ShopDTO param = new ShopDTO();
    param.setId(2);
    param.setCountryId(2);
    param.setDeleted(true);
    param.setGroupId(45);
    param.setLogoUrl("logo.rul");
    param.setName("myShop");
    final Shop result = converter.createShop(param);
    assertEquals(result.getId(), param.getId());
    assertEquals(result.getCountry().getId(), param.getCountryId());
    assertEquals(result.isDeleted(), param.isDeleted());
    assertEquals(result.getGroup().getId(), param.getGroupId());
    assertEquals(result.getLogoUrl(), param.getLogoUrl());
    assertEquals(result.getName(), param.getName());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void createShop_NullExcp() {
    converter.createShop(null);
  }
}
