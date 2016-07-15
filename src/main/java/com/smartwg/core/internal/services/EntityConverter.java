package com.smartwg.core.internal.services;

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

/**
 * This interfaces provides the methods necessary for converting DataTransferObjects into their
 * respective entity representation. Passing a null parameter to one of the methods will cause a
 * NullPointerException to be thrown
 * 
 * @author Tobias Ortmayr (to),Kamil Sierzant (ks), Anna Sadriu (as)
 * 
 */
public interface EntityConverter {
  /**
   * Converts a ShoppinglistDTO into a ShoppingList with equal attributes. Internally the method
   * uses implementations of createListPosition()
   * 
   * @param shoppingListDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  ShoppingList createUserShoppingList(ShoppingListDTO shoppingListDTO);

  /**
   * Converts a ListPositionDTO into a ListPositon with equal attributes
   * 
   * @param listItemDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  ListPosition createListPosition(ListItemDTO listItemDTO);

  /**
   * Converts a AbsenceDTO into a Absence with equal attributes.
   * 
   * @param absenceDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Absence createAbsence(AbsenceDTO absenceDTO);

  /**
   * Converts a BillDTO into a Bill with equal attributes.Internally the methods uses
   * implementations of createCostEntry() and createPayments() to convert the Entity collection
   * 
   * @param billDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Bill createBill(BillDTO billDTO);

  /**
   * Converts a CostEntryDTO into a CostEntry with equal attributes
   * 
   * @param costEntryDTO
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  CostEntry createCostEntry(CostEntryDTO costEntryDTO);

  /**
   * Converts a RecurringDTO into a Recurring with equal attributes
   * 
   * @param recurringDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Recurring createRecurring(RecurringDTO recurringDTO);

  /**
   * Converts a ResourceDTO into a Resource with equal attributes
   * 
   * @param resourceDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Resource createResource(ResourceDTO resourceDTO);

  /**
   * Converts a UserDTO into a User with equal attributes
   * 
   * @param userDto dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  User createUser(UserDTO userDto);

  /**
   * Converts a UserDTO into a UserGroup with equal attributes
   * 
   * @param userGroupDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  UserGroup createUserGroup(UserGroupDTO userGroupDTO);

  /**
   * Converts a GroupDTO into a Group with equal attributes
   * 
   * @param groupDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Group createGroup(GroupDTO groupDTO);

  /**
   * Converts a GrouActivityDTOpDTO into a Activity with equal attributes
   * 
   * @param activityDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Activity createActivity(ActivityDTO activityDTO);

  /**
   * Converts a CategoryDTO into a Category with equal attributes
   * 
   * @param catgegoryDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Category createCategory(CategoryDTO catgegoryDTO);

  /**
   * Converts a UserGroupCategoryDTO into a UserGroupCategory with equal attributes
   * 
   * @param ugcDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  UserGroupCategory createUserGroupCategory(UserGroupCategoryDTO ugcDTO);

  /**
   * Converts a PaymentDTO into a Payment with equal attributes
   * 
   * @param paymentDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Payment createPayment(PaymentDTO paymentDTO);

  /**
   * Converts a PaymentUserDTO into a PaymentUser with equal attributes
   * 
   * @param paymentUserDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  PaymentUser createPaymentUser(PaymentUserDTO paymentUserDTO);

  /**
   * Converts a UserActivityDTO into a UserActivity with equal attributes
   * 
   * @param userActivityDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  UserActivity createUserActivity(UserActivityDTO dto);

  /**
   * Converts a ShopDTO into a Shop with equal attributes
   * 
   * @param shopDTO dto to convert
   * @return respective Entity representation
   * @throws NullPointerExcpetion if the passed parameter is null
   */
  Shop createShop(ShopDTO shopDTO);

}
