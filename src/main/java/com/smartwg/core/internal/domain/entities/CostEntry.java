package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks), Tobias Ortmayr (to)
 */
@Entity
@Table(name = "cost_entry")
public class CostEntry implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(nullable = false)
  private BigDecimal amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "bill_id", nullable = false)
  private Bill bill;

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false, name = "is_excluded")
  private boolean excluded;

  private int times = 1;

  /**
   * default constructor for hibernate
   */
  public CostEntry() {}

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

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Bill getBill() {
    return bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public boolean isExcluded() {
    return excluded;
  }

  public void setExcluded(boolean excluded) {
    this.excluded = excluded;
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CostEntry)) {
      return false;
    }

    CostEntry costEntry = (CostEntry) o;

    return new EqualsBuilder().append(excluded, costEntry.excluded).append(times, costEntry.times)
        .append(id, costEntry.id).append(name, costEntry.name).append(amount, costEntry.amount)
        .append(bill, costEntry.bill).append(category, costEntry.category).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(amount).append(bill)
        .append(category).append(excluded).append(times).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("amount", amount)
        .append("bill", bill).append("category", category).append("excluded", excluded)
        .append("times", times).toString();
  }
}
