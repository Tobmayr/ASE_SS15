package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.NotificationType;
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
import com.smartwg.core.internal.domain.entities.Currency;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/**
 * @author Tobias Ortmayr (to),Kamil Sierzant (ks),Anna Sadriu (as)
 */
@Named
public class EntityConverterImpl implements EntityConverter {

  @Override
  public ShoppingList createUserShoppingList(final ShoppingListDTO shoppingListDTO) {
    final ShoppingList result = new ShoppingList();
    result.setId(shoppingListDTO.getId());
    result.setName(shoppingListDTO.getName());
    result.setDeadline(shoppingListDTO.getDeadline());
    result.setEmailNotification(shoppingListDTO.isEmailNotification());
    result.setPrivateList(shoppingListDTO.isPrivateList());
    result.setState(shoppingListDTO.getState());
    if (shoppingListDTO.getActivityId() != null) {
      Activity activity = new Activity();
      activity.setId(shoppingListDTO.getActivityId());
      result.setActivity(activity);
    }

    for (ListItemDTO listItemDTO : shoppingListDTO.getListItemDTOs()) {
      final ListPosition listPosition = createListPosition(listItemDTO);
      listPosition.setShoppingList(result);
      result.addListPosition(listPosition);
    }

    return result;
  }

  @Override
  public ListPosition createListPosition(final ListItemDTO listItemDTO) {
    final ListPosition listPosition = new ListPosition();
    listPosition.setName(listItemDTO.getName());
    Category category = new Category();
    category.setId(listItemDTO.getCategory().getId());
    listPosition.setCategory(category);
    listPosition.setAmount(listItemDTO.getAmount());
    return listPosition;
  }

  @Override
  public Bill createBill(final BillDTO billDTO) {
    final Bill result = new Bill();
    result.setId(billDTO.getId());
    result.setName(billDTO.getName());
    result.setTotal(billDTO.getTotal());
    result.setDate(billDTO.getDate());
    result.setPrivateBill(billDTO.isPrivateBill());
    if (billDTO.getCurrency() != null) {
      Currency currency = new Currency();
      currency.setId(1);
      result.setCurrency(currency);
    }
    if (billDTO.getCreatedBy_id() != null) {
      User u = new User();
      u.setId(billDTO.getCreatedBy_id());
      result.setCreatedBy(u);
    }
    if (billDTO.getRecurring() != null) {
      result.setRecurring(createRecurring(billDTO.getRecurring()));
    }

    if (billDTO.getShopDTO() != null) {
      Shop shop = new Shop();
      shop.setId(billDTO.getShopDTO().getId());
      result.setShop(shop);
    }
    if (billDTO.getResource() != null) {
      result.setResource(createResource(billDTO.getResource()));
    }

    if (billDTO.getGroupId() != null) {
      Group g = new Group();
      g.setId(billDTO.getGroupId());
      result.setGroup(g);
    }
    for (CostEntryDTO costDTO : billDTO.getCostEntries()) {
      final CostEntry costEntry = createCostEntry(costDTO);
      costEntry.setBill(result);
      result.addCostEntry(costEntry);
    }

    if (billDTO.getPaymendId() != null) {
      Payment p = new Payment();
      p.setId(billDTO.getPaymendId());
      result.setPayment(p);
    }

    return result;
  }


  @Override
  public CostEntry createCostEntry(final CostEntryDTO costEntryDTO) {
    final CostEntry result = new CostEntry();
    result.setId(costEntryDTO.getId());
    result.setName(costEntryDTO.getName());
    result.setAmount(costEntryDTO.getAmount());
    result.setTimes(costEntryDTO.getTimes());
    result.setExcluded(costEntryDTO.isExcluded());
    if (costEntryDTO.getCategory() != null) {
      Category category = new Category();
      category.setId(costEntryDTO.getCategory().getId());
      result.setCategory(category);
    }

    return result;
  }

  @Override
  public Recurring createRecurring(final RecurringDTO recurringDTO) {
    final Recurring recurring = new Recurring();
    recurring.setId(recurringDTO.getId());
    recurring.setValue(recurringDTO.getValue());
    recurring.setDate(recurringDTO.getDate());
    recurring.setEndDate(recurringDTO.getEndDate());
    recurring.setTimes(recurringDTO.getTimes());
    recurring.setRecurringType(recurringDTO.getRecurringType());
    recurring.setRepeatType(recurringDTO.getRepeatType());
    return recurring;
  }

