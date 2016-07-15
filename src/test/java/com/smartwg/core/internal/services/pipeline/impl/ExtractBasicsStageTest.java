package com.smartwg.core.internal.services.pipeline.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import util.TestDataConstants;

import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;


@Test
public class ExtractBasicsStageTest {
  @InjectMocks
  private ExtractBasicsStage stage;



  final String SHOPNAME = "Libro";
  final String ISO_CODE = "EUR";
  private ShopDTO shopDTO;
  private CurrencyDTO currencyDTO;
  private ImageBillDTO dto;

  @BeforeMethod
  public void init() throws IOException {
    MockitoAnnotations.initMocks(this);
    stage = new ExtractBasicsStage();
    dto = new ImageBillDTO(null);
    shopDTO = new ShopDTO();
    shopDTO.setName(SHOPNAME);
    currencyDTO = new CurrencyDTO();
    currencyDTO.setIsoCode(ISO_CODE);
    dto.setShopMap(Arrays.asList(shopDTO));
    dto.setCurrencyMap(Arrays.asList(currencyDTO));

  }

  // Expected to throw a NullpointerException
  @Test(expectedExceptions = {NullPointerException.class})
  public void process_withNull() {
    stage.process(null);
  }

  // If the conversionString of the dto null, no exception should occur and no data should be
  // changed

  public void process_conversionStringNull() {

    stage.process(dto);
    assertNull(dto.getDate());
    assertNull(dto.getCurrency());
    assertNull(dto.getShop());
  }

  public void process_conversionStringEmpty() {
    dto.setConversionString("");
    stage.process(dto);
    assertNull(dto.getDate());
    assertNull(dto.getCurrency());
    assertNull(dto.getShop());
  }

  // If the conversionString doesnt contain valid information, no changed on the dto should occur
  public void process_noMatchingData() {
    final String noMatch =
        "A String witch contains rand1232nasd435dom characters but no valid information";
    dto.setConversionString(noMatch);
    stage.process(dto);
    assertNull(dto.getDate());
    assertNull(dto.getCurrency());
    assertNull(dto.getShop());
  }

  @Test(dataProvider = "dateData")
  public void process_matchingDate(String conversionString) {
    final Date expected = DateTime.parse("2015-05-25").toDate();
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertEquals(dto.getDate(), expected);

  }

  @DataProvider(name = "dateData")
  public Object[][] getDateStrings() {
    return new Object[][] { {"25.05.2015"}, {"25-05-2015"}, {"25 05 2015"}, {"25/05/2015"}};
  }

  @Test(dataProvider = "invalidDateData")
  public void process_noMatchingDate(String conversionString) {
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertNull(dto.getDate());
  }



  @DataProvider(name = "invalidDateData")
  public Object[][] getInvalidDateStrings() {
    return new Object[][] { {"25,05.2015"}, {"25052015"}, {"32.05.2015"}, {"25/13/2015"},
        {"25.05.1899"}};
  }


  public void process_multipleDates() {
    final Date expected = DateTime.parse("2015-06-26").toDate();
    final String conversionString = "26.06.2015 , 25.05.2015";
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertEquals(dto.getDate(), expected);
  }

  public void process_matchingShop() {
    final String conversionString = "filler" + SHOPNAME + "filler";
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertEquals(dto.getShop(), shopDTO);
  }

  public void process_matchingCurrency() {
    final String conversionString = "filler" + ISO_CODE + "filler";
    dto.setConversionString(conversionString);
    stage.process(dto);
    assertEquals(dto.getCurrency(), currencyDTO);
  }

  public void process_fullConversionString() {

    dto.setConversionString(TestDataConstants.DATA1_CONVERSION_STRING);
    stage.process(dto);
    assertEquals(dto.getDate(), TestDataConstants.DATA1_DATE);
    assertEquals(dto.getShop(), shopDTO);
    assertEquals(dto.getCurrency(), currencyDTO);
  }
}
