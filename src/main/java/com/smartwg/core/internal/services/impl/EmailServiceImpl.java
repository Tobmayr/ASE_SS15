package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.RecurringDTO;
import com.smartwg.core.internal.domain.dtos.RepeatType;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.services.EmailGatewayService;
import com.smartwg.core.internal.services.EmailService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

/**
 * {@inheritDoc}
 */
@Named
public class EmailServiceImpl implements EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
  private static final String NEW_LINE = "<br>";

  private static final String EMAIL_LIST_TEMPLATE = "email.list.template";
  private static final String EMAIL_LIST_ITEM_TEMPLATE = "email.list.item.template";
  private static final String EMAIL_LIST_ITEM_DONE_TEMPLATE = "email.list.item.done.template";
  private static final String PIECES = "email.pieces";
  private static final String SHOPPINGLIST_DONE_SUBJECT = "email.shoppinglist.done.subject.title";
  private static final String SHOPPINGLIST_DONE_CONTENT = "email.shoppinglist.done.content.header";
  private static final String SHOPPINGLIST_RELEASED_SUBJECT =
      "email.shoppinglist.released.subject.title";
  private static final String SHOPPINGLIST_RELEASED_CONTENT =
      "email.shoppinglist.released.content.header";
  private static final String PASS_RESET_SUBJECT = "email.passreset.subject.title";
  private static final String PASS_RESET_CONTENT = "email.passreset.subject.content.header";
  private static final String ACTIVITY_CREATED_CONTENT_NAME = "email.activity.created.content.name";
  private static final String ACTIVITY_CREATED_CONTENT_POINTS =
      "email.activity.created.content.points";
  private static final String ACTIVITY_CREATED_CONTENT_DEADLINE =
      "email.activity.created.content.deadline";
  private static final String ACTIVITY_CREATED_CONTENT_ASSIGNED_TO =
      "email.activity.created.content.assignedTo";
  private static final String ACTIVITY_CREATED_SUBJECT = "email.activity.created.subject.title";
  private static final String ACTIVITY_CREATED_CONTENT = "email.activity.created.content.header";
  private static final String BILL_CREATED_SUBJECT = "email.bill.created.subject.title";
  private static final String BILL_CREATED_CONTENT = "email.bill.created.content.header";
  private static final String BILL_CREATED_CONTENT_NAME = "email.bill.created.content.name";
  private static final String BILL_CREATED_CONTENT_DATE = "email.bill.created.content.date";
  private static final String BILL_CREATED_CONTENT_SHOP = "email.bill.created.content.shop";
  private static final String BILL_CREATED_CONTENT_TOTAL = "email.bill.created.content.total";
  private static final String BILL_CREATED_CONTENT_RECURRING =
      "email.bill.created.content.recurring";
  private static final String BILL_CREATED_CONTENT_RECURRING_START_DATE =
      "email.bill.created.content.recurringStartDate";
  private static final String BILL_CREATED_CONTENT_RECURRING_REPEAT_PERIOD =
      "email.bill.created.content.recurringRepeatPeriod";
  private static final String BILL_CREATED_CONTENT_RECURRING_REPEAT_TIME =
      "email.bill.created.content.recurringRepeatTime";
  private static final String BILL_CREATED_CONTENT_RECURRING_ITERATION =
      "email.bill.created.content.recurringIteration";
  private static final String BILL_CREATED_CONTENT_BILL_POSITIONS =
      "email.bill.created.content.bill.positions";
  private static final String REGISTRATION_CONFIRMATION_SUBJECT =
      "email.registration.confirmation.subject.title";
  private static final String REGISTRATION_CONFIRMATION_SUBJECT_CONTENT_SALUTATION =
      "email.registration.confirmation.subject.content.salutation";
  private static final String REGISTRATION_CONFIRMATION_SUBJECT_CONTENT =
      "email.registration.confirmation.subject.content.header";
  private static final String JOIN_GROUP_SUBJECT = "join.group.subject.title";
  private static final String JOIN_GROUP_CONTENT = "join.group.content";
  private static final String JOIN_GROUP_CONTENT_ENDING = "join.group.content.ending";
  private static final String GROUP_DELETED_SUBJECT = "group.deleted.subject.title";
  private static final String GROUP_DELETED_CONTENT = "group.deleted.content";
  private static final String NEW_GROUP_MEMBER_SUBJECT = "new.group.member.subject.title";
  private static final String NEW_GROUP_MEMBER_CONTENT = "new.group.member.content";
  private static final String DEFAULT_SALUTATION = "email.default.salutation";
  private static final String DEFAULT_ENDING = "email.default.ending";

  @Inject
  private MessageSource messageSource;
  @Inject
  private Properties properties;
  @Inject
  private EmailGatewayService emailGatewayService;

  @Override
  public void createAndSendShoppingListDoneEmail(final ShoppingListDTO shoppingList,
      final Set<String> recipients) {
    final String subject =
        getMessage(SHOPPINGLIST_DONE_SUBJECT, shoppingList.getName(), shoppingList.getState()
            .getName());
    final String contentHeader =
        getMessage(SHOPPINGLIST_DONE_CONTENT, shoppingList.getName(),
            shoppingList.getAssignedToUsername(), shoppingList.getState().getName());

    final String content = contentHeader + getShoppingListPositions(shoppingList);
    try {
      emailGatewayService.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to completed shopping list could not be sent!");
    }
  }

  @Override
  public void createAndSendShoppingListReleasedEmail(final ShoppingListDTO shoppingList,
      final Set<String> recipients) {
    final String subject = getMessage(SHOPPINGLIST_RELEASED_SUBJECT);
    final String contentHeader =
        getMessage(SHOPPINGLIST_RELEASED_CONTENT, shoppingList.getName(),
            shoppingList.getCreatedByUsername());

    final String content = contentHeader + getShoppingListPositions(shoppingList);
    try {
      emailGatewayService.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to released shopping list could not be sent!");
    }
  }

  private String getShoppingListPositions(final ShoppingListDTO shoppingList) {

    final String positionList = properties.getProperty(EMAIL_LIST_TEMPLATE);
    final String listPosition = properties.getProperty(EMAIL_LIST_ITEM_TEMPLATE);
    final String doneListPosition = properties.getProperty(EMAIL_LIST_ITEM_DONE_TEMPLATE);

    String result = StringUtils.EMPTY;
    for (final ListItemDTO listItemDTO : shoppingList.getListItemDTOs()) {
      final String name = listItemDTO.getName();
      final int amount = listItemDTO.getAmount();
      result +=
          (listItemDTO.isDone() ? doneListPosition.replace("%LIST_ITEM%", name + " " + amount)
              : listPosition.replace("%LIST_ITEM%", name + " " + amount));
    }

    return positionList.replace("%LIST%", result);
  }

  @Override
  public void createAndSendPasswordResetEmail(final UserDTO user, final String pageLink) {
    final String subject = getMessage(PASS_RESET_SUBJECT, user.getUserName());
    final String content = getMessage(PASS_RESET_CONTENT, user.getUserName(), pageLink);

    HashSet<String> recipients = new HashSet<>();
    recipients.add(user.getEmail());
    try {
      emailGatewayService.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to a resetPassword request could not be sent!");
    }
  }

  public void createAndSendActivityCreatedEmail(final ActivityDTO activityDTO,
      final Set<String> recipients) {
    final String subject = getMessage(ACTIVITY_CREATED_SUBJECT);
    final String contentHeader =
        getMessage(ACTIVITY_CREATED_CONTENT, activityDTO.getCreatedBy_Username());
    final String detailList = properties.getProperty(EMAIL_LIST_TEMPLATE);

    String result = StringUtils.EMPTY;

    result += getMessage(ACTIVITY_CREATED_CONTENT_NAME, activityDTO.getName()) + NEW_LINE;
    result += getMessage(ACTIVITY_CREATED_CONTENT_POINTS, activityDTO.getPoints()) + NEW_LINE;
    result += getMessage(ACTIVITY_CREATED_CONTENT_DEADLINE, activityDTO.getDate()) + NEW_LINE;
    result +=
        getMessage(ACTIVITY_CREATED_CONTENT_ASSIGNED_TO, activityDTO.getAssigentTo_Username())
            + NEW_LINE;

    final String content = contentHeader + detailList.replace("%LIST%", result);

    try {
      emailGatewayService.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to created activity could not be sent!");
    }
  }

  public void createAndSendBillCreatedEmail(final BillDTO billDTO, final Set<String> recipients) {
    final String subject = getMessage(BILL_CREATED_SUBJECT);
    final String contentHeader = getMessage(BILL_CREATED_CONTENT, billDTO.getCreatedByUsername());
    final String detailList = properties.getProperty(EMAIL_LIST_TEMPLATE);

    String result = StringUtils.EMPTY;

    result += getMessage(BILL_CREATED_CONTENT_NAME, billDTO.getName()) + NEW_LINE;
    result += getMessage(BILL_CREATED_CONTENT_DATE, billDTO.getDate()) + NEW_LINE;
    if (billDTO.getShopDTO() != null) {
      result += getMessage(BILL_CREATED_CONTENT_SHOP, billDTO.getShopDTO().getName()) + NEW_LINE;
    }
    result +=
        getMessage(BILL_CREATED_CONTENT_TOTAL, billDTO.getTotal(), billDTO.getCurrency()
            .getIsoCode())
            + NEW_LINE;

    if (billDTO.isRecurringBill()) {
      final RecurringDTO recurring = billDTO.getRecurring();
      final RepeatType repeatType = recurring.getRepeatType();
      result += NEW_LINE + getMessage(BILL_CREATED_CONTENT_RECURRING);
      result +=
          getMessage(BILL_CREATED_CONTENT_RECURRING_START_DATE, recurring.getDate()) + NEW_LINE;
      result +=
          getMessage(BILL_CREATED_CONTENT_RECURRING_REPEAT_PERIOD, recurring.getValue(), recurring
              .getRecurringType().getName())
              + NEW_LINE;
      result += getMessage(BILL_CREATED_CONTENT_RECURRING_REPEAT_TIME) + NEW_LINE;

      if (RepeatType.TIMES.equals(repeatType)) {
        result +=
            getMessage(BILL_CREATED_CONTENT_RECURRING_ITERATION, repeatType.getName(),
                recurring.getTimes())
                + NEW_LINE;
      } else if (RepeatType.UNTIL.equals(repeatType)) {
        result += repeatType.getName() + " " + recurring.getEndDate() + NEW_LINE;
      } else {
        result += repeatType.getName() + NEW_LINE;
      }
    }

    final String content =
        contentHeader + detailList.replace("%LIST%", result)
            + getBillPositions(billDTO.getCostEntries(), billDTO.getCurrency().getIsoCode());

    try {
      emailGatewayService.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to created bill could not be sent!");
    }
  }

  private String getBillPositions(final List<CostEntryDTO> costEntries, final String currency) {
    final String pieces = getMessage(PIECES);
    final String positionList = properties.getProperty(EMAIL_LIST_TEMPLATE);
    final String listPosition = properties.getProperty(EMAIL_LIST_ITEM_TEMPLATE);
    String result = getMessage(BILL_CREATED_CONTENT_BILL_POSITIONS);

    for (final CostEntryDTO costEntryDTO : costEntries) {
      final String name = costEntryDTO.getName();
      final int times = costEntryDTO.getTimes();
      final BigDecimal amount = costEntryDTO.getAmount();
      final String category = costEntryDTO.getCategory().getName();

      result +=
          listPosition.replace("%LIST_ITEM%", name + ": " + times + " " + pieces + " - " + amount
              + " " + currency + " - " + category);
    }

    return positionList.replace("%LIST%", result);
  }

  @Override
  public void createAndSendRegistrationConfirmationEmail(final UserDTO user, final String pageLink) {
    final String subject = getMessage(REGISTRATION_CONFIRMATION_SUBJECT);
    final String salutation =
        getMessage(REGISTRATION_CONFIRMATION_SUBJECT_CONTENT_SALUTATION, user.getUserName());
    final String content = getMessage(REGISTRATION_CONFIRMATION_SUBJECT_CONTENT, pageLink);
    final String ending = getMessage(DEFAULT_ENDING);
    final HashSet<String> recipients = new HashSet<>();
    recipients.add(user.getEmail());
    try {
      emailGatewayService.generateAndSendEmail(subject, salutation + NEW_LINE + NEW_LINE + content
          + NEW_LINE + NEW_LINE + ending, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to a registration confirmation could not be sent!");
    }
  }

  @Override
  public void createAndSendJoinRequestMail(final UserDTO user, final Set<String> recipients) {
    final String subject = getMessage(JOIN_GROUP_SUBJECT);
    final String salutation = getMessage(DEFAULT_SALUTATION);
    final String content = getMessage(JOIN_GROUP_CONTENT, user.getEmail());
    final String ending = getMessage(JOIN_GROUP_CONTENT_ENDING, user.getUserName());
    try {
      emailGatewayService.generateAndSendEmail(subject, salutation + NEW_LINE + NEW_LINE + content
          + NEW_LINE + NEW_LINE + ending, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to a join group request could not be sent!");
    }
  }

  @Override
  public void createAndSendUserAddedToGroupInfoMail(final UserDTO user, final GroupDTO group,
      final Set<String> recipients) {
    final String subject = getMessage(NEW_GROUP_MEMBER_SUBJECT);
    final String salutation = getMessage(DEFAULT_SALUTATION);
    final String content =
        getMessage(NEW_GROUP_MEMBER_CONTENT, user.getUserName(), group.getName());
    final String ending = getMessage(DEFAULT_ENDING);
    try {
      emailGatewayService.generateAndSendEmail(subject, salutation + NEW_LINE + NEW_LINE + content
          + NEW_LINE + NEW_LINE + ending, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to new group member could not be sent!");
    }
  }

  @Override
  public void createAndSendGroupDeletedInfoMail(final GroupDTO group, final Set<String> recipients) {
    final String subject = getMessage(GROUP_DELETED_SUBJECT);
    final String salutation = getMessage(DEFAULT_SALUTATION);
    final String content = getMessage(GROUP_DELETED_CONTENT, group.getName());
    final String ending = getMessage(DEFAULT_ENDING);
    try {
      emailGatewayService.generateAndSendEmail(subject, salutation + NEW_LINE + NEW_LINE + content
          + NEW_LINE + NEW_LINE + ending, recipients);
    } catch (MessagingException e) {
      LOGGER.warn("Email notification related to group deletion could not be sent!");
    }
  }

  private String getMessage(final String messageKey, final Object... objects) {
    final Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(messageKey, objects, locale);
  }
}
