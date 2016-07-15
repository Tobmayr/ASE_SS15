package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Kamil Sierzant (ks)
 */
@Entity
@Table(name = "shop")
public class Shop implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(name = "logo_url")
  private String logoUrl;
  private boolean deleted;
  @OneToMany(mappedBy = "shop", fetch = FetchType.EAGER)
  private List<Bill> bills;
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "country_id")
  private Country country;

  /**
   * default constructor for hibernate
   */
  public Shop() {}

  public boolean isDeleted() {
    return deleted;
  }


  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
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

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public List<Bill> getBills() {
    return bills;
  }

  public void setBills(List<Bill> bills) {
    this.bills = bills;
  }

  public void addBill(Bill bill) {
    if (this.bills == null) {
      this.bills = new ArrayList<>();
    }
    this.bills.add(bill);
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }


  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Shop)) {
      return false;
    }

    Shop shop = (Shop) o;

    return new EqualsBuilder().append(id, shop.id).append(name, shop.name)
        .append(logoUrl, shop.logoUrl).append(bills, shop.bills).append(group, shop.group)
        .append(country, shop.country).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(logoUrl).append(bills)
        .append(group).append(country).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("logoUrl", logoUrl).toString();
  }
}