  @Override
  public Resource createResource(final ResourceDTO resourceDTO) {
    final Resource result = new Resource();
    result.setId(resourceDTO.getId());
    result.setName(resourceDTO.getName());
    result.setType(resourceDTO.getType());
    result.setContent(resourceDTO.getContent());
    result.setFileUrl(resourceDTO.getFileURL());
    return result;
  }

  @Override
  public User createUser(final UserDTO userDTO) {
    final User user = new User();
    user.setId(userDTO.getId());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setUserName(userDTO.getUserName());
    user.setPassword(userDTO.getPassword());
    user.setSalt(userDTO.getSalt());
    user.setEmail(userDTO.getEmail());
    user.setBirthDate(userDTO.getBirthDate());
    user.setIncome(userDTO.getIncome());
    user.setNotify(convertToString(userDTO.getNotificationTypes()));
    user.setConfirmCode(userDTO.getConfirmCode());
    user.setConfirmed(userDTO.isConfirmed());
    user.setLocale(userDTO.getLocale());

    return user;
  }

  private String convertToString(final List<NotificationType> notificationTypes) {
    String result = StringUtils.EMPTY;
    for (NotificationType notificationType : notificationTypes) {
      result += ";" + notificationType;
    }
    return result.replaceFirst(";", "");
  }

  @Override
  public UserGroup createUserGroup(final UserGroupDTO dto) {
    final UserGroup userGroup = new UserGroup();
    Group group = new Group();
    group.setId(dto.getGroupId());
    userGroup.setGroup(group);
    User user = new User();
    user.setId(dto.getUserId());
    userGroup.setUser(user);
    userGroup.setRole(dto.getRole());
    userGroup.setBalance(dto.getBalance());
    userGroup.setJoinDate(dto.getJoinDate());
    userGroup.setLeaveDate(dto.getLeaveDate());
    return userGroup;
  }

  @Override
  public UserActivity createUserActivity(final UserActivityDTO dto) {
    final UserActivity userActivity = new UserActivity();
    Activity activity = new Activity();
    activity.setId(dto.getActivityId());
    userActivity.setActivity(activity);
    User user = new User();
    user.setId(dto.getUserId());
    userActivity.setUser(user);
    userActivity.setRating(dto.getRating());
    return userActivity;
  }

  @Override
  public Group createGroup(final GroupDTO dto) {
    final Group group = new Group();
    group.setId(dto.getId());
    group.setName(dto.getName());
    group.setCity(dto.getCity());
    group.setZip(dto.getZip());
    group.setStreet(dto.getStreet());
    group.setStreet2(dto.getStreet2());
    if (dto.getCountry() != null) {
      final Country country = new Country();
      country.setId(dto.getCountry().getId());
      group.setCountry(country);
    }
    return group;
  }

  @Override
  public Activity createActivity(final ActivityDTO dto) {
    final Activity activity = new Activity();
    activity.setId(dto.getId());
    activity.setName(dto.getName());
    activity.setDate(dto.getDate());
    activity.setPoints(dto.getPoints());
    activity.setRating(dto.getRating());
    activity.setIsDone(dto.getIsDone());
    final User createdBy = new User();

    if (dto.getCreatedByUserId() != null) {
      createdBy.setId(dto.getCreatedByUserId());
      activity.setCreatedBy(createdBy);
    }
    if (dto.getAssignedToUserId() != null && dto.getAssignedToUserId() != 0) {
      final User assignedTo = new User();
      assignedTo.setId(dto.getAssignedToUserId());
      activity.setAssignedTo(assignedTo);
    }
    if (dto.getGroupId() != null) {
      final Group group = new Group();
      group.setId(dto.getGroupId());
      activity.setGroup(group);
    }

    if (dto.getRecurring() != null) {
      activity.setRecurring(createRecurring(dto.getRecurring()));
    }

    // for (UserActivityDTO uaDTO : dto.getRatings()) {
    // activity.addUserActivity(createUserActivity(uaDTO));
    // }

    return activity;
  }

