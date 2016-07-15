package com.smartwg.core.internal.domain.dtos;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class CurrencyDTO {

  private Integer id;
  private String isoCode;


  public CurrencyDTO(final Integer id, final String isoCode) {
    super();
    this.id = id;
    this.isoCode = isoCode;
  }


  public CurrencyDTO() {}

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof CurrencyDTO))
      return false;
    CurrencyDTO that = (CurrencyDTO) obj;
    return new EqualsBuilder().append(this.id, that.id).append(this.isoCode, that.isoCode)
        .isEquals();
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


}
