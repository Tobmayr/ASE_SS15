package com.smartwg.core.controllers.bills;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;

@Named("billOverview")
@Scope("view")
public class BillOverviewBean {

  @Inject
  private BillFacade facade;

  private BillDTO bill;
  private String returnPage;
  private CostEntryDTO selectedCostEntry;
  private StreamedContent file;

  @PostConstruct
  public void setUp() {
    final Map<String, String> requestParameterMap =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String billId = requestParameterMap.get("billId");
    returnPage = requestParameterMap.get("returnPage");
    String date = requestParameterMap.get("date");
    String privateEntries = requestParameterMap.get("private");
    if (date != null) {
      returnPage += "date=" + date;
    }

    if (billId != null) {
      final Integer integer = Integer.valueOf(billId);
      bill = facade.findById(integer);
      if (privateEntries == null || !privateEntries.equals("true")) {
        if (bill.getCostEntries().isEmpty()) {
          bill.setCostEntries(facade.getCostEntries(integer));
        }
      } else {
        bill.setCostEntries(facade.getPrivateCostEntries(integer));
      }
      if (bill.getResource() != null) {
        if (bill.getResource().getContent() != null) {
          System.out.println("set file");
          InputStream stream = new ByteArrayInputStream(bill.getResource().getContent());
          file =
              new DefaultStreamedContent(stream, bill.getResource().getType(), bill.getResource()
                  .getName());
        }

      }

    } else {
      bill = new BillDTO();
    }

  }
  
  public StreamedContent getFile() {
    return file;
}

  public String mobileWizardNext(int id) {
    switch (id) {
      case 1: {
        if (bill.getRecurring() != null || bill.getResource() != null) {
          return "pm:recurring?transition=slide";
        }

        return "pm:items?transition=slide";
      }
      case 2: {
        return "pm:items?transition=slide";
      }
      case 3: {

        return "pm:showItem?transition=slide";
      }
      default:
        return null;
    }

  }

  public String mobileWizardBack(int id) {
    switch (id) {
      case 2: {
        return "pm:basic?transition=slide";
      }
      case 3: {
        if (bill.getRecurring() != null || bill.getResource() != null)
          return "pm:recurring?transition=slide";
        return "pm:basic?transition=slide";
      }
      case 4: {
        return "pm:items?transition=slide";
      }
      default:
        return "";

    }
  }

  public BillDTO getBill() {
    return bill;
  }

  public void setBill(BillDTO bill) {
    this.bill = bill;
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



}
