package com.smartwg.core.controllers.costDivision;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.AbsenceFacade;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.PaymentFacade;
import com.smartwg.core.facades.UserGroupCategoryFacade;
import com.smartwg.core.internal.domain.Role;
import com.smartwg.core.internal.domain.dtos.AbsenceDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupCategoryDTO;
import com.smartwg.core.internal.domain.dtos.UserGroupDTO;
import com.smartwg.core.internal.services.PaymentLogConversionService;
import com.smartwg.core.util.Constants;

@Named("costDivisionBean")
@Scope("view")
public class CostDivisionBean implements Serializable {
  public class Tuple {
    private BigDecimal[] payments;
    private boolean changed;

    public Tuple(BigDecimal[] payments, boolean changed) {
      this.payments = payments;
      this.changed = changed;
    }

    public void setChanged(boolean changed) {
      this.changed = changed;
    }

    public boolean getChanged() {
      return this.changed;
    }

    public BigDecimal[] getPayments() {
      return this.payments;
    }

    public void setPayments(BigDecimal[] payments) {
      this.payments = payments;
    }
  }

  private ResourceBundle validationMessages = SmartWG.getValidationBundle();

  private DefaultDiagramModel diagramModel;
  private List<UserDTO> users;
  private PaymentDTO payment;
  private List<PaymentUserDTO> paymentUsers;
  private SortedMap<BillDTO, Tuple> costDivisionDetails;
  private List<BillDTO> bills;
  private HashMap<String, BigDecimal> paidBillAmountsOfUsers;
  private BigDecimal totalExpenses;
  private List<AbsenceDTO> absences;
  private List<PaymentUserDTO> unconfirmedPaymentUsers;
  private List<PaymentUserDTO> databasePaymentUsers;
  private List<PaymentUserDTO> databasePaymentUsersAll;
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");

  @Inject
  private NavigationBean navigationBean;
  @Inject
  private UserBean userBean;
  @Inject
  private BillFacade billFacade;
  @Inject
  private PaymentLogConversionService paymentLogConversionService;
  @Inject
  private AbsenceFacade absenceFacade;
  @Inject
  private PaymentFacade paymentFacade;
  @Inject
  private SmartWG smartWG;
  @Inject
  private UserGroupCategoryFacade userGroupCategoryFacade;

