package com.smartwg.core.internal.domain.dtos;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.Shop;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class ShopDTO implements Comparable<ShopDTO> {

  private Integer id;
  private String name;
  private String logoUrl;
  private List<Bill> bills;
  private boolean deleted;
  private Integer countryId;
  private Integer groupId;



  public ShopDTO(Shop shop) {
    this.id = shop.getId();
    this.name = shop.getName();
    this.logoUrl = shop.getLogoUrl();
    this.bills = shop.getBills();
    this.deleted = shop.isDeleted();
    if (shop.getCountry() != null) {
      this.countryId = shop.getCountry().getId();
    }
    if (shop.getGroup() != null) {
      this.groupId = shop.getGroup().getId();
    }
  }


  public ShopDTO() {

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

  public Integer getCountryId() {
    return countryId;
  }


  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }


  public Integer getGroupId() {
    return groupId;
  }


  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof ShopDTO))
      return false;
    ShopDTO that = (ShopDTO) obj;
    return new EqualsBuilder().append(this.id, that.id).append(this.name, that.name)
        .append(this.groupId, that.groupId).append(this.countryId, that.countryId)
        .append(this.logoUrl, that.logoUrl).append(this.deleted, that.deleted).isEquals();


  }

  @Override
  public int compareTo(ShopDTO o) {
    return this.getName().compareTo(o.getName());
  }


  public boolean isDeleted() {
    return deleted;
  }


  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }


}
