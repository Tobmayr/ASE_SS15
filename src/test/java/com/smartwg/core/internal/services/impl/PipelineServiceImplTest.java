package com.smartwg.core.internal.services.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.PipelineService;
import com.smartwg.core.internal.services.pipeline.impl.ImageBillDTO;
import com.smartwg.core.internal.services.pipeline.impl.ImageBillPipeline;

@Test
public class PipelineServiceImplTest {
  @InjectMocks
  private PipelineService service;
  @Mock
  private ImageBillPipeline pipeline;
  @Mock
  private BillDTO billDTO;
  @Mock
  private List<CostEntryDTO> mockList;


  @Mock
  private ResourceDTO resourceDTO;


  @BeforeMethod
  private void beforeMethod() {
    service = new PipelineServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void doImageToBillPipeline_null() {
    final BillDTO result = service.doImageToBillPipeline(null, null, null);
    assertEquals(result, new BillDTO());
  }

  public void doImageToBillPipeline_valid() {
    final ShopDTO shop = new ShopDTO();
    ArgumentCaptor<ImageBillDTO> argument = ArgumentCaptor.forClass(ImageBillDTO.class);
    shop.setName("LIBRO");
    final CurrencyDTO currency = new CurrencyDTO();
    currency.setIsoCode("EUR");
    final List<ShopDTO> shops = Arrays.asList(shop);
    final List<CurrencyDTO> currencies = Arrays.asList(currency);
    final BillDTO result = service.doImageToBillPipeline(resourceDTO, shops, currencies);
    verify(pipeline, times(1)).doPipeline(argument.capture());
    argument.getValue().setCostEntries(mockList);
    final Map<String, ShopDTO> shopMap = argument.getValue().getShopMap();
    assertEquals(shopMap.get(shop.getName()), shop);
    final Map<String, CurrencyDTO> curMap = argument.getValue().getCurrencyMap();
    assertEquals(curMap.get(currency.getIsoCode()), currency);


  }

}