  @PostConstruct
  public void init() {
    // Init all variables necessary for payments:
    payment = new PaymentDTO();
    paymentUsers = new ArrayList<>();
    costDivisionDetails = new TreeMap<>();
    unconfirmedPaymentUsers = new ArrayList<>(); // actually not necessary because getter always
                                                 // receives current paymentUsers

    // Init FlowChart-Model
    diagramModel = new DefaultDiagramModel();
    diagramModel.setMaxConnections(-1);
    FlowChartConnector connector = new FlowChartConnector();
    connector.setPaintStyle("{strokeStyle:'#C7B097',lineWidth:3}");
    diagramModel.setDefaultConnector(connector);

    // Load all users of the current group, their bills of the current month and the absence times
    List<UserDTO> userBeanUsers = userBean.getUsersOfCurrentGroup();
    if (userBeanUsers != null) {
      users = new ArrayList<>();
    } else {
      return;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(smartWG.getCostDivisionOverviewSelectedDate());
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    bills =
        billFacade.getBillsWithCostEntriesBetweenTimespan(new DateTime(year, month, 1, 0, 0, 0, 0)
            .toDate(), new DateTime(year, month <= 11 ? month + 1 : 1, 1, 0, 0, 0, 0).toDate(),
            userBean.getCurrentUserGroup().getGroupId());
    absences = absenceFacade.getGroupAbsences(userBean.getCurrentUserGroup().getGroupId());

    // only consider users, that already joined the group before/at the selected month and did not
    // leave
    for (UserDTO user : userBeanUsers) {
      for (UserGroupDTO userGroupDTO : user.getGroups()) {
        if (userGroupDTO.getGroupId().equals(userBean.getCurrentUserGroup().getGroupId())) {
          cal.setTime(smartWG.getCostDivisionOverviewSelectedDate());
          year = cal.get(Calendar.YEAR);
          month = cal.get(Calendar.MONTH) + 1;
          cal.setTime(userGroupDTO.getJoinDate());
          int joinYear = cal.get(Calendar.YEAR);
          int joinMonth = cal.get(Calendar.MONTH) + 1;
          if (joinYear <= year && joinMonth <= month) {
            if (userGroupDTO.getLeaveDate() != null) {
              cal.setTime(userGroupDTO.getLeaveDate());
              int leaveYear = cal.get(Calendar.YEAR);
              int leaveMonth = cal.get(Calendar.MONTH) + 1;
              if (leaveYear <= year && leaveMonth < month) {
                continue;
              }
            }
            users.add(user);
          }
          break;
        }
      }
    }

    // Load possibly existing PaymentUser entries from Database
    databasePaymentUsers =
        paymentFacade.findPaymentUsersByUserId(userBean.getCurrentUser().getId());
    databasePaymentUsersAll = new ArrayList<>();

    // get all PaymentUser entries for all users of the current group (for Group Payment Overview)
    for (UserDTO user : userBeanUsers) {
      List<PaymentUserDTO> paymentUserDTOs = paymentFacade.findPaymentUsersByUserId(user.getId());
      if (paymentUserDTOs != null) {
        for (PaymentUserDTO paymentUserDTO : paymentUserDTOs) {
          boolean alreadyInList = false;
          for (PaymentUserDTO databasePaymentUserDTO : databasePaymentUsersAll) {
            if (databasePaymentUserDTO.getPaymentId().equals(paymentUserDTO.getPaymentId())
                && databasePaymentUserDTO.getReceiver().getId()
                    .equals(paymentUserDTO.getReceiver().getId())
                && databasePaymentUserDTO.getSender().getId()
                    .equals(paymentUserDTO.getSender().getId())) {
              alreadyInList = true;
              break;
            }
          }
          if (!alreadyInList) {
            databasePaymentUsersAll.add(paymentUserDTO);
          }
        }
      }
    }

    // Init variables necessary for Expenses:
    paidBillAmountsOfUsers = new HashMap<>();
    totalExpenses = BigDecimal.ZERO;
    for (UserDTO user : users) {
      paidBillAmountsOfUsers.put(user.getUserName(), BigDecimal.ZERO);
    }

    int numberOfBillsInDb = bills.size();
    // If a bill's payment-ID is not null it means that the Admin already saved a payment (including
    // paymentUsers) in the Database
    if (bills.size() > 0 && bills.get(0).getPaymendId() != null) {
      payment = paymentFacade.findById(bills.get(0).getPaymendId());

      for (BillDTO billDTO : bills) {
        if (billDTO.getPaymendId() == null) {
          // New bills got created after payment got saved => calculate cost division details for
          // this bills
          calculateCostDivisionDetails(billDTO.getId());
        }
      }

      // Load paymentLog from Database and convert to costDivisionDetails
      for (Map.Entry<Integer, BigDecimal[]> entry : paymentLogConversionService.deserialize(
          payment.getPaymentLog()).entrySet()) {
        BillDTO bill = billFacade.findById(entry.getKey());
        if (bill != null) {
          numberOfBillsInDb--;
          boolean onlyContainsZeros = true;
          for (BigDecimal costSplit : entry.getValue()) {
            if (costSplit.signum() != 0) {
              onlyContainsZeros = false;
              break;
            }
          }
          if (onlyContainsZeros
              && simpleDateFormat.format(bill.getDate()).equals(
                  simpleDateFormat.format(DateTime.now().toDate()))) {
            // Recalculate paymentLog Entries that only contains 0s (maybe a private bill changed to
            // non private, after saving payments)
            calculateCostDivisionDetails(bill.getId());
          } else {
            costDivisionDetails.put(bill, new Tuple(entry.getValue(), false));
          }
        }
      }
    } else if (bills.size() > 0) {
      numberOfBillsInDb = 0;
      calculateCostDivisionDetails();
    }

    calculatePaymentUsers();
    calculateExpenses();
    visualizeCostDivision();

    if (numberOfBillsInDb != 0 && this.isEditingAllowed()) {
      // update payment (and paymentUsers) in database (because it's possible that new bills were
      // created)
      saveButtonClicked(false);
    }
  }

  public List<PaymentUserDTO> getDatabasePaymentUsersAll() {
    return this.databasePaymentUsersAll;
  }

  private void calculateExpenses() {
    for (BillDTO bill : bills) {
      for (UserDTO user : users) {
        if (user.getId().equals(bill.getCreatedBy_id())) {
          for (CostEntryDTO costEntry : bill.getCostEntries()) {
            if (!costEntry.isExcluded()) {
              paidBillAmountsOfUsers.put(user.getUserName(),
                  paidBillAmountsOfUsers.get(user.getUserName()).add(costEntry.getAmount()));
              totalExpenses = totalExpenses.add(costEntry.getAmount());
            }
          }
          break;
        }
      }
    }
  }

  private void visualizeCostDivision() {
    // Add user nodes (with Id as data)
    for (int i = 0; i < users.size(); i++) {
      Element element = new Element(users.get(i).getId());
      element.setDraggable(false);
      element.setId(users.get(i).getUserName());
      diagramModel.addElement(element);
    }

    int a = 2;
    if (paymentUsers != null && paymentUsers.size() > 0) {
      for (PaymentUserDTO paymentUserDTO : paymentUsers) {
        Element sender = null, receiver = null;
        for (Element element : diagramModel.getElements()) {
          if (element.getData().toString().equals(paymentUserDTO.getSender().getId().toString())) {
            sender = element;
          } else if (element.getData().toString()
              .equals(paymentUserDTO.getReceiver().getId().toString())) {
            receiver = element;
          }
          if (sender != null && receiver != null) {
            EndPoint senderEndPoint = new BlankEndPoint(EndPointAnchor.CONTINUOUS);
            EndPoint receiverEndPoint = new BlankEndPoint(EndPointAnchor.CONTINUOUS);
            sender.addEndPoint(senderEndPoint);
            receiver.addEndPoint(receiverEndPoint);
            sender.setX("2em");
            sender.setY(a + "em");
            receiver.setX("20em");
            receiver.setY(a + "em");
            a += 3;
            diagramModel.connect(createConnection(senderEndPoint, receiverEndPoint, paymentUserDTO
                .getAmount().toString()
                + " "
                + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode()));
            break;
          }
        }
      }
    } else {
      diagramModel.clearElements();
      FacesMessage msg = new FacesMessage();
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
      msg.setSummary(validationMessages.getString("noPayments"));
      boolean absencesOfCurrentMonthExist = false;
      Calendar cal = Calendar.getInstance();
      cal.setTime(smartWG.getCostDivisionOverviewSelectedDate());
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
      Date firstDateOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0).toDate();
      Date lastDateOfMonth = new DateTime(year, month, lastDayOfMonth, 0, 0, 0, 0).toDate();
      for (AbsenceDTO absence : absences) {
        if (!absence.getAwayFrom().before(firstDateOfMonth)
            && !absence.getAwayTill().after(lastDateOfMonth)) {
          absencesOfCurrentMonthExist = true;
          break;
        }
      }
      if (absencesOfCurrentMonthExist) {
        if (isCurrentUserAdmin()) {
          msg.setDetail(validationMessages.getString("noPaymentsDetailAdminAbsence"));
        } else {
          msg.setDetail(validationMessages.getString("noPaymentsDetailAbsence"));
        }
      } else {
        if (isCurrentUserAdmin()) {
          msg.setDetail(validationMessages.getString("noPaymentsDetailAdmin"));
        } else {
          msg.setDetail(validationMessages.getString("noPaymentsDetail"));
        }
      }
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    List<Element> removeElements = new ArrayList<>();
    for (Element element : diagramModel.getElements()) {
      // User does not have to pay and is not a receiver => remove him from Chart
      if (element.getEndPoints().size() == 0) {
        removeElements.add(element);
        continue;
      }

      // Replace User-IDs with their usernames
      for (UserDTO user : users) {
        if (element.getData().toString().equals(user.getId().toString())) {
          element.setData(user.getUserName());
          break;
        }
      }
    }

    for (Element element : removeElements) {
      diagramModel.removeElement(element);
    }
  }

  private void calculatePaymentUsers() {
    // Initialize debts
    BigDecimal[] debts = new BigDecimal[users.size()];
    Arrays.fill(debts, BigDecimal.ZERO);

    // Prefill debts if there are already some paymentUser-Entries in Database
    for (Map.Entry<BillDTO, Tuple> entry : costDivisionDetails.entrySet()) {
      for (int i = 0; i < users.size(); i++) {
        if (i < entry.getValue().getPayments().length) {
          debts[i] = debts[i].add(entry.getValue().getPayments()[i]);
        } else {
          entry.getValue().setChanged(true);
        }
      }
    }
    BigDecimal[] tempArray = debts.clone(); // Just for debugging.. actually cloning is not
                                            // necessary
    while (!paymentCalculationFinished(tempArray)) {
      for (int i = 0; i < tempArray.length; i++) {
        if (tempArray[i].signum() < 0) {
          // user has to pay. Find a receiver
          for (int j = 0; j < tempArray.length; j++) {
            if (i == j)
              continue;
            if (tempArray[j].signum() > 0) {
              // possible receiver found
              BigDecimal amount;
              if (tempArray[i].abs().compareTo(tempArray[j]) < 0) {
                amount = tempArray[i].abs();
                tempArray[j] = tempArray[j].subtract(tempArray[i].abs());
                tempArray[i] = BigDecimal.ZERO;
              } else if (tempArray[i].abs().compareTo(tempArray[j]) > 0) {
                amount = tempArray[j];
                tempArray[i] = tempArray[i].add(tempArray[j]);
                tempArray[j] = BigDecimal.ZERO;
              } else { // tempArray[i].abs() == tempArray[j]
                amount = tempArray[j];
                tempArray[i] = BigDecimal.ZERO;
                tempArray[j] = BigDecimal.ZERO;
              }
              PaymentUserDTO paymentUser =
                  new PaymentUserDTO(amount, false, users.get(i), users.get(j), payment.getId());
              paymentUsers.add(paymentUser);
              break;
            }
          }
        }
        // else: user will receive money - should already be covered with the above cases.
      }
    }
  }

  public boolean isEditingAllowed() {
    return this.isCurrentUserAdmin()
        && simpleDateFormat.format(smartWG.getCostDivisionOverviewSelectedDate()).equals(
            simpleDateFormat.format(DateTime.now().toDate()));
  }

  private boolean paymentCalculationFinished(BigDecimal[] paymentArray) {
    for (BigDecimal aPaymentArray : paymentArray) {
      if (aPaymentArray.signum() != 0) {
        return false;
      }
    }
    return true;
  }

  private void clearFaceMessages() {
    Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
    while (msgIterator.hasNext()) {
      if (!msgIterator.next().getSummary().equals(validationMessages.getString("errorConfirm"))) {
        msgIterator.remove();
      }
    }
  }

  public List<PaymentUserDTO> getUnconfirmedPaymentUsers() {
    clearFaceMessages();
    if (userBean.getCurrentUser() != null && userBean.getCurrentUser().getId() != null) {
      unconfirmedPaymentUsers =
          paymentFacade.findUnconfirmedPaymentUsersByUserId(userBean.getCurrentUser().getId());
    }
    List<PaymentUserDTO> result = new ArrayList<>();
    for (PaymentUserDTO payDTO : unconfirmedPaymentUsers) {
      if (payDTO.getReceiver().getId().equals(userBean.getCurrentUser().getId())) {
        result.add(payDTO);
      }
    }
    return result;
  }

  public List<PaymentUserDTO> getDatabasePaymentUsersReceiver() {
    clearFaceMessages();
    List<PaymentUserDTO> result = new ArrayList<>();
    for (PaymentUserDTO payDTO : databasePaymentUsers) {
      if (payDTO.getReceiver().getId().equals(userBean.getCurrentUser().getId())) {
        result.add(payDTO);
      }
    }
    return result;
  }

  public List<PaymentUserDTO> getDatabasePaymentUsersSender() {
    clearFaceMessages();
    List<PaymentUserDTO> result = new ArrayList<>();
    for (PaymentUserDTO payDTO : databasePaymentUsers) {
      if (payDTO.getSender().getId().equals(userBean.getCurrentUser().getId())) {
        result.add(payDTO);
      }
    }
    return result;
  }

  public List<PaymentUserDTO> getUnconfirmedPaymentsSender() {
    clearFaceMessages();
    if (userBean.getCurrentUser() != null && userBean.getCurrentUser().getId() != null) {
      unconfirmedPaymentUsers =
          paymentFacade.findUnconfirmedPaymentUsersByUserId(userBean.getCurrentUser().getId());
    }
    List<PaymentUserDTO> result = new ArrayList<>();
    for (PaymentUserDTO payDTO : unconfirmedPaymentUsers) {
      if (payDTO.getSender().getId().equals(userBean.getCurrentUser().getId())) {
        result.add(payDTO);
      }
    }
    return result;
  }

  public List<UserDTO> getUsers() {
    return this.users;
  }

  public boolean hasChanges() {
    for (Map.Entry<BillDTO, Tuple> entry : costDivisionDetails.entrySet()) {
      if (entry.getValue().getChanged()) {
        return true;
      }
    }
    return false;
  }

  public String backButtonClicked(boolean confirm) {
    RequestContext context = RequestContext.getCurrentInstance();
    if (confirm && this.hasChanges()) {
      context.execute("PF('confirmDialog').show();");
      return null;
    }
    return navigationBean.getPageCostDivisionOverview() + Constants.PAGE_REDIRECT;
  }

  public boolean isCurrentUserAdmin() {
    return userBean.getCurrentUserGroup().getRole().equals(Role.ADMIN);
  }

  public boolean isSaveButtonEnabled() {
    // If there is no payment for the current month in the database enable the Save-Button if the
    // currentUser is Admin
    // Else only enable if there are unsaved changes
    return this.hasChanges()
        || ((this.payment == null || this.payment.getId() == null) && this.isEditingAllowed());
  }

  public void onConfirm(Integer paymentId, Integer senderId) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(smartWG.getCostDivisionOverviewSelectedDate());
    int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
    Date lastDateOfMonth =
        new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, lastDayOfMonth, 0, 0, 0,
            0).toDate();

