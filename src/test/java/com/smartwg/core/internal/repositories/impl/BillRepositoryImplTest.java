package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.Currency;
import com.smartwg.core.internal.domain.entities.Group;
import com.smartwg.core.internal.domain.entities.User;
import com.smartwg.core.internal.repositories.BillRepository;
import com.smartwg.core.util.Constants;

/**
 * 
 * @author Oezde Simsek (os)
 *
 */
@Test
public class BillRepositoryImplTest {

  @InjectMocks
  private BillRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private BillDTO billDTO;
  @Mock
  private Bill bill;
  @Mock
  private User user;
  @Mock
  private Group group;
  @Mock
  private Currency currency;
  
  private static final Integer GROUP_ID = 1;
  private static final Integer BILL_ID = 2;

  @BeforeMethod
  public void setUp() {
    repository = new BillRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getBillBetweenTimespan() {
    final Date start = new Date();
    final Date end = new Date();

    when(entityManager.createNamedQuery(eq(Constants.QUERY_COLLECTIVE_BILLS_TIMESPAN))).thenReturn(
        query);
    when(query.setParameter("start", start)).thenReturn(query);
    when(query.setParameter("end", end)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(billDTO));

    final List<BillDTO> result = repository.getBillsBetweenTimespan(start, end, GROUP_ID);
    

    assertEquals(result, Collections.singletonList(billDTO));

  }
  
  public void getBillsWithCostEntriesBetweenTimespan(){
    final Date start = new Date();
    final Date end = new Date();

    when(entityManager.createNamedQuery(eq(Constants.QUERY_COLLECTIVE_BILLS_WITH_COST_ENTRIES_TIMESPAN))).thenReturn(
        query);
    when(query.setParameter("start", start)).thenReturn(query);
    when(query.setParameter("end", end)).thenReturn(query);
    when(query.setParameter("group", GROUP_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(billDTO));

    final List<BillDTO> result = repository.getBillsWithCostEntriesBetweenTimespan(start, end, GROUP_ID);
    

    assertEquals(result, Collections.singletonList(billDTO));
  }

  public void findBillById() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_FIND_BILL_BY_ID))).thenReturn(query);
    when(query.setParameter("id", BILL_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(billDTO));
    final BillDTO result = repository.findBillById(BILL_ID);
    assertEquals(result, Collections.singletonList(billDTO).get(0));
  }
  
  @DataProvider(name = "parameters")
  private Object[][] getParameters() {
    return new Object[][] {
        {
            new Date(),
            new Date(),
            "SELECT b FROM Bill b where b.group.id="
                + GROUP_ID
                + " AND b.createdBy.id=3 AND b.shop.id=1 AND ( b.date BETWEEN :start AND :end) ORDER BY b.date DESC"},
        
        {
           new Date(),
           null,
           "SELECT b FROM Bill b where b.group.id="
                 + GROUP_ID
                 + " AND b.createdBy.id=3 AND b.shop.id=1 AND ( b.date >= :start) ORDER BY b.date DESC"},
             
    
        {
            null,
            new Date(),
            "SELECT b FROM Bill b where b.group.id="
                + GROUP_ID
                + " AND b.createdBy.id=3 AND b.shop.id=1 AND ( b.date <= :end) ORDER BY b.date DESC"},
        {
            null,
            null,
            "SELECT b FROM Bill b where b.group.id=" + GROUP_ID
                + " AND b.createdBy.id=3 AND b.shop.id=1 ORDER BY b.date DESC"},};
  }
  
  @Test(dataProvider = "parameters")
  public void findByParameters(Date start, Date end, String expectedQuery){
    when(entityManager.createQuery(eq(expectedQuery))).thenReturn(query);
    when(query.setParameter("start", start, TemporalType.DATE)).thenReturn(query);
    when(query.setParameter("end", end, TemporalType.DATE)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(bill));
    when(bill.getCreatedBy()).thenReturn(user);
    when(bill.getGroup()).thenReturn(group);
    when(bill.getCurrency()).thenReturn(currency);

    final List<BillDTO> result =
        repository.findByParameters(GROUP_ID, 3, start, end, 1);
    

    assertEquals(result.get(0).getCreatedBy_id(), user.getId());
  }
}
