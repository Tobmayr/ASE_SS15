package com.smartwg.core.internal.domain.dtos;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.CostEntry;

/**
 * @author Tobias Ortmayr (to)
 */
public class CostEntryDTO {
  private Integer id;
  private String name;
  private BigDecimal amount = new BigDecimal(0.00);
  private CategoryDTO category;
  private boolean excluded;
  private int times = 1;
  private Date date;

  public CostEntryDTO() {

  }

  public CostEntryDTO(final Integer id, final String name, final BigDecimal amount,
      final int times, final boolean excluded, final Integer categoryId, final String categoryName,
      final boolean categoryIsFixedCost, final boolean isDefault) {
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.times = times;
    this.excluded = excluded;
    this.category = new CategoryDTO(categoryId, categoryName, categoryIsFixedCost, isDefault);
  }

  public CostEntryDTO(CostEntry entry) {
    this.id = entry.getId();
    this.name = entry.getName();
    this.amount = entry.getAmount();
    this.times = entry.getTimes();
    this.excluded = entry.isExcluded();
    Category cat = entry.getCategory();
    if (cat != null) {
      this.category = new CategoryDTO(cat);
    }
  }

  public CostEntryDTO(final Integer id, final String name, final BigDecimal amount,
      final int times, final boolean excluded, final Integer categoryId, final String categoryName,
      final boolean categoryIsFixedCost, final Date date) {
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.times = times;
    this.excluded = excluded;
    this.category = new CategoryDTO(categoryId, categoryName, categoryIsFixedCost, false);
    this.date = date;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof CostEntryDTO))
      return false;
    final CostEntryDTO that = (CostEntryDTO) o;
    return new EqualsBuilder().append(this.id, that.id).append(this.name, that.name)
        .append(this.amount, that.amount).append(this.times, that.times)
        .append(this.excluded, that.excluded).append(this.category, that.category).isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("Id", id).append("name", name).append("amount", amount)
        .append("times", times).append("excluded", excluded).append("category", category)
        .toString();
  }


  public int getRowKey() {
    return this.hashCode();
  }

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
    this.amount = this.amount.setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }



}
