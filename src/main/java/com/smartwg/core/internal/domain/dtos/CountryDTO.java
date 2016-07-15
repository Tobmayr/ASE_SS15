package com.smartwg.core.internal.domain.dtos;

import java.util.ResourceBundle;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.internal.domain.entities.Country;
import com.smartwg.core.util.Constants;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author xsk
 */
public class CountryDTO {
  private ResourceBundle ms = SmartWG.getMessageBundle();
  private Integer id;
  private String isoCode;

  private String name;

  private CurrencyDTO currency;

  public CountryDTO() {}

  public CountryDTO(Integer id, String isoCode, String name) {
    this.id = id;
    this.isoCode = isoCode;
    this.name = name;
  }

  public CountryDTO(final Country country) {
    this.id = country.getId();
    this.isoCode = country.getIsoCode();
    this.name = country.getName();
    if (country.getCurrency() != null) {
      this.currency =
          new CurrencyDTO(country.getCurrency().getId(), country.getCurrency().getIsoCode());
    }
  }

  public String getLocalizedName() {
    return ms.getString(name);
  }

  public CurrencyDTO getCurrency() {
    return currency;
  }

  public void setCurrency(CurrencyDTO currency) {
    this.currency = currency;
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

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof CountryDTO))
      return false;
    CountryDTO that = (CountryDTO) obj;
    return new EqualsBuilder().append(this.id, that.id).append(this.isoCode, that.isoCode)
        .append(this.name, that.name).isEquals();


  };


  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", name).append("isoCode", isoCode)
        .append("id", id).toString();
  }
}
