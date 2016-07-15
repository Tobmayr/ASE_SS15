package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.smartwg.core.internal.domain.dtos.CountryDTO;

/**
 * @author Anna Sadriu (as)
 */
@Entity
@Table(name = "country")
public class Country implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, name = "iso_code")
  private String isoCode;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  private List<Group> groups = new ArrayList<Group>();

  @ManyToOne
  @JoinColumn(name = "default_currency")
  private Currency currency;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  private List<Shop> shops = new ArrayList<Shop>();

  public Country() {}

  public Country(String name, String isoCode) {
    this.name = name;
    this.isoCode = isoCode;
  }

  public Country(Integer id, String name, String isoCode) {
    this.id = id;
    this.name = name;
    this.isoCode = isoCode;
  }



  public Country(CountryDTO country) {
    this.id = country.getId();
    this.name = country.getName();
    this.isoCode = country.getIsoCode();
  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String iso_code) {
    this.isoCode = iso_code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public void addGroup(Group group) {
    if (this.groups == null) {
      this.groups = new ArrayList<>();
    }
    this.groups.add(group);
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public List<Shop> getShops() {
    return shops;
  }

  public void setShops(List<Shop> shops) {
    this.shops = shops;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Country)) {
      return false;
    }

    Country country = (Country) o;

    return new EqualsBuilder().append(id, country.id).append(isoCode, country.isoCode)
        .append(name, country.name).append(groups, country.groups)
        .append(currency, country.currency).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(isoCode).append(name).append(groups)
        .append(currency).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("isoCode", isoCode)
        .append("name", name).append("currency", currency).toString();
  }
}
