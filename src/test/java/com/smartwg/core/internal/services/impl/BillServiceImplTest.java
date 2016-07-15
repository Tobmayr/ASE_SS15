package com.smartwg.core.internal.services.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.NotificationType;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.repositories.BillRepository;
import com.smartwg.core.internal.repositories.CostEntryRepository;
import com.smartwg.core.internal.services.BillService;
import com.smartwg.core.internal.services.EmailService;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.UserService;

@Test
public class BillServiceImplTest {

  @InjectMocks
  private BillService service;

  @Mock
  private BillRepository billRepository;
  @Mock
  private CostEntryRepository costEntryRepository;
  @Mock
  private EntityConverter entityFactory;
  @Mock
  private BillDTO billDTO;
  @Mock
  private Bill bill;
  @Mock
  private BillDTO billDTO2;
  @Mock
  private CostEntryDTO costEntryDTO;
  @Mock
  private CostEntryDTO costEntryDTO2;
  @Mock
  EmailService emailService;
  @Mock
  UserService userService;


  @BeforeMethod
  public void setUp() {
    service = new BillSerivceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void createBill_Succeed() {
    when(entityFactory.createBill(billDTO)).thenReturn(bill);
    when(billDTO.isPrivateBill()).thenReturn(true);
    service.createBill(billDTO);
    verify(entityFactory, times(1)).createBill(billDTO);
    verify(billRepository, times(1)).save(eq(bill));
  }

  public void createBill_SendEmailNotification() {
    final String reciepient = "HiThere";
    final List<String> expected = Arrays.asList(reciepient);
    when(entityFactory.createBill(billDTO)).thenReturn(bill);
    when(billDTO.isPrivateBill()).thenReturn(false);
    when(billDTO.getId()).thenReturn(null);
    when(billDTO.getGroupId()).thenReturn(4);
    when(userService.findUsersEmailsByGroupId(4, NotificationType.BILL)).thenReturn(expected);
    service.createBill(billDTO);
    verify(emailService, times(1)).createAndSendBillCreatedEmail(billDTO,
        new HashSet<String>(expected));
    verify(entityFactory, times(1)).createBill(billDTO);
    verify(billRepository, times(1)).save(eq(bill));
  }

  public void editBill() {
    when(entityFactory.createBill(billDTO)).thenReturn(bill);
    service.editBill(billDTO);
    verify(entityFactory, times(1)).createBill(billDTO);
    verify(billRepository, times(1)).merge(bill);
  }

  public void deleteBill() {
    ArgumentCaptor<Bill> billArgumentCaptor = ArgumentCaptor.forClass(Bill.class);
    when(billDTO.getId()).thenReturn(23);

    service.deleteBill(billDTO);

    verify(billRepository).delete(billArgumentCaptor.capture());
    assertEquals(billArgumentCaptor.getValue().getId(), Integer.valueOf(23));
  }

  public void findBillsBetweenTimespan_Succeed() {
    final Date now = new Date();
    final Date tomorrow = DateUtils.addDays(now, 1);
    when(billRepository.getBillsBetweenTimespan(now, tomorrow, 2)).thenReturn(
        Arrays.<BillDTO>asList(billDTO, billDTO2));

    final List<BillDTO> result = service.findBillsBetweenTimespan(now, tomorrow, 2);

    assertEquals(result, Arrays.<BillDTO>asList(billDTO, billDTO2));
  }

  public void findBillBetweenTimespanWithCostentries() {
    final Date start = new Date();
    final Date end = DateUtils.addDays(start, 1);
    final Integer group_id = 3;
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billRepository.getBillsWithCostEntriesBetweenTimespan(start, end, group_id)).thenReturn(
        expected);
    final List<BillDTO> result =
        service.findBillsWithCostEntriesBetweenTimespan(start, end, group_id);
    verify(billRepository, times(1)).getBillsWithCostEntriesBetweenTimespan(start, end, group_id);
    assertEquals(result, expected);
  }

  public void findeBillsBetweenTimespan_forUser_Succeed() {
    final Date now = new Date();
    final Date tomorrow = DateUtils.addDays(now, 1);
    when(billRepository.getPrivateBillsBetweenTimespan(now, tomorrow, 2, 1)).thenReturn(
        Arrays.<BillDTO>asList(billDTO, billDTO2));

    final List<BillDTO> result = service.findPrivateBillsBetweenTimespan(now, tomorrow, 2, 1);

    assertEquals(result, Arrays.<BillDTO>asList(billDTO, billDTO2));
  }

