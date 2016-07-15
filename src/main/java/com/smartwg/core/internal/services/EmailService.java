package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;

import java.util.Set;

/**
 * @author Kamil Sierzant (ks)
 */
public interface EmailService {

  /**
   * Creates and sends an email when the given shopping list has been marked as done
   *
   * @param shoppingList {@link ShoppingListDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendShoppingListDoneEmail(ShoppingListDTO shoppingList, Set<String> recipients);

  /**
   * Creates and sends an email when the given shopping list has been released
   *
   * @param shoppingList {@link ShoppingListDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendShoppingListReleasedEmail(ShoppingListDTO shoppingList, Set<String> recipients);

  /**
   * Creates and sends an email with password reset link
   *
   * @param user {@link UserDTO}
   * @param pageLink {@link String} with activation link
   */
  void createAndSendPasswordResetEmail(UserDTO user, String pageLink);

  /**
   * Creates and sends an email when the given activity has been created to all users from the group
   *
   * @param activityDTO {@link ActivityDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendActivityCreatedEmail(ActivityDTO activityDTO, Set<String> recipients);

  /**
   * Creates and sends an email when the given bill has been created to all users from the group
   *
   * @param billDTO {@link BillDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendBillCreatedEmail(final BillDTO billDTO, final Set<String> recipients);

  /**
   * Creates and sends an email with registration confirmation link
   *
   * @param user {@link UserDTO}
   * @param pageLink {@link String} with activation link
   */
  void createAndSendRegistrationConfirmationEmail(UserDTO user, String pageLink);

  /**
   * Creates and sends an email with the join request of a certain user to a group
   *
   * @param user {@link UserDTO}
   */
  void createAndSendJoinRequestMail(UserDTO user, final Set<String> recipients);

  /**
   * Creates and sends an email with an info message that the admin has added a user to a certain
   * group
   *
   * @param user {@link UserDTO}
   * @param group {@link GroupDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendUserAddedToGroupInfoMail(UserDTO user, GroupDTO group, Set<String> recipients);

  /**
   * Creates and sends an email with an info message that the admin has deleted a certain group
   *
   * @param group {@link GroupDTO}
   * @param recipients {@link Set} of {@link String} with recipient emails
   */
  void createAndSendGroupDeletedInfoMail(GroupDTO group, Set<String> recipients);
}
