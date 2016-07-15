package com.smartwg.core.internal.services.pipeline.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.pipeline.PipelineDTO;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class ImageBillDTO implements PipelineDTO {

  private byte[] image;
  private String conversionString;
  private Map<String, ShopDTO> shopMap;
  private Map<String, CurrencyDTO> currencyMap;
  private List<CostEntryDTO> costEntries = new ArrayList<CostEntryDTO>();
  private ShopDTO shop;
  private CurrencyDTO currency;
  private Date date;

  public ImageBillDTO(byte[] image) {
    this.image = image;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getConversionString() {
    return conversionString;
  }

  public void setConversionString(String conversionString) {
    this.conversionString = conversionString;
  }

  public ShopDTO getShop() {
    return shop;
  }

  public void setShop(ShopDTO shop) {
    this.shop = shop;
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
    this.date = date;
  }

  public Map<String, ShopDTO> getShopMap() {
    return shopMap;
  }

  public void setShopMap(List<ShopDTO> shops) {
    this.shopMap = new HashMap<String, ShopDTO>();
    for (ShopDTO dto : shops) {
      this.shopMap.put(dto.getName().toUpperCase(), dto);
    }
  }


  public Map<String, CurrencyDTO> getCurrencyMap() {
    return currencyMap;
  }

  public void setCurrencyMap(List<CurrencyDTO> currencies) {
    this.currencyMap = new HashMap<String, CurrencyDTO>();
    for (CurrencyDTO dto : currencies) {
      this.currencyMap.put(dto.getIsoCode().toUpperCase(), dto);
    }
  }

  public List<CostEntryDTO> getCostEntries() {
    return costEntries;
  }

  public void setCostEntries(List<CostEntryDTO> costEntries) {
    this.costEntries = costEntries;
  }

  public void addCostEntry(CostEntryDTO costEntryDTO) {
    this.costEntries.add(costEntryDTO);
  }



}