    // Is is only allowed to confirm payments at the end of the month or any other days after:
    if (!DateTime.now().toDate().before(lastDateOfMonth)) {
      for (PaymentUserDTO payDTO : unconfirmedPaymentUsers) {
        if (payDTO.getPaymentId().equals(paymentId) && payDTO.getSender().getId().equals(senderId)) {
          paymentFacade.confirmUserPayment(payDTO);
          break;
        }
      }
    } else {
      FacesMessage msg = new FacesMessage();
      msg.setSummary(validationMessages.getString("errorConfirm"));
      msg.setDetail(validationMessages.getString("errorConfirmDetail"));
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      FacesContext.getCurrentInstance().addMessage(null, msg);
      FacesContext.getCurrentInstance().validationFailed();
    }
  }

  public void onEditCostDivision(RowEditEvent event) {
    if (event.getObject() != null) {
      Map.Entry<BillDTO, Tuple> entry = (Map.Entry<BillDTO, Tuple>) event.getObject();
      BigDecimal senderAmounts = BigDecimal.ZERO, billTotal = entry.getKey().getTotal();
      int positiveCounter = 0;

      // To validate input: Calculate sum of debts and sum of positive numbers
      for (BigDecimal amount : entry.getValue().getPayments()) {
        if (amount.signum() <= 0) {
          senderAmounts = senderAmounts.add(amount.abs());
        } else {
          positiveCounter++;
        }
      }

      // It's not allowed to input positive numbers or sums that are greater than billTotal
      if (billTotal.subtract(senderAmounts).signum() < 0 || positiveCounter > 1) {
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        if (positiveCounter > 1) {
          msg.setSummary(validationMessages.getString("errorCellNotNegative"));
          msg.setDetail(validationMessages.getString("errorCellNotNegativeDetail"));
        } else {
          msg.setSummary(validationMessages.getString("errorRowSum"));
          msg.setDetail(validationMessages.getString("errorRowSumDetail"));
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().validationFailed();

        // Reset payment of this row (because it already got (invalid) overwritten by user)
        calculateCostDivisionDetails(entry.getKey().getId());
        return;
      }

      // Input was ok => recalculate receivers-Amount
      BigDecimal[] entryPayments = entry.getValue().getPayments();
      for (int i = 0; i < entryPayments.length; i++) {
        if (entryPayments[i].signum() > 0) {
          entryPayments[i] = senderAmounts.equals(billTotal) ? BigDecimal.ZERO : senderAmounts;
          break;
        }
      }

      // Check if receivers-Amount got calculated right (= sum of entryPayments is ZERO)
      BigDecimal sumOfEntryPayments = BigDecimal.ZERO;
      for (int i = 0; i < entryPayments.length; i++) {
        sumOfEntryPayments = sumOfEntryPayments.add(entryPayments[i]);
      }
      if (sumOfEntryPayments.signum() != 0) {
        // Something went wrong during recalculation. This happens, if the receiver-Amount was ZERO.
        // => Try to fix it
        for (int i = 0; i < users.size(); i++) {
          if (users.get(i).getId().equals(entry.getKey().getCreatedBy_id())) {
            entryPayments[i] = senderAmounts.equals(billTotal) ? BigDecimal.ZERO : senderAmounts;
            break;
          }
        }
      }

      // Check sum of entryPayments again
      sumOfEntryPayments = BigDecimal.ZERO;
      for (int i = 0; i < entryPayments.length; i++) {
        sumOfEntryPayments = sumOfEntryPayments.add(entryPayments[i]);
      }
      if (sumOfEntryPayments.signum() != 0) {
        // The user edited the receiver (wrongly possible, if amount was ZERO) => reset
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        msg.setSummary(validationMessages.getString("errorEditedReceiver"));
        msg.setDetail(validationMessages.getString("errorEditedReceiverDetail"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().validationFailed();

        // Reset payment of this row (because it already got (invalid) overwritten by user)
        calculateCostDivisionDetails(entry.getKey().getId());
        RequestContext.getCurrentInstance().reset(":form:paymentTable");
        return;
      }

      // Replace cost division detail entry
      costDivisionDetails.put(entry.getKey(), new Tuple(entryPayments, true));

      // Reset and recalculate PaymentUsers
      paymentUsers.clear();
      calculatePaymentUsers();
    }
  }

  public String saveButtonClicked(boolean notify) {
    if (payment == null || payment.getId() == null) {
      payment = new PaymentDTO();
    }
    payment.setDate(DateTime.now().toDate());

    // Extract only relevant data from local costDivisionDetails
    HashMap<Integer, BigDecimal[]> payLog = new HashMap<>();
    for (int i = 0; i < costDivisionDetails.size(); i++) {
      Map.Entry<BillDTO, Tuple> entry =
          (Map.Entry<BillDTO, Tuple>) costDivisionDetails.entrySet().toArray()[i];
      payLog.put(entry.getKey().getId(), entry.getValue().getPayments());
    }
    payment.setPaymentLog(paymentLogConversionService.serialize(payLog));

    // Save Payment
    Integer paymentId = paymentFacade.createOrUpdatePayment(payment);
    payment.setId(paymentId);
    payment.setUserPayments(paymentUsers);

    // Save PaymentUsers
    if (!notify) {
      // First delete existing paymentUser-Entries in DB
      paymentFacade.createOrUpdatePaymentUsers(null, paymentId);
    }
    paymentFacade.createOrUpdatePaymentUsers(paymentUsers, paymentId);

    // Save Payment-ID into bills
    for (BillDTO bill : bills) {
      bill.setPaymentId(paymentId);
      billFacade.editBill(bill);
    }

    // Reset changed flags
    for (Map.Entry<BillDTO, Tuple> entry : costDivisionDetails.entrySet()) {
      entry.getValue().setChanged(false);
    }

    if (notify) {
      FacesMessage msg = new FacesMessage();
      msg.setSeverity(FacesMessage.SEVERITY_INFO);
      msg.setSummary(validationMessages.getString("saveSuccessful"));
      msg.setDetail(validationMessages.getString("saveSuccessfulDetail"));
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    return null;
  }

  private void calculateCostDivisionDetails() {
    calculateCostDivisionDetails(null);
  }

  private void calculateCostDivisionDetails(Integer billId) {
    for (BillDTO bill : bills) {
      if (billId != null && !bill.getId().equals(billId)) {
        continue;
      }
      BigDecimal[] costDivisionDetailPaymentArray = new BigDecimal[users.size()];
      Arrays.fill(costDivisionDetailPaymentArray, BigDecimal.ZERO);
      List<Integer> temporaryAbsentUsers = new ArrayList<>();
      List<Integer> permanentAbsentUsers = new ArrayList<>();

      for (AbsenceDTO absence : absences) {
        if (!absence.getUserGroup().getUserId().equals(bill.getCreatedBy_id())
            && !bill.getDate().before(absence.getAwayFrom())
            && !bill.getDate().after(absence.getAwayTill())) {
          if (absence.isTemporary()) {
            temporaryAbsentUsers.add(absence.getUserGroup().getUserId());
          } else {
            permanentAbsentUsers.add(absence.getUserGroup().getUserId());
          }
        }
      }
      for (CostEntryDTO costEntry : bill.getCostEntries()) {
        if (!costEntry.isExcluded()) {
          int usersWhoHaveToPayCounter = 0;
          if (costEntry.getCategory().isFixedCost()) {
            // Only exclude permanent absent users from division
            usersWhoHaveToPayCounter = users.size() - permanentAbsentUsers.size();
          } else {
            // Exclude all absent users (temporary and permanent) from division
            usersWhoHaveToPayCounter =
                users.size() - permanentAbsentUsers.size() - temporaryAbsentUsers.size();
          }

          int indexOfUserWhoPaid = -1;
          for (int j = 0; j < users.size(); j++) {
            if (users.get(j).getId().equals(bill.getCreatedBy_id())) {
              indexOfUserWhoPaid = j;
              break;
            }
          }

          if (indexOfUserWhoPaid == -1 || usersWhoHaveToPayCounter <= 1) {
            // Error in DB (a user who is not in the group created bill) or everybody is away...
            break;
          }

          for (int j = 0; j < users.size(); j++) {
            boolean joinedAfterBillCreation = true;
            for (UserGroupDTO userGroupDTO : users.get(j).getGroups()) {
              if (userGroupDTO.getGroupId().equals(userBean.getCurrentUserGroup().getGroupId())) {
                if (userGroupDTO.getJoinDate().after(bill.getDate())) {
                  joinedAfterBillCreation = false;
                }
                break;
              }
            }
            if (!joinedAfterBillCreation
                || permanentAbsentUsers.contains(users.get(j).getId())
                || (costEntry.getCategory().isFixedCost() && temporaryAbsentUsers.contains(users
                    .get(j).getId())) || users.get(j).getId().equals(bill.getCreatedBy_id())) {
              continue; // user does not have to pay
            }

            BigDecimal average = BigDecimal.ZERO;
            boolean userPercentReallyIsZero = false;
            List<UserGroupCategoryDTO> userGroupCategoryList;
            if (costEntry.getCategory().isCustomDivision()
                && (userGroupCategoryList =
                    userGroupCategoryFacade.findByCategory(costEntry.getCategory().getId())) != null) {
              // some cost entries like rent may have an extra division rule
              for (UserGroupCategoryDTO userGroupCategoryDTO : userGroupCategoryList) {
                if (userGroupCategoryDTO.getUserGroupDTO().getUserId().equals(users.get(j).getId())) {
                  average =
                      costEntry.getAmount().multiply(userGroupCategoryDTO.getPercent())
                          .setScale(2, RoundingMode.UP);
                  userPercentReallyIsZero = userGroupCategoryDTO.getPercent().signum() == 0;
                  break;
                }
              }
            }
            if (average.signum() == 0 && !userPercentReallyIsZero) {
              // divide costs equally between users (category is default category or error happened
              // in above loop)
              average =
                  costEntry.getAmount().divide(new BigDecimal(usersWhoHaveToPayCounter),
                      RoundingMode.UP);
            }

            costDivisionDetailPaymentArray[indexOfUserWhoPaid] =
                costDivisionDetailPaymentArray[indexOfUserWhoPaid].add(average);
            costDivisionDetailPaymentArray[j] = costDivisionDetailPaymentArray[j].subtract(average);
          }
        }
      }
      costDivisionDetails.put(bill, new Tuple(costDivisionDetailPaymentArray, false));
    }
  }

  public DiagramModel getDiagramModel() {
    return diagramModel;
  }

  public SortedMap<BillDTO, Tuple> getCostDivisionDetails() {
    return this.costDivisionDetails;
  }

  public PaymentDTO getPayment() {
    return this.payment;
  }

  public List<PaymentUserDTO> getPaymentUsers() {
    return this.paymentUsers;
  }

  public HashMap<String, BigDecimal> getPaidBillAmountsOfUsers() {
    return this.paidBillAmountsOfUsers;
  }

  private Connection createConnection(EndPoint from, EndPoint to, String label) {
    Connection conn = new Connection(from, to, new StraightConnector());
    conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

    if (label != null) {
      conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
    }
    return conn;
  }

  public BigDecimal getTotalExpenses() {
    return totalExpenses;
  }

  public void decrementMonth() throws Exception {
    Calendar c = Calendar.getInstance();
    c.setTime(smartWG.getCostDivisionOverviewSelectedDate());
    c.add(Calendar.MONTH, -1);
    smartWG.setCostDivisionOverviewSelectedDate(c.getTime());

    init();
  }

  public void incrementMonth() throws Exception {
    Calendar c = Calendar.getInstance();
    c.setTime(smartWG.getCostDivisionOverviewSelectedDate());
    c.add(Calendar.MONTH, 1);
    smartWG.setCostDivisionOverviewSelectedDate(c.getTime());

    init();
  }
}
