package com.smartwg.core.controllers.bills;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.CurrencyFacade;
import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.facades.ShoppingListFacade;
import com.smartwg.core.internal.domain.RecurringType;
import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.GroupDTO;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.RecurringDTO;
import com.smartwg.core.internal.domain.dtos.RepeatType;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Tobias Ortmayr (to)
 */

@Named("editBill")
@Scope("view")
public class EditBillBean {
  private static final Logger logger = LoggerFactory.getLogger(EditBillBean.class);
  
  private ResourceBundle ms = SmartWG.getMessageBundle();
  private ResourceBundle valMs = SmartWG.getValidationBundle();

  @Inject
  private BillFacade billFacade;
  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private CurrencyFacade currencyFacade;
  @Inject
  private ShopFacade shopFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private NavigationBean navigation;
  @Inject
  private ShoppingListFacade shoppingListFacade;


  private List<CurrencyDTO> currencies;
  private List<CategoryDTO> categories;
  private List<ShopDTO> shops;
  private CostEntryDTO costEntry;
  private BigDecimal total;
  private BigDecimal privateCost;
  private BillDTO bill;
  private ShoppingListDTO shoppingListDTO;
  private CostEntryDTO selectedCostEntry;
  private boolean onlyTotal;
  private String returnPage;
  private String newCateName;
  private boolean newCatDlgVisible;
  private UploadedFile file;
  public EditBillBean() {

  }

  @PostConstruct
  public void init() {


    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    if (requestParameterMap != null) {
      String billId = requestParameterMap.get("billId");
      returnPage = requestParameterMap.get("returnPage");
      String shopList = requestParameterMap.get("shopList");

      total = new BigDecimal(0);
      privateCost = new BigDecimal(0);

      if (billId != null) {
        final Integer integer = Integer.valueOf(billId);
        bill = billFacade.findById(integer);
        if (bill.getCostEntries().isEmpty()) {
          bill.setCostEntries(billFacade.getCostEntries(integer));
        }

        if (bill.getCostEntries().isEmpty()
            || (bill.getCostEntries().size() == 1 && bill.getCostEntries().get(0).getName()
                .equals("TOTAL"))) {
          onlyTotal = true;
        }
        logger.info("" + bill.getCostEntries().size());

        calculateTotal();

      } else if (shopList != null) {
        final Integer id = Integer.valueOf(shopList);
        this.shoppingListDTO = shoppingListFacade.getShoppingListsById(id);
        bill = new BillDTO();
        bill.setCostEntries(new ArrayList<CostEntryDTO>());
        convertToBill();
      } else {
        bill = new BillDTO();
        bill.setCostEntries(new ArrayList<CostEntryDTO>());

      }
    }


    if (costEntry == null) {
      costEntry = new CostEntryDTO();

    }


    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());
    currencies = currencyFacade.findAll();
    if (userBean.getCurrentGroup().getCountry() != null
        && userBean.getCurrentGroup().getCountry().getCurrency() != null) {
      bill.setCurrency(userBean.getCurrentGroup().getCountry().getCurrency());
    } else {
      if (!currencies.isEmpty()) {
        bill.setCurrency(currencies.get(0));
      }
    }