  public void findBillsBetweenTimespan_StartNull_Fail() {
    final Date now = new Date();
    final Date tomorrow = DateUtils.addDays(now, 1);

    when(billRepository.getBillsBetweenTimespan(null, tomorrow, 2)).thenReturn(
        new ArrayList<BillDTO>());
    final List<BillDTO> result = service.findBillsBetweenTimespan(null, tomorrow, 2);
    assertEquals(result, Collections.EMPTY_LIST);
  }

  public void findBillBetweenTimespan_EndBeforeStart_Fail() {
    final Date now = new Date();
    final Date tomorrow = DateUtils.addDays(now, 1);
    when(billRepository.getBillsBetweenTimespan(tomorrow, now, 2)).thenReturn(
        new ArrayList<BillDTO>());

    final List<BillDTO> result = service.findBillsBetweenTimespan(tomorrow, now, 2);

    assertEquals(result, Collections.EMPTY_LIST);
  }

  public void findBillsBetweenTimespan_EndNull_Fail() {
    final Date now = new Date();
    when(billRepository.getBillsBetweenTimespan(now, null, 2)).thenReturn(new ArrayList<BillDTO>());
    final List<BillDTO> result = service.findBillsBetweenTimespan(now, null, 2);
    assertEquals(result, Collections.EMPTY_LIST);
  }

  public void findById_Succeed() {
    when(billRepository.findBillById(1)).thenReturn(billDTO);
    final BillDTO result = service.findById(1);
    assertEquals(result, billDTO);
  }

  public void findById_Fails() {
    when(billRepository.findBillById(1)).thenReturn(null);
    final BillDTO result = service.findById(1);
    assertEquals(result, null);
  }

  public void getCostEntries_Succeed() {
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO, costEntryDTO2);
    when(costEntryRepository.getCostEntries(1)).thenReturn(expected);
    final List<CostEntryDTO> result = service.getCostEntries(1);
    assertEquals(result, expected);
  }

  public void getCostEntries_Fail() {
    final List<CostEntryDTO> expected = new ArrayList<CostEntryDTO>();
    when(costEntryRepository.getCostEntries(1)).thenReturn(expected);
    final List<CostEntryDTO> result = service.getCostEntries(1);
    assertEquals(result, expected);
  }

  public void getPrivateCostentries() {
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO);
    when(costEntryRepository.getPrivateCostEntries(4)).thenReturn(expected);
    final List<CostEntryDTO> result = service.getPrivateCostEntries(4);
    verify(costEntryRepository, times(1)).getPrivateCostEntries(4);
    assertEquals(result, expected);
  }

  public void findByParameters() {
    final Integer group_id = 34;
    final Integer createdBy_id = 3;
    final Date start = new Date();
    final Date end = new Date();
    final Integer shop_id = 3;
    final List<BillDTO> expected = Arrays.asList(billDTO);
    when(billRepository.findByParameters(group_id, createdBy_id, start, end, shop_id)).thenReturn(
        expected);
    final List<BillDTO> result =
        service.findByParameters(group_id, createdBy_id, start, end, shop_id);
    verify(billRepository, times(1)).findByParameters(group_id, createdBy_id, start, end, shop_id);
    assertEquals(result, expected);
  }

  public void findCostEntriesByParameters() {
    final Integer group_id = 34;
    final Integer createdBy_id = 3;
    final Date start = new Date();
    final Date end = new Date();
    final Integer category_id = 45;
    final Integer shop_id = 3;
    final List<CostEntryDTO> expected = Arrays.asList(costEntryDTO2);
    when(
        costEntryRepository.findCostEntryByParameters(group_id, createdBy_id, start, end,
            category_id, shop_id)).thenReturn(expected);
    List<CostEntryDTO> result =
        service.findCostEntryByParameters(group_id, createdBy_id, start, end, category_id, shop_id);
    verify(costEntryRepository, times(1)).findCostEntryByParameters(group_id, createdBy_id, start,
        end, category_id, shop_id);
    assertEquals(result, expected);

  }

}
