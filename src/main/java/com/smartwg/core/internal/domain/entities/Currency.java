package com.smartwg.core.internal.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "currency")
public class Currency implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "iso_code", nullable = false)
  private String isoCode;

  @Column(name = "rate_to_euro", nullable = false)
  private BigDecimal rateToEuro;

  @Column(nullable = false)
  private Date updated;

  @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
  private List<Country> countries = new ArrayList<Country>();

  @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
  private List<Bill> bills = new ArrayList<Bill>();

  /**
   * default constructor for hibernate
   */
  public Currency() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

  public BigDecimal getRateToEuro() {
    return rateToEuro;
  }

  public void setRateToEuro(BigDecimal rateToEuro) {
    this.rateToEuro = rateToEuro;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  public List<Bill> getBills() {
    return bills;
  }

  public void setBills(List<Bill> bills) {
    this.bills = bills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Currency)) {
      return false;
    }

    Currency currency = (Currency) o;

    return new EqualsBuilder().append(id, currency.id).append(isoCode, currency.isoCode)
        .append(rateToEuro, currency.rateToEuro).append(updated, currency.updated)
        .append(countries, currency.countries).append(bills, currency.bills).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(isoCode).append(rateToEuro)
        .append(updated).append(countries).append(bills).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("isoCode", isoCode)
        .append("rateToEuro", rateToEuro).append("updated", updated).toString();
  }
}
