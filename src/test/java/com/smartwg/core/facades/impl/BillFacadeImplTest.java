package com.smartwg.core.facades.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.BillService;
import com.smartwg.core.internal.services.PipelineService;

/**
 * Mehthods which just delegate to the underlying service are not tested
 * 
 * @author Tobias Ortmayr, to
 *
 */
@Test
public class BillFacadeImplTest {

  @InjectMocks
  private BillFacade facade;
  @Mock
  private BillService billService;

  @Mock
  private BillDTO billDTO;
  @Mock
  private CostEntryDTO costEntryDTO;
  @Mock
  private ResourceDTO resourceDTO;
  @Mock
  PipelineService pipelineService;
  @Mock
  private List<ShopDTO> shops;
  @Mock
  private List<CurrencyDTO> currencies;

  @BeforeMethod
  public void beforeMethod() {
    facade = new BillFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void saveBill_newBill() {
    when(billDTO.getId()).thenReturn(null);
    facade.saveBill(billDTO);
    verify(billService, times(1)).createBill(eq(billDTO));
  }

  public void saveBill_existingBill() {
    when(billDTO.getId()).thenReturn(1);
    facade.saveBill(billDTO);
    verify(billService, times(1)).editBill(eq(billDTO));
  }

  public void calculateTotal_Succed() {
    final BillDTO bill1 = new BillDTO();
    bill1.setTotal(new BigDecimal(20.00));
    final BillDTO bill2 = new BillDTO();
    bill2.setTotal(new BigDecimal(30.00));

    final BigDecimal result = facade.calculateTotal(Arrays.asList(bill1, bill2));

    assertEquals(result, new BigDecimal(50.00));
  }

  public void calculateTotal_WithNull() {
    final BigDecimal result = facade.calculateTotal(null);
    assertEquals(result, null);
  }

  public void calculateTotal_EmptyList() {
    final BigDecimal result = facade.calculateTotal(new ArrayList<BillDTO>());
    assertEquals(result, new BigDecimal(0));
  }

  public void calculateBillTotal_succeed() {

    final CostEntryDTO costEntryDTO = new CostEntryDTO();
    costEntryDTO.setAmount(new BigDecimal(10.0));
    final CostEntryDTO costEntryDTO2 = new CostEntryDTO();
    costEntryDTO2.setAmount(new BigDecimal(20.0));
    costEntryDTO2.setExcluded(true);
    final BillDTO bill1 = new BillDTO();
    bill1.setCostEntries(Arrays.asList(costEntryDTO, costEntryDTO2));

    final BigDecimal[] result = facade.calcualteBillTotal(bill1);
    assertEquals(result[0], new BigDecimal(10.0).setScale(2, BigDecimal.ROUND_HALF_UP));
    assertEquals(result[1], new BigDecimal(20.0).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  public void calculateBillTotal_NoCostEntries_succeed() {
    final BillDTO bill = new BillDTO();
    bill.setTotal(new BigDecimal(123.30));
    final BigDecimal[] result = facade.calcualteBillTotal(bill);
    assertEquals(result[0], new BigDecimal(123.30));
    assertEquals(result[1], new BigDecimal(0.0));
  }

  public void calculateBillTotal_PrivatBill_succeed() {
    final CostEntryDTO costEntryDTO = new CostEntryDTO();
    costEntryDTO.setAmount(new BigDecimal(10.0));
    final CostEntryDTO costEntryDTO2 = new CostEntryDTO();
    costEntryDTO2.setAmount(new BigDecimal(20.0));
    final BillDTO bill1 = new BillDTO();
    bill1.setCostEntries(Arrays.asList(costEntryDTO, costEntryDTO2));
    bill1.setPrivateBill(true);
    final BigDecimal[] result = facade.calcualteBillTotal(bill1);
    assertEquals(result[0], new BigDecimal(0.0));
    assertEquals(result[1], new BigDecimal(30).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  public void calculateBillTotal_PrivateBill_NoCostEntries_succeed() {
    final BillDTO bill = new BillDTO();
    bill.setTotal(new BigDecimal(40.4));
    bill.setPrivateBill(true);
    final BigDecimal[] result = facade.calcualteBillTotal(bill);
    assertEquals(result[0], new BigDecimal(0.0));
    assertEquals(result[1], new BigDecimal(40.4));
  }

  public void calcualteBilLTotal_WithNull() {
    final BigDecimal[] result = facade.calcualteBillTotal(null);
    assertEquals(result, null);
  }

  public void editBill() {
    facade.editBill(billDTO);
    verify(billService, times(1)).editBill(eq(billDTO));
  }

  public void deleteBill() {
    facade.deleteBill(billDTO);
    verify(billService, times(1)).deleteBill(billDTO);
  }

  public void getBillBetweenTimeSpan_1() {
    final Date start = new Date();
    final Date end = new Date();
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billService.findBillsBetweenTimespan(start, end, 1)).thenReturn(expected);
    final List<BillDTO> result = facade.getBillsBetweenTimespan(start, end, 1);
    verify(billService, times(1)).findBillsBetweenTimespan(start, end, 1);
    assertEquals(result, expected);
  }

  public void getBillBetweenTimeSpan_2() {
    final Date start = new Date();
    final Date end = new Date();
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billService.findPrivateBillsBetweenTimespan(start, end, 1, 1)).thenReturn(expected);
    final List<BillDTO> result = facade.getPrivateBillsBetweenTimespan(start, end, 1, 1);
    verify(billService, times(1)).findPrivateBillsBetweenTimespan(start, end, 1, 1);
    assertEquals(result, expected);
  }

  public void getBillBetweenTimeSpanWithCostentries() {
    final Date start = new Date();
    final Date end = new Date();
    final Integer GROUP_ID = 2;
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billService.findBillsWithCostEntriesBetweenTimespan(start, end, GROUP_ID)).thenReturn(
        expected);
    final List<BillDTO> result =
        facade.getBillsWithCostEntriesBetweenTimespan(start, end, GROUP_ID);
    verify(billService, times(1)).findBillsWithCostEntriesBetweenTimespan(start, end, GROUP_ID);
    assertEquals(result, expected);
  }

  public void getPrivateBillsBetweenTimespan() {
    final Date start = new Date();
    final Date end = new Date();
    final Integer GROUP_ID = 4;
    final Integer USER_ID = 2;
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billService.findPrivateBillsBetweenTimespan(start, end, GROUP_ID, USER_ID)).thenReturn(
        expected);
    final List<BillDTO> result =
        facade.getPrivateBillsBetweenTimespan(start, end, GROUP_ID, USER_ID);
    verify(billService, times(1)).findPrivateBillsBetweenTimespan(start, end, GROUP_ID, USER_ID);
    assertEquals(result, expected);
  }

  public void findById() {
    when(billService.findById(254)).thenReturn(billDTO);
    final BillDTO result = facade.findById(254);
    verify(billService, times(1)).findById(254);
    assertEquals(result, billDTO);
  }

  public void getCostEntries() {
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO);
    when(billService.getCostEntries(24)).thenReturn(expected);
    List<CostEntryDTO> result = facade.getCostEntries(24);
    verify(billService, times(1)).getCostEntries(24);
    assertEquals(result, expected);
  }

