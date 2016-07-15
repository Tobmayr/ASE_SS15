package com.smartwg.core.controllers.bills;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.util.Constants;

/**
 * @author Tobias Ortmayr (to)
 */
@Named("billList")
@Scope("view")
public class BillListBean {
  private static final Logger logger = LoggerFactory.getLogger(BillListBean.class);
  @Inject
  BillFacade facade;
  @Inject
  UserBean userBean;
  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private ShopFacade ShopFacade;
  @Inject
  private NavigationBean navigation;
  private List<BillDTO> bills;
  private BigDecimal overviewTotal;
  private BillSelect billSelect = BillSelect.CURRENT_MONTH;
  private List<BillDTO> filteredBills;
  // TODO still mocked
  private List<CategoryDTO> categories;
  private List<ShopDTO> shops;
  private int overviewType = 0;
  private boolean privateBills;
  private BillDTO currentBill;
  // TODO Default Currency is mocked
  private CurrencyDTO defaultCurrency;
  private Date start = new Date();
  private Date end = new Date();


  public BillListBean() {

  }

  @PostConstruct
  public void init() {
    logger.info("CREATE NEW BEAN");
    if (bills == null) {
      billSelectChanged(null);
    }

    shops =
        ShopFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId(), userBean
            .getCurrentUserGroup().getGroupCountryId());
    categories = categoryFacade.findByGroup(userBean.getCurrentUserGroup().getGroupId());

    defaultCurrency = new CurrencyDTO();
    defaultCurrency.setIsoCode("EUR");


  }



  public void overviewChanged(AjaxBehaviorEvent event) {

    if (overviewType == 0) {
      privateBills = false;

    } else if (overviewType == 1) {
      privateBills = true;

    }
    billSelectChanged(null);
  }


  public void billSelectChanged(AjaxBehaviorEvent event) {

    DateTime dEnd = DateTime.now();
    DateTime dStart;
    Date start = new Date();
    Date end = new Date();

    switch (billSelect) {
      case CURRENT_MONTH:

        start = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case LAST_MONTH:
        dStart = dEnd.minusMonths(1);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dStart.getYear(), dStart.getMonthOfYear() + 1, 1, 0, 0, 0).toDate();
        break;
      case LAST_HALFYEAR:
        dStart = dEnd.minusMonths(6);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case LAST_YEAR:
        dStart = dEnd.minusMonths(12);
        start = new DateTime(dStart.getYear(), dStart.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
        end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();
        break;
      case CUSTOM:
        start = this.start;
        end = this.end;
        break;
    }


    if (!privateBills) {
      bills =
          facade.getBillsBetweenTimespan(start, end, userBean.getCurrentUserGroup().getGroupId());
      overviewTotal = facade.calculateTotal(bills);

    } else {
      bills =
          facade.getPrivateBillsBetweenTimespan(start, end, userBean.getCurrentUserGroup()
              .getGroupId(), userBean.getCurrentUser().getId());
      calculatePrivateTotal();
    }


  }



  private void calculatePrivateTotal() {
    BigDecimal privateCost = new BigDecimal(0.0);

    for (BillDTO b : bills) {
      logger.info("ARE THER COSTENTRIES? " + b.getCostEntries().size());
      if (b.isPrivateBill()) {
        privateCost = privateCost.add(b.getTotal());
      } else {
        BigDecimal[] temp = facade.calcualteBillTotal(b);
        b.setTotal(temp[1]);
        privateCost = privateCost.add(temp[1]);
      }

    }
    overviewTotal = privateCost;

  }

  public BillSelect[] getBillSelectTypes() {
    return BillSelect.values();
  }



  public BillFacade getFacade() {
    return facade;
  }

  public void setFacade(BillFacade facade) {
    this.facade = facade;
  }

  public NavigationBean getNavigation() {
    return navigation;
  }

  public void setNavigation(NavigationBean navigation) {
    this.navigation = navigation;
  }

  public List<BillDTO> getBills() {
    return bills;
  }

  public void setBills(List<BillDTO> bills) {
    this.bills = bills;
  }

  public BigDecimal getOverviewTotal() {
    return overviewTotal;
  }

  public void setOverviewTotal(BigDecimal overviewTotal) {
    this.overviewTotal = overviewTotal;
  }

  public BillSelect getBillSelect() {
    return billSelect;
  }

  public void setBillSelect(BillSelect billSelect) {
    this.billSelect = billSelect;
  }

  public List<BillDTO> getFilteredBills() {
    return filteredBills;
  }

  public void setFilteredBills(List<BillDTO> filteredBills) {
    this.filteredBills = filteredBills;
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


  public int getOverviewType() {
    return overviewType;
  }

  public void setOverviewType(int overviewType) {
    this.overviewType = overviewType;
  }

  public BillDTO getCurrentBill() {
    return currentBill;
  }

  public void setCurrentBill(BillDTO currentBill) {
    this.currentBill = currentBill;
  }

  public enum BillSelect {
    CURRENT_MONTH("sCurrentMonth"), LAST_MONTH("sLastMonth"), LAST_HALFYEAR("sLast6Months"), LAST_YEAR(
        "sLastYear"), CUSTOM("cCustom");

    private String ms_id;
    
    private ResourceBundle ms = SmartWG.getMessageBundle();

    BillSelect(String ms_id) {
      this.ms_id = ms_id;

    }

    public String getName() {
      return ms.getString(ms_id);
    }
  }

  public CurrencyDTO getDefaultCurrency() {
    return defaultCurrency;
  }

  public void setDefaultCurrency(CurrencyDTO defaultCurrency) {
    this.defaultCurrency = defaultCurrency;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public boolean isPrivateBills() {
    return privateBills;
  }

  public void setPrivateBills(boolean privateBills) {
    this.privateBills = privateBills;
  }


}
