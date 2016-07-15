package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Kamil Sierzant (ks),Tobias Ortmayr (to)
 */
@Entity
@Table(name = "bill")
public class Bill implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "resource_id")
  private Resource resource;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "recurring_id")
  private Recurring recurring;

  private Date date;

  @Column(nullable = false)
  private BigDecimal total;
  @Column(name = "private")
  private boolean privateBill;
  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by_user_id")
  private User createdBy;

  @ManyToOne(optional = false)
  @JoinColumn(name = "currency_id")
  private Currency currency;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shop_id")
  private Shop shop;
  @ManyToOne()
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CostEntry> costEntries = new ArrayList<CostEntry>();

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinColumn(name = "group_id")
  private Group group;

  @OneToOne(mappedBy = "bill", orphanRemoval = true)
  private ShoppingList shoppingList;

  /**
   * default constructor for hibernate
   */
  public Bill() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Recurring getRecurring() {
    return recurring;
  }

  public void setRecurring(Recurring recurring) {
    this.recurring = recurring;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public List<CostEntry> getCostEntries() {
    return costEntries;
  }

  public void setCostEntries(List<CostEntry> costEntries) {
    this.costEntries = costEntries;
  }

  public void addCostEntry(CostEntry costEntry) {
    costEntries.add(costEntry);
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public void setShoppingList(ShoppingList shoppingList) {
    this.shoppingList = shoppingList;
  }


  public boolean isPrivateBill() {
    return privateBill;
  }

  public void setPrivateBill(boolean privateBill) {
    this.privateBill = privateBill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Bill)) {
      return false;
    }

    Bill bill = (Bill) o;

    return new EqualsBuilder().append(id, bill.id).append(name, bill.name)
        .append(resource, bill.resource).append(recurring, bill.recurring).append(date, bill.date)
        .append(total, bill.total).append(createdBy, bill.createdBy)
        .append(currency, bill.currency).append(shop, bill.shop)
        .append(costEntries, bill.costEntries).append(group, bill.group)
        .append(shoppingList, bill.shoppingList).append(privateBill, bill.privateBill).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(resource).append(recurring)
        .append(date).append(total).append(createdBy).append(currency).append(shop).append(group)
        .append(shoppingList).append(privateBill).toHashCode();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("id", id).append("name", name).append("date", date).append("total", total)
        .append("group", group.getId()).append("createdBy", createdBy.getId())
        .append("currency", currency.getId()).append("isPrivate", privateBill);
    if (shoppingList != null) {
      builder.append("shoppingList", shoppingList.getId());
    }
    if (resource != null) {
      builder.append("resource", resource.getId());
    }
    if (recurring != null) {
      builder.append("recurring", recurring);
    }
    if (shop != null) {
      builder.append("shop", shop.getId());
    }
    return builder.toString();
  }
}
