package com.smartwg.core.internal.services.pipeline.impl;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import util.TestDataConstants;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.services.pipeline.Stage;

@Test
public class ExtractCostentryStageTest {

  private Stage<ImageBillDTO> stage;

  private ImageBillDTO dto;

  @BeforeMethod
  public void init() throws IOException {
    stage = new ExtractCostentryStage();
    dto = new ImageBillDTO(null);

  }

  @Test(expectedExceptions = {NullPointerException.class})
  public void process_null() {
    stage.process(null);
  }

  // If the conversionString of the dto null, no exception should occur and no data should be
  // changed
  public void process_conversionStringNull() {
    stage.process(dto);
    assertEquals(dto.getCostEntries(), Collections.EMPTY_LIST);
  }

  public void process_conversionStringEmpty() {
    dto.setConversionString("");
    stage.process(dto);
    assertEquals(dto.getCostEntries(), Collections.EMPTY_LIST);
  }


  @Test(dataProvider = "validCostentries")
  public void process_conversionStringMatches(String conversionString, List<CostEntryDTO> result) {
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertEquals(result, dto.getCostEntries());
  }

  @DataProvider(name = "validCostentries")
  public Object[][] generateValidEntries() {
    String s1 = "Stiegel Goldbräu 0.5 A 0.89\nPesto190 G A 0.43\nBackkäse B 1,99";
    System.out.println(s1);
    String s2 = "Stiegel Goldbräu 0.5 F 0.89\nPesto190 G A 0.43\nBackkäse A 1.99";
    String s3 = "Stiegel Goldbräu 0.5 0.89 C\nPesto190 G 0.43 F\nBackkäse 1.99 F";
    CostEntryDTO c1 = new CostEntryDTO();
    CostEntryDTO c2 = new CostEntryDTO();
    CostEntryDTO c3 = new CostEntryDTO();
    c1.setName("Stiegel Goldbräu 0.5");
    c1.setAmount(new BigDecimal(0.89));
    c2.setName("Pesto190 G");
    c2.setAmount(new BigDecimal(0.43));
    c3.setName("Backkäse");
    c3.setAmount(new BigDecimal(1.99));

    List<CostEntryDTO> result = Arrays.asList(c1, c2, c3);

    return new Object[][] { {s1, result}, {s2, result}, {s3, result}};
  }


  public void process_withRealData() {
    dto.setConversionString(TestDataConstants.DATA2_CONVERSION_STRING);
    stage.process(dto);
    assertEquals(dto.getCostEntries(), TestDataConstants.DATA2_COSTENTRIES);
  }


}