    shops =
        shopFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId(), userBean
            .getCurrentUserGroup().getGroupCountryId());


    costEntry.setName(ms.getString("itemAutoFill") + bill.getCostEntries().size() + 1);

  }

  public void costEntryTabbed(SelectEvent event) {

    if (event.getObject() != null) {
      selectedCostEntry = (CostEntryDTO) event.getObject();
    }

  }

  public void changePrivateBill() {
    for (CostEntryDTO dto : bill.getCostEntries()) {
      dto.setExcluded(bill.isPrivateBill());
    }
  }

  public String mobileWizardNext(int id) {

    logger.info("mobileWizardmobileWizard called");

    switch (id) {
      case 1: {
        if (bill.isRecurringBill()) {
          logger.info("gotoRec");
          bill.setRecurring(new RecurringDTO());
          return "pm:recurring?transition=slide";
        }

        return "pm:items?transition=slide";
      }
      case 2: {
        return "pm:items?transition=slide";
      }
      case 3: {

        return "pm:addItem?transition=slide";
      }
      default:
        return "";
    }

  }

  public String mobileWizardBack(int id) {
    switch (id) {
      case 2: {
        return "pm:basic?transition=slide";
      }
      case 3: {
        if (bill.isRecurringBill())
          return "pm:recurring?transition=slide";
        return "pm:basic?transition=slide";
      }
      case 4: {
        costEntry = new CostEntryDTO();
        costEntry.setName(ms.getString("itemAutoFill") + (bill.getCostEntries().size() + 1));
        return "pm:items?transition=slide";
      }
      default:
        return "";

    }
  }


  public void dialogCreate() {

    CategoryDTO category = new CategoryDTO();
    category.setName(newCateName);
    category.setDefaultCategory(true);
    category.setGroup_id(userBean.getCurrentUserGroup().getGroupId());
    categoryFacade.saveCategory(category);
    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());

  }

  public void handleFileUpload(FileUploadEvent event) {
    logger.info("handle fileupload");
    ResourceDTO dto = new ResourceDTO();
    dto.setContent(event.getFile().getContents());
    dto.setName(event.getFile().getFileName());
    dto.setType(event.getFile().getContentType());
    logger.info("contentsize: " + dto.getContent().length);
    bill.setResource(dto);
    startIr();
  }

  public void startIr() {
    logger.info("startIr");
    BillDTO result = billFacade.doImageRecognition(bill.getResource(), shops, currencies);

    if (result != null) {
      logger.info(result.toString());
      bill = result;
      for (CostEntryDTO ce : bill.getCostEntries()) {
        ce.setCategory(categories.get(0));
      }
      if (bill.getCurrency() == null) {
        bill.setCurrency(userBean.getCurrentGroup().getCountry().getCurrency());
      }
      calculateTotal();
    }
  }

  public void amountEdited(AjaxBehaviorEvent event) {
    calculateTotal();
  }


  private void calculateTotal() {
    BigDecimal[] temp = billFacade.calcualteBillTotal(bill);
    total = temp[0];
    privateCost = temp[1];
    bill.setTotal(temp[0]);

  }

  public void deleteItem(CostEntryDTO costEntryDTO) {
    logger.info("deleteItems() invoked");

    bill.getCostEntries().remove(costEntryDTO);
    if (bill.getCostEntries().isEmpty()) {
      bill.setTotal(new BigDecimal(0));
      total = new BigDecimal(0);
      privateCost = new BigDecimal(0);
    } else {
      calculateTotal();
    }



  }

  public RecurringType[] getRecurringTypes() {
    return RecurringType.values();
  }

  public RepeatType[] getRepeatTypes() {
    return RepeatType.values();
  }


  public void changeItemStatus(CostEntryDTO dto) {
    logger.info("changeItemStatus- invoked");
    logger.info(dto.toString());
    dto.setExcluded(!dto.isExcluded());
    calculateTotal();
  }

  public void addItem() {
    if (validateItem()) {
      if (bill.isPrivateBill())
        costEntry.setExcluded(true);
      bill.getCostEntries().add(costEntry);
      calculateTotal();
      costEntry = new CostEntryDTO();
      costEntry.setName(ms.getString("itemAutoFill") + bill.getCostEntries().size());
    }

  }

  private boolean validateItem() {
    if (costEntry.getName() == null || costEntry.getName().equals("")) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.costentry.name.invalid"));
      return false;
    }
    if (costEntry.getAmount().compareTo(new BigDecimal(0)) == -1
        || costEntry.getAmount().compareTo(new BigDecimal(99999.99)) == 1) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.costentry.amount.invalid"));
      return false;
    }
    if (costEntry.getCategory() == null) {
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_ERROR,
          valMs.getString("error.costentry.category.invalid"));
      return false;
    }
    return true;
  }

  public String addItemMobile() {

    if (validateItem()) {
      addItem();
      return "pm:items?transition=slide";
    }
    return "pm:addItem";

  }

  public void reset() {
    bill = new BillDTO();
    costEntry = new CostEntryDTO();
    bill.setCostEntries(new ArrayList<CostEntryDTO>());
    total = new BigDecimal(0);
    privateCost = new BigDecimal(0);
    bill.setRecurringBill(false);
    if (shoppingListDTO!=null) convertToBill();

  }
  public void mobileUpload(){
    logger.info(""+file.getContents().length);
  }


  private void convertToBill() {
    bill.setName(shoppingListDTO.getName());
    List<CostEntryDTO> costEntries = new ArrayList<CostEntryDTO>();
    final List<ListItemDTO> shoppingListsPositions =
        shoppingListFacade.getShoppingListsPositions(shoppingListDTO.getId());
    for (ListItemDTO item : shoppingListsPositions) {

      if (item.isDone()){
      String name = item.getCategory().getName();
      if (item.getCategory().isDefaultCategory()) {
        name = item.getCategory().getI18NKey();
      }
      CostEntryDTO costEntryDTO =
          new CostEntryDTO(null, item.getName(), new BigDecimal(0), item.getAmount(), false, item
              .getCategory().getId(), name, item.getCategory().isFixedCost(), item.getCategory()
              .isDefaultCategory());

      logger.info(costEntryDTO.getName());
      costEntries.add(costEntryDTO);
      }
    }
    bill.setCostEntries(costEntries);
  }

  public String createBill(boolean docontinue) {
    logger.info("createBill() invoked");
    if (bill.isRecurringBill()) {
      switch (bill.getRecurring().getRepeatType()) {
        case UNTIL: {
          bill.getRecurring().setTimes(0);
          break;
        }
        case TIMES: {
          bill.getRecurring().setEndDate(null);
          break;
        }
        default: {
          bill.getRecurring().setEndDate(null);
          bill.getRecurring().setTimes(0);
          break;
        }
      }
    } else {
      bill.setRecurring(null);
    }


    if (onlyTotal) {

      costEntry.setName("TOTAL");
      costEntry.setAmount(bill.getTotal());
      costEntry.setExcluded(bill.isPrivateBill());
      bill.setCostEntries(Arrays.asList(costEntry));
    }

    if (bill.getCreatedBy_id() == null) {
      bill.setCreatedBy_id(userBean.getCurrentUser().getId());
      bill.setCreatedByUsername(userBean.getCurrentUser().getUserName());
    }
    if (bill.getGroupId() == null) {
      bill.setGroupId(userBean.getCurrentUserGroup().getGroupId());
    }

    billFacade.saveBill(bill);


    if (docontinue)
      return navigation.getPageNewBill();
    return navigation.getPageBillOverview();
  }

  public String onFlowProcess(FlowEvent event) {

    if (event.getOldStep().equals("basicTab")) {
      if (bill.isRecurringBill()) {
        if (bill.getRecurring() == null) {
          bill.setRecurring(new RecurringDTO());
        }

        return "recurringTab";
      } else
        return "itemsTab";

    } else if (event.getOldStep().equals("itemsTab") && event.getNewStep().equals("recurringTab")) {
      if (bill.isRecurringBill())
        return "recurringTab";
      else
        return "basicTab";
    }
    return event.getNewStep();
  }


  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BillDTO getBill() {
    return bill;
  }

  public void setBill(BillDTO bill) {
    this.bill = bill;
  }

  public CostEntryDTO getCostEntry() {
    return costEntry;
  }

  public void setCostEntry(CostEntryDTO costEntry) {
    this.costEntry = costEntry;
  }

  public List<CurrencyDTO> getCurrencies() {
    return currencies;
  }

  public void setCurrencies(List<CurrencyDTO> currencies) {
    this.currencies = currencies;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

  public List<ShopDTO> getShops() {
    return shops;
  }

  public void setShops(List<ShopDTO> shops) {
    this.shops = shops;
  }

  public BigDecimal getPrivateCost() {
    return privateCost;
  }

  public void setPrivateCost(BigDecimal privateCost) {
    this.privateCost = privateCost;
  }

  public ShoppingListDTO getShoppingListDTO() {
    return shoppingListDTO;
  }

  public void setShoppingListDTO(ShoppingListDTO shoppingListDTO) {
    this.shoppingListDTO = shoppingListDTO;
  }

  public boolean isOnlyTotal() {
    return onlyTotal;
  }

  public void setOnlyTotal(boolean onlyTotal) {
    this.onlyTotal = onlyTotal;
  }

  public String getReturnPage() {
    return returnPage;
  }

  public void setReturnPage(String returnPage) {
    this.returnPage = returnPage;
  }

  public CostEntryDTO getSelectedCostEntry() {
    return selectedCostEntry;
  }

  public void setSelectedCostEntry(CostEntryDTO selectedCostEntry) {
    this.selectedCostEntry = selectedCostEntry;
  }

  public String getNewCateName() {
    return newCateName;
  }

  public void setNewCateName(String newCateName) {
    this.newCateName = newCateName;
  }

  public boolean isNewCatDlgVisible() {
    return newCatDlgVisible;
  }

  public void setNewCatDlgVisible(boolean newCatDlgVisible) {
    this.newCatDlgVisible = newCatDlgVisible;
  }

 

  public UploadedFile getFile() {
    return file;
  }
  

  public void setFile(UploadedFile file) {
      this.file = file;
  }
   
}
