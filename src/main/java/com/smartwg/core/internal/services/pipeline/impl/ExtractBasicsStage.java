package com.smartwg.core.internal.services.pipeline.impl;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.smartwg.core.internal.services.pipeline.Stage;
import com.smartwg.core.util.Constants;

/**
 * In this stage the String result from converting a bill-image (a.k.a Conversionstring) will be
 * processed and the basic bill attributes will be extracted. Currently following attributes can be
 * determined:
 * <ul>
 * <li>Date</li>
 * <li>Shopname</li>
 * <li>Currency</li>
 * </ul>
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class ExtractBasicsStage implements Stage<ImageBillDTO> {

  /**
   * Processes the ImageBillDTO and analyzes the conversion String, trying to extract information
   * related to shop,currency and date of the bill. The dto should only be changed if valid
   * information could be extracted from the conversion String
   * 
   * Date: a Date in the format dd.mm.yyyy should be recognized. The delimiter can '.' '-' '/' or
   * ' ' in case of multipe occurences the first matching value will be used. Shop: Shops which are
   * in the ShopMap can be recognized by name Currency: Currencies which are in the CurrencyMap of
   * the dto can be recognized by isoCode
   * 
   * @param dto ImageBillDTO on which all pipeline stages are operating
   * @throws NullPointerException if the passed parameter is null
   */
  @Override
  public void process(ImageBillDTO dto) {
    if (dto.getConversionString() == null || dto.getConversionString().isEmpty())
      return;
    // Conversion to upperCase (-> extraction ignores cases)
    String data = dto.getConversionString().toUpperCase();

    Pattern pattern_date = Pattern.compile(Constants.PATTERN_DATE);
    Matcher matcher = pattern_date.matcher(data);
    // Extract Date via Regex
    if (matcher.find()) {
      String dateString = matcher.group();

      dateString = dateString.replaceAll(Constants.PATTERN_DATE_DELIMITER, ".");

      Date date = DateTime.parse(dateString, DateTimeFormat.forPattern("dd.MM.yyyy")).toDate();
      dto.setDate(date);
    }

    // Extract Shopname via regex
    if (dto.getShopMap() != null && !dto.getShopMap().isEmpty()) {

      Pattern pattern_shops = Pattern.compile(createRegex(dto.getShopMap().keySet()));
      matcher = pattern_shops.matcher(data);
      if (matcher.find()) {
        String shopName = matcher.group();

        dto.setShop(dto.getShopMap().get(shopName));
      }
    }
    // Extract currency via regex
    if (dto.getCurrencyMap() != null && !dto.getCurrencyMap().isEmpty()) {
      Pattern pattern_currency = Pattern.compile(createRegex(dto.getCurrencyMap().keySet()));
      matcher = pattern_currency.matcher(data);
      if (matcher.find()) {
        String isoCode = matcher.group();
        dto.setCurrency(dto.getCurrencyMap().get(isoCode));
      }
    }


  }

  private String createRegex(Collection<String> values) {
    String regex = "";
    for (String value : values) {
      regex += value + "|";
    }

    int length = regex.length();

    return regex.substring(0, length - 1);


  }
}
