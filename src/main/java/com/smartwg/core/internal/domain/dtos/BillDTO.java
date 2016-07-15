package com.smartwg.core.internal.domain.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.CostEntry;
import com.smartwg.core.internal.domain.entities.Shop;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class BillDTO implements Comparable<BillDTO> {
  private Integer id;
  private String name;
  private RecurringDTO recurring;
  private Date date = new Date();
  private Integer createdBy_id;
  private String createdByUsername;
  private BigDecimal total = new BigDecimal(0.00);
  private ShopDTO shopDTO;
  private ResourceDTO resource;
  private List<CostEntryDTO> costEntries = new ArrayList<CostEntryDTO>();
  private CurrencyDTO currency;
  private boolean recurringBill;
  private Integer group_id;
  private boolean privateBill;
  private Integer payment_id;


  public BillDTO() {};

  public BillDTO(final Integer id, final String name, final Date date, final Integer createdby_id,
      final String createdby_username, final BigDecimal total, final boolean privateBill,
      final Integer currency_id, final String currency_isoCode, final Integer group_id, Shop shop,
      Integer payment_id) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.createdBy_id = createdby_id;
    this.createdByUsername = createdby_username;
    this.total = total;
    this.privateBill = privateBill;
    this.currency = new CurrencyDTO(currency_id, currency_isoCode);
    this.group_id = group_id;
    if (shop != null) {
      this.shopDTO = new ShopDTO(shop);
    }
    this.payment_id = payment_id;

  }

  public BillDTO(final Bill bill) {
    this.id = bill.getId();
    this.name = bill.getName();
    this.date = bill.getDate();
    this.createdBy_id = bill.getCreatedBy().getId();
    this.createdByUsername = bill.getCreatedBy().getUserName();
    this.total = bill.getTotal();
    this.currency = new CurrencyDTO(bill.getCurrency().getId(), bill.getCurrency().getIsoCode());
    this.privateBill = bill.isPrivateBill();
    this.group_id = bill.getGroup().getId();
    if (bill.getShop() != null) {
      this.shopDTO = new ShopDTO(bill.getShop());
    }
    if (bill.getRecurring() != null) {
      this.recurring = new RecurringDTO(bill.getRecurring());
      recurringBill = true;
    }

    if (bill.getResource() != null) {
      this.resource =
          new ResourceDTO(bill.getResource().getId(), bill.getResource().getName(), bill
              .getResource().getType());
    }

    for (CostEntry entry : bill.getCostEntries()) {
      costEntries.add(new CostEntryDTO(entry));
    }

    if (bill.getPayment() != null) {
      this.payment_id = bill.getPayment().getId();
    }
  }

  public void addCostEntry(CostEntryDTO c) {
    this.costEntries.add(c);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CostEntryDTO> getCostEntries() {
    return costEntries;
  }

  public void setCostEntries(List<CostEntryDTO> costEntries) {
    this.costEntries = costEntries;
  }

  public CurrencyDTO getCurrency() {
    return currency;
  }

  public void setCurrency(CurrencyDTO currency) {
    this.currency = currency;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    if (date != null) {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, 12);
      this.date = calendar.getTime();
    }

  }

  public RecurringDTO getRecurring() {
    return recurring;
  }

  public void setRecurring(RecurringDTO recurring) {
    this.recurring = recurring;
  }

  public ResourceDTO getResource() {
    return resource;
  }

  public void setResource(ResourceDTO resource) {
    this.resource = resource;
  }


  public boolean isPrivateBill() {
    return privateBill;
  }

  public void setPrivateBill(boolean privateBill) {
    this.privateBill = privateBill;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCreatedByUsername() {
    return createdByUsername;
  }

  public void setCreatedByUsername(String createdByUsername) {
    this.createdByUsername = createdByUsername;
  }

  public Integer getCreatedBy_id() {
    return createdBy_id;
  }

  public void setCreatedBy_id(Integer createdBy_id) {
    this.createdBy_id = createdBy_id;
  }

  public ShopDTO getShopDTO() {
    return shopDTO;
  }

  public void setShopDTO(ShopDTO shopDTO) {
    this.shopDTO = shopDTO;
  }

  public Integer getGroupId() {
    return group_id;
  }

  public void setGroupId(Integer group_id) {
    this.group_id = group_id;
  }

  public boolean isRecurringBill() {
    return recurringBill;
  }

  public void setRecurringBill(boolean recurringBill) {
    this.recurringBill = recurringBill;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Integer getPaymendId() {
    return this.payment_id;
  }

  public void setPaymentId(Integer paymentId) {
    this.payment_id = paymentId;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof BillDTO))
      return false;
    BillDTO that = (BillDTO) obj;
    return new EqualsBuilder().append(this.id, that.id)
        .append(this.createdBy_id, that.createdBy_id).append(this.name, that.name)
        .append(this.createdByUsername, that.createdByUsername).append(this.total, that.total)
        .append(this.recurringBill, that.recurringBill).append(this.group_id, that.group_id)
        .append(this.privateBill, that.privateBill).append(this.payment_id, that.payment_id)
        .isEquals();

  }


  @Override
  public String toString() {
    return "BillDTO [id=" + id + ", name=" + name + ", recurring=" + recurring + ", date=" + date
        + ", createdBy_id=" + createdBy_id + ", createdByUsername=" + createdByUsername
        + ", total=" + total + ", shopDTO=" + shopDTO + ", resource=" + resource + ", costEntries="
        + costEntries + ", currency=" + currency + ", recurringBill=" + recurringBill
        + ", group_id=" + group_id + "]";
  }

  @Override
  public int compareTo(BillDTO o) {
    return id.compareTo(o.id);
  }
}
