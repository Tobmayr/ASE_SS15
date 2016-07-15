package com.smartwg.core.controllers.bills;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.util.Constants;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named("billActions")
@Scope("session")
public class BillActionsBean {

  @Inject
  private NavigationBean navigation;

  @Inject
  private BillFacade facade;

  @PostConstruct
  public void init() {

  }

  public String showBill(final BillDTO billDTO, String returnPage, boolean privateBill) {
    String retString =
        navigation.getPageShowBill() + Constants.PAGE_REDIRECT + "&billId=" + billDTO.getId()
            + "&returnPage=" + returnPage;
    if (privateBill) {
      retString += "&private=true";
    }
    return retString;
  }



  public String editBill(final BillDTO billDTO, String returnPage) {
    return navigation.getPageNewBill() + Constants.PAGE_REDIRECT + "&billId=" + billDTO.getId()
        + "&returnPage=" + returnPage;
  }

  public String deleteBill(final BillDTO bill, String returnPage) {
    facade.deleteBill(bill);
    return returnPage + Constants.PAGE_REDIRECT;
  }

  public String convertShoppingList(final ShoppingListDTO shoppingList, String returnPage) {
    return navigation.getPageNewBill() + Constants.PAGE_REDIRECT + "&shopList="
        + shoppingList.getId() + "&returnPage=" + returnPage;
  }

}
