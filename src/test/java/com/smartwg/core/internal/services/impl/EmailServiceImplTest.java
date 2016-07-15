package com.smartwg.core.internal.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.RecurringDTO;
import com.smartwg.core.internal.domain.dtos.RepeatType;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.services.EmailGatewayService;
import com.smartwg.core.internal.services.EmailService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class EmailServiceImplTest {

  private static final String SUBJECT = "subject";
  private static final String CONTENT = "content";
  private static final String POSITIONS_TEMPLATE = "positions %LIST%";
  private static final Date NOW = new Date();
  private static final Date END_DATE = new DateTime(NOW).plusDays(2).toDate();
  @InjectMocks
  private EmailService service;

  @Mock
  private MessageSource messageSource;
  @Mock
  private Properties properties;
  @Mock
  private EmailGatewayService emailGatewayService;


  @BeforeMethod
  public void setUp() {
    service = new EmailServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createAndSendShoppingListDoneEmailWithoutPositions() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final ShoppingListDTO shoppingListDTO = createShoppingList();

    when(
        messageSource.getMessage(eq("email.shoppinglist.done.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.shoppinglist.done.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);
    when(properties.getProperty("email.list.template")).thenReturn(POSITIONS_TEMPLATE);

    service.createAndSendShoppingListDoneEmail(shoppingListDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "positions "), eq(recipients));
  }

  public void createAndSendShoppingListDoneEmailWithDonePositions() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final ShoppingListDTO shoppingListDTO = createShoppingList();
    shoppingListDTO.getListItemDTOs().add(new ListItemDTO(null, "Cola", null, 1, true));

    when(
        messageSource.getMessage(eq("email.shoppinglist.done.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.shoppinglist.done.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);
    when(properties.getProperty("email.list.template")).thenReturn(POSITIONS_TEMPLATE);
    when(properties.getProperty("email.list.item.done.template")).thenReturn("-x- %LIST_ITEM%");

    service.createAndSendShoppingListDoneEmail(shoppingListDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "positions -x- Cola 1"), eq(recipients));
  }

  public void createAndSendShoppingListDoneEmailWithoutDonePositions() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final ShoppingListDTO shoppingListDTO = createShoppingList();
    shoppingListDTO.getListItemDTOs().add(new ListItemDTO(null, "Cola", null, 1, false));

    when(
        messageSource.getMessage(eq("email.shoppinglist.done.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.shoppinglist.done.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);
    when(properties.getProperty("email.list.template")).thenReturn(POSITIONS_TEMPLATE);
    when(properties.getProperty("email.list.item.template")).thenReturn("- %LIST_ITEM%");

    service.createAndSendShoppingListDoneEmail(shoppingListDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "positions - Cola 1"), eq(recipients));
  }

  public void createAndSendShoppingListReleasedEmail() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final ShoppingListDTO shoppingListDTO = createShoppingList();

    when(
        messageSource.getMessage(eq("email.shoppinglist.released.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.shoppinglist.released.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);
    when(properties.getProperty("email.list.template")).thenReturn(POSITIONS_TEMPLATE);

    service.createAndSendShoppingListReleasedEmail(shoppingListDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "positions "), eq(recipients));
  }

  public void createAndSendPaswortResetEmail() throws MessagingException {
    final UserDTO userDTO = new UserDTO();
    userDTO.setEmail("sierzant.kamil@gmail.com");
    final Set<String> recipients = new HashSet<String>() {
      {
        add("sierzant.kamil@gmail.com");
      }
    };

    when(messageSource.getMessage(eq("email.passreset.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.passreset.subject.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);

    service.createAndSendPasswordResetEmail(userDTO, "pageLink");

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT), eq(CONTENT),
        eq(recipients));
  }

  public void createAndSendActivityCreatedEmail() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final ActivityDTO activityDTO = createActivity();

    when(
        messageSource.getMessage(eq("email.activity.created.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.activity.created.content.header"), any(),
            any(Locale.class))).thenReturn(CONTENT);
    when(
        messageSource.getMessage(eq("email.activity.created.content.name"), any(),
            any(Locale.class))).thenReturn("Party");
    when(
        messageSource.getMessage(eq("email.activity.created.content.points"), any(),
            any(Locale.class))).thenReturn("1");
    when(
        messageSource.getMessage(eq("email.activity.created.content.deadline"), any(),
            any(Locale.class))).thenReturn("31.06.2015");
    when(
        messageSource.getMessage(eq("email.activity.created.content.assignedTo"), any(),
            any(Locale.class))).thenReturn("Kamil");
    when(properties.getProperty("email.list.template")).thenReturn("%LIST%");

    service.createAndSendActivityCreatedEmail(activityDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "Party<br>1<br>31.06.2015<br>Kamil<br>"), eq(recipients));
  }

  @Test(dataProvider = "billData")
  public void createAndSendBillCreatedEmailForRecurringBillWithoutPositions(
      final RepeatType repeatType, final String asd) throws MessagingException {
    final Set<String> recipients = getRecipients();
    final BillDTO billDTO = createBillDTO();
    billDTO.setRecurringBill(Boolean.TRUE);
    billDTO.setRecurring(createRecurring(repeatType));

    when(messageSource.getMessage(eq("email.bill.created.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.bill.created.content.header"), any(), any(Locale.class)))
        .thenReturn(CONTENT);
    when(messageSource.getMessage(eq("email.bill.created.content.name"), any(), any(Locale.class)))
        .thenReturn("Hofer April");
    when(messageSource.getMessage(eq("email.bill.created.content.date"), any(), any(Locale.class)))
        .thenReturn("31.06.2015");
    when(messageSource.getMessage(eq("email.bill.created.content.shop"), any(), any(Locale.class)))
        .thenReturn("Hofer");
    when(messageSource.getMessage(eq("email.bill.created.content.total"), any(), any(Locale.class)))
        .thenReturn("10");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurring"), any(),
            any(Locale.class))).thenReturn("Recurring");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurringStartDate"), any(),
            any(Locale.class))).thenReturn("01.07.2015");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurringRepeatPeriod"), any(),
            any(Locale.class))).thenReturn("3");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurringRepeatTime"), any(),
            any(Locale.class))).thenReturn("10");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurringIteration"), any(),
            any(Locale.class))).thenReturn("30.12.2015");
    when(
        messageSource.getMessage(eq("email.bill.created.content.bill.positions"), any(),
            any(Locale.class))).thenReturn("Positions");
    when(properties.getProperty("email.list.template")).thenReturn("%LIST%");

    service.createAndSendBillCreatedEmail(billDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(
        eq(SUBJECT),
        eq(CONTENT
            + "Hofer April<br>31.06.2015<br>Hofer<br>10<br><br>Recurring01.07.2015<br>3<br>10<br>"
            + asd + "<br>Positions"), eq(recipients));
  }

  public void createAndSendBillCreatedEmailForNotRecurringBillWithoutPositions()
      throws MessagingException {
    final Set<String> recipients = getRecipients();
    final BillDTO billDTO = createBillDTO();
    billDTO.addCostEntry(new CostEntryDTO(null, "Cola", BigDecimal.ONE, 2, false, 1, "FOOD", false,
        true));
    billDTO.setRecurringBill(Boolean.FALSE);

    when(messageSource.getMessage(eq("email.bill.created.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.bill.created.content.header"), any(), any(Locale.class)))
        .thenReturn(CONTENT);
    when(messageSource.getMessage(eq("email.bill.created.content.name"), any(), any(Locale.class)))
        .thenReturn("Hofer April");
    when(messageSource.getMessage(eq("email.bill.created.content.date"), any(), any(Locale.class)))
        .thenReturn("31.06.2015");
    when(messageSource.getMessage(eq("email.bill.created.content.shop"), any(), any(Locale.class)))
        .thenReturn("Hofer");
    when(messageSource.getMessage(eq("email.bill.created.content.total"), any(), any(Locale.class)))
        .thenReturn("10");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurring"), any(),
            any(Locale.class))).thenReturn("Recurring");
    when(
        messageSource.getMessage(eq("email.bill.created.content.bill.positions"), any(),
            any(Locale.class))).thenReturn("Positions");
    when(messageSource.getMessage(eq("email.pieces"), any(), any(Locale.class))).thenReturn("Stk.");
    when(properties.getProperty("email.list.template")).thenReturn("%LIST%");
    when(properties.getProperty("email.list.item.template")).thenReturn(" - %LIST_ITEM%");

    service.createAndSendBillCreatedEmail(billDTO, recipients);

    verify(emailGatewayService, times(1))
        .generateAndSendEmail(
            eq(SUBJECT),
            eq(CONTENT
                + "Hofer April<br>31.06.2015<br>Hofer<br>10<br>Positions - Cola: 2 Stk. - 1 EUR - Food"),
            eq(recipients));
  }

  public void createAndSendBillCreatedEmailForNotRecurringBillWithPositions()
      throws MessagingException {
    final Set<String> recipients = getRecipients();
    final BillDTO billDTO = createBillDTO();
    billDTO.setRecurringBill(Boolean.FALSE);

    when(messageSource.getMessage(eq("email.bill.created.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.bill.created.content.header"), any(), any(Locale.class)))
        .thenReturn(CONTENT);
    when(messageSource.getMessage(eq("email.bill.created.content.name"), any(), any(Locale.class)))
        .thenReturn("Hofer April");
    when(messageSource.getMessage(eq("email.bill.created.content.date"), any(), any(Locale.class)))
        .thenReturn("31.06.2015");
    when(messageSource.getMessage(eq("email.bill.created.content.shop"), any(), any(Locale.class)))
        .thenReturn("Hofer");
    when(messageSource.getMessage(eq("email.bill.created.content.total"), any(), any(Locale.class)))
        .thenReturn("10");
    when(
        messageSource.getMessage(eq("email.bill.created.content.recurring"), any(),
            any(Locale.class))).thenReturn("Recurring");
    when(
        messageSource.getMessage(eq("email.bill.created.content.bill.positions"), any(),
            any(Locale.class))).thenReturn("Positions");
    when(properties.getProperty("email.list.template")).thenReturn("%LIST%");

    service.createAndSendBillCreatedEmail(billDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq(CONTENT + "Hofer April<br>31.06.2015<br>Hofer<br>10<br>Positions"), eq(recipients));
  }

  @DataProvider(name = "billData")
  private Object[][] getBillDate() {
    return new Object[][] {
        {RepeatType.FOREVER, "for ever"},
        {RepeatType.TIMES, "30.12.2015"},
        {
            RepeatType.UNTIL,
            RepeatType.UNTIL.getName() + " "
                + new DateTime(NOW).plusDays(2).withHourOfDay(12).toDate().toString()},};
  }

  private RecurringDTO createRecurring(final RepeatType repeatType) {
    final RecurringDTO recurringDTO = new RecurringDTO();
    recurringDTO.setRepeatType(repeatType);
    recurringDTO.setDate(NOW);
    recurringDTO.setEndDate(END_DATE);
    recurringDTO.setRecurringType(RecurringType.MONTH);
    recurringDTO.setValue(3);
    recurringDTO.setTimes(10);
    return recurringDTO;
  }

  private BillDTO createBillDTO() {
    final BillDTO billDTO = new BillDTO();
    final ShopDTO shopDTO = new ShopDTO();
    shopDTO.setName("Hofer");
    billDTO.setCreatedByUsername("Kamil");
    billDTO.setName("Hofer April");
    billDTO.setDate(NOW);
    billDTO.setShopDTO(shopDTO);
    billDTO.setTotal(BigDecimal.TEN);
    billDTO.setCurrency(new CurrencyDTO(1, "EUR"));
    return billDTO;
  }

  public void createAndSendRegistrationConfirmationEmail() throws MessagingException {
    final UserDTO userDTO = new UserDTO();
    userDTO.setEmail("sierzant.kamil@gmail.com");
    final Set<String> recipients = new HashSet<String>() {
      {
        add("sierzant.kamil@gmail.com");
      }
    };

    when(
        messageSource.getMessage(eq("email.registration.confirmation.subject.title"), any(),
            any(Locale.class))).thenReturn(SUBJECT);
    when(
        messageSource.getMessage(eq("email.registration.confirmation.subject.content.salutation"),
            any(), any(Locale.class))).thenReturn("Herr");
    when(
        messageSource.getMessage(eq("email.registration.confirmation.subject.content.header"),
            any(), any(Locale.class))).thenReturn("taralala");
    when(messageSource.getMessage(eq("email.default.ending"), any(), any(Locale.class)))
        .thenReturn("Kamil");

    service.createAndSendRegistrationConfirmationEmail(userDTO, "PageLink");

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq("Herr<br><br>taralala" + "<br><br>Kamil"), eq(recipients));
  }

  public void createAndSendJoinRequestMail() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final UserDTO userDTO = new UserDTO();

    when(messageSource.getMessage(eq("join.group.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(messageSource.getMessage(eq("email.default.salutation"), any(), any(Locale.class)))
        .thenReturn("Herr");
    when(messageSource.getMessage(eq("join.group.content"), any(), any(Locale.class))).thenReturn(
        "taralala");
    when(messageSource.getMessage(eq("join.group.content.ending"), any(), any(Locale.class)))
        .thenReturn("Kamil");

    service.createAndSendJoinRequestMail(userDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq("Herr<br><br>taralala" + "<br><br>Kamil"), eq(recipients));
  }

  public void createAndSendUserAddedToGroupInfoMail() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final UserDTO userDTO = new UserDTO();
    final GroupDTO groupDTO = new GroupDTO();

    when(messageSource.getMessage(eq("new.group.member.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(messageSource.getMessage(eq("email.default.salutation"), any(), any(Locale.class)))
        .thenReturn("Herr");
    when(messageSource.getMessage(eq("new.group.member.content"), any(), any(Locale.class)))
        .thenReturn("taralala");
    when(messageSource.getMessage(eq("email.default.ending"), any(), any(Locale.class)))
        .thenReturn("Kamil");

    service.createAndSendUserAddedToGroupInfoMail(userDTO, groupDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq("Herr<br><br>taralala" + "<br><br>Kamil"), eq(recipients));
  }

  public void createAndSendGroupDeletedInfoMail() throws MessagingException {
    final Set<String> recipients = getRecipients();
    final GroupDTO groupDTO = new GroupDTO();

    when(messageSource.getMessage(eq("group.deleted.subject.title"), any(), any(Locale.class)))
        .thenReturn(SUBJECT);
    when(messageSource.getMessage(eq("email.default.salutation"), any(), any(Locale.class)))
        .thenReturn("Herr");
    when(messageSource.getMessage(eq("group.deleted.content"), any(), any(Locale.class)))
        .thenReturn("taralala");
    when(messageSource.getMessage(eq("email.default.ending"), any(), any(Locale.class)))
        .thenReturn("Kamil");

    service.createAndSendGroupDeletedInfoMail(groupDTO, recipients);

    verify(emailGatewayService, times(1)).generateAndSendEmail(eq(SUBJECT),
        eq("Herr<br><br>taralala" + "<br><br>Kamil"), eq(recipients));
  }

  private ShoppingListDTO createShoppingList() {
    final ShoppingListDTO shoppingListDTO = new ShoppingListDTO();
    shoppingListDTO.setName("Billa");
    shoppingListDTO.setState(ShoppingListState.DONE);
    shoppingListDTO.setAssignedToUsername("Kamil");
    return shoppingListDTO;
  }

  private Set<String> getRecipients() {
    final Set<String> recipients = new HashSet<>();
    recipients.add("sierzant.kamil@gmail.com");
    recipients.add("test@test.com");
    return recipients;
  }

  private ActivityDTO createActivity() {
    final ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setName("Party");
    activityDTO.setPoints(2);
    activityDTO.setDate(NOW);
    activityDTO.setCreatedBy_Username("Michi");
    activityDTO.setAssigentTo_Username("Kamil");

    return activityDTO;
  }
}
