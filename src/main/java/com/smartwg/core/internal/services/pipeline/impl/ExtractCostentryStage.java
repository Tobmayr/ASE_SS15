package com.smartwg.core.internal.services.pipeline.impl;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.services.pipeline.Stage;
import com.smartwg.core.util.Constants;

/**
 * In this stage the String result from converting a bill-image (a.k.a Conversionstring) will be
 * processed and the separate bill positions will be processed.
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class ExtractCostentryStage implements Stage<ImageBillDTO> {

  /**
   * Processes the ImageBillDTO and analyzes the conversion String, trying to extract information
   * related to separate Costentries (items of the bill).The dto should only be changed if valid
   * information could be extracted from the conversion String Costentry: a Costentry of a typical
   * Bill-receipt is in the following format {Descriptiontext}{Character-Code}{Price}{optional:
   * Charactercode if not before the price} e.g. Icream Cookies 200g A 1.99
   * 
   * @param dto ImageBillDTO on which all pipeline stages are operating
   * @throws NullPointerException if the passed parameter is null
   */
  @Override
  public void process(ImageBillDTO dto) {
    if (dto.getConversionString() == null || dto.getConversionString().isEmpty())
      return;
    Pattern pattern = Pattern.compile(Constants.PATTERN_COSTENTRY);

    Pattern pattern_amount = Pattern.compile(Constants.PATTERN_AMOUNT);
    Scanner sc = new Scanner(dto.getConversionString());
    String currentLine = "";
    Matcher matcher = null;
    while (sc.hasNextLine()) {
      currentLine = sc.nextLine();
      System.out.println(currentLine);
      matcher = pattern.matcher(currentLine);
      if (matcher.find()) {
        CostEntryDTO costEntry = new CostEntryDTO();
        String match = matcher.group();
        System.out.println("match: " + match);
        matcher = pattern_amount.matcher(match);
        if (matcher.find()) {
          String amountString = matcher.group();

          BigDecimal amount = new BigDecimal(Double.parseDouble(amountString.replace(",", ".")));
          costEntry.setAmount(amount);
          int index = match.indexOf(amountString);
          String name;
          if (index + amountString.length() == match.length()) {
            name = match.substring(0, index - 3);
          } else {
            name = match.substring(0, index - 1);
          }
          costEntry.setName(name);

        }
        dto.addCostEntry(costEntry);
      }
    }
    sc.close();
  }
}