  public void findByParameters() {
    final Date start = new Date();
    final Date end = new Date();
    final Integer GROUP_ID = 4;
    final Integer USER_ID = 2;
    final Integer SHOP_ID = 45;
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billService.findByParameters(GROUP_ID, USER_ID, start, end, SHOP_ID)).thenReturn(expected);
    final List<BillDTO> result = facade.findByParameters(GROUP_ID, USER_ID, start, end, SHOP_ID);
    verify(billService, times(1)).findByParameters(GROUP_ID, USER_ID, start, end, SHOP_ID);
    assertEquals(result, expected);
  }

  public void findCostEntriesByParameters() {
    final Date start = new Date();
    final Date end = new Date();
    final Integer GROUP_ID = 4;
    final Integer USER_ID = 2;
    final Integer SHOP_ID = 45;
    final Integer CATEGORY_ID = 21;
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO);
    when(billService.findCostEntryByParameters(GROUP_ID, USER_ID, start, end, CATEGORY_ID, SHOP_ID))
        .thenReturn(expected);
    final List<CostEntryDTO> result =
        facade.findCostEntryByParameters(GROUP_ID, USER_ID, start, end, CATEGORY_ID, SHOP_ID);
    verify(billService, times(1)).findCostEntryByParameters(GROUP_ID, USER_ID, start, end,
        CATEGORY_ID, SHOP_ID);
    assertEquals(result, expected);
  }

  public void getPrivateCostEntries() {
    final Integer BILL_ID = 21;
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO);
    when(billService.getPrivateCostEntries(BILL_ID)).thenReturn(expected);
    final List<CostEntryDTO> result = facade.getPrivateCostEntries(BILL_ID);
    verify(billService, times(1)).getPrivateCostEntries(BILL_ID);
    assertEquals(result, expected);
  }

  public void doImageRecognition_validMimetype() {
    when(pipelineService.doImageToBillPipeline(resourceDTO, shops, currencies)).thenReturn(billDTO);
    when(resourceDTO.getType()).thenReturn("image/jpg");
    final BillDTO result = facade.doImageRecognition(resourceDTO, shops, currencies);
    verify(pipelineService, times(1)).doImageToBillPipeline(resourceDTO, shops, currencies);
    assertEquals(result, billDTO);
  }

  public void doImageRecognition_invalidMimetype() {
    when(resourceDTO.getType()).thenReturn("application/pdf");
    final BillDTO result = facade.doImageRecognition(resourceDTO, shops, currencies);
    verify(pipelineService, times(0)).doImageToBillPipeline(resourceDTO, shops, currencies);
    assertEquals(result, null);
  }
}