  @Override
  public Absence createAbsence(final AbsenceDTO absenceDTO) {
    final Absence result = new Absence();
    result.setId(absenceDTO.getId());
    result.setAwayFrom(absenceDTO.getAwayFrom());
    result.setAwayTill(absenceDTO.getAwayTill());
    result.setTemporary(absenceDTO.isTemporary());
    if (absenceDTO.getUserGroup() != null) {
      result.setUserGroup(createUserGroup(absenceDTO.getUserGroup()));
    }
    return result;
  }

  @Override
  public Category createCategory(CategoryDTO dto) {
    final Category category = new Category();
    category.setId(dto.getId());
    category.setName(dto.getName());
    category.setDefaultCategory(dto.isDefaultCategory());
    category.setFixedCost(dto.isFixedCost());
    category.setDeleted(dto.isDeleted());
    if (dto.getGroup_id() != null) {
      Group g = new Group();
      g.setId(dto.getGroup_id());
      category.setGroup(g);
    }

    if (dto.getParentCategory() != null) {
      Category c = new Category();
      c.setId(dto.getParentCategory().getId());
      category.setParentCategory(c);
    }
    if (dto.getUserGroupCategories() != null) {
      List<UserGroupCategory> ugcList = new ArrayList<UserGroupCategory>();
      for (UserGroupCategoryDTO u : dto.getUserGroupCategories()) {
        ugcList.add(this.createUserGroupCategory(u));
      }
      category.setUserGroupCategories(ugcList);
    }
    return category;
  }

  @Override
  public UserGroupCategory createUserGroupCategory(UserGroupCategoryDTO dto) {
    UserGroupCategory ugc = new UserGroupCategory();
    Category category = new Category();
    category.setId(dto.getCategory().getId());
    ugc.setCategory(category);
    ugc.setPercent(dto.getPercent());
    UserGroup userGroup = createUserGroup(dto.getUserGroupDTO());
    ugc.setUserGroup(userGroup);
    return ugc;

  }

  @Override
  public Payment createPayment(final PaymentDTO paymentDTO) {
    final Payment result = new Payment();
    result.setId(paymentDTO.getId());
    result.setDate(paymentDTO.getDate());
    result.setPaymentLog(paymentDTO.getPaymentLog());

    if (paymentDTO.getUserPayments() != null) {
      for (PaymentUserDTO puDTO : paymentDTO.getUserPayments()) {
        final PaymentUser pu = createPaymentUser(puDTO);
        pu.setPayment(result);
        result.addPaymentUser(pu);
      }
    }
    return result;
  }

  @Override
  public PaymentUser createPaymentUser(final PaymentUserDTO paymentUserDTO) {
    final PaymentUser result = new PaymentUser();
    result.setAmount(paymentUserDTO.getAmount());
    result.setConfirmed(paymentUserDTO.isConfirmed());

    if (paymentUserDTO.getPaymentId() != null) {
      final Payment payment = new Payment();
      payment.setId(paymentUserDTO.getPaymentId());
      result.setPayment(payment);
    }

    if (paymentUserDTO.getReceiver() != null && paymentUserDTO.getReceiver().getId() != null) {
      final User user = new User();
      user.setId(paymentUserDTO.getReceiver().getId());
      result.setReceiver(user);
    }

    if (paymentUserDTO.getSender() != null && paymentUserDTO.getSender().getId() != null) {
      final User user = new User();
      user.setId(paymentUserDTO.getSender().getId());
      result.setSender(user);
    }
    return result;
  }

  @Override
  public Shop createShop(ShopDTO shopDTO) {
    Shop result = new Shop();
    result.setId(shopDTO.getId());
    result.setLogoUrl(shopDTO.getLogoUrl());
    result.setName(shopDTO.getName());
    result.setBills(shopDTO.getBills());
    result.setDeleted(shopDTO.isDeleted());
    if (shopDTO.getGroupId() != null) {
      Group g = new Group();
      g.setId(shopDTO.getGroupId());
      result.setGroup(g);
    }
    if (shopDTO.getCountryId() != null) {
      Country c = new Country();
      c.setId(shopDTO.getCountryId());
      result.setCountry(c);
    }
    return result;
  }
}
