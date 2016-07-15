package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.Bill;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.internal.domain.entities.CostEntry;
import com.smartwg.core.internal.repositories.CostEntryRepository;
import com.smartwg.core.util.Constants;

/**
 * 
 * @author ozdesimsek
 *
 */
@Test
public class CostEntryRepositoryImplTest {
  
  @InjectMocks
  private CostEntryRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private CostEntryDTO costEntryDTO;
  @Mock
  private CostEntry costEntry;
  @Mock
  private Category category;
  @Mock
  private Bill bill;
  
  private static final Integer GROUP_ID = 1;
  private static final Integer BILL_ID = 2;
  
  @BeforeMethod
  public void beforeMethod() {
    repository = new CostEntryRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void getCostEntries() {
    when(entityManager.createNamedQuery(eq(Constants.QUERY_COSTENTRIES_BY_ID))).thenReturn(
        query);
    when(query.setParameter("id", BILL_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(costEntryDTO));
    
    final List<CostEntryDTO> result = repository.getCostEntries(BILL_ID);    

    assertEquals(result, Collections.singletonList(costEntryDTO));
  }
  
  public void getPrivateCostEntries(){
    when(entityManager.createNamedQuery(eq(Constants.QUERY_COSTENTRIES_BY_ID))).thenReturn(
        query);
    when(query.setParameter("id", BILL_ID)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(costEntryDTO));
    final List<CostEntryDTO> tmp = new ArrayList<CostEntryDTO>();
    for(CostEntryDTO c : Collections.singletonList(costEntryDTO)){
      if(c.isExcluded()){
        tmp.add(c);
      }
    }   
    final List<CostEntryDTO> result = repository.getPrivateCostEntries(BILL_ID);
    
    assertEquals(result, tmp);
     
  }
  
  @DataProvider(name = "parameters")
  private Object[][] getParameters() {
    return new Object[][] {
        {
            new Date(),
            new Date(),
            "SELECT c FROM CostEntry c where c.bill.group.id="
                + GROUP_ID
                + " AND c.bill.createdBy.id=3 AND c.bill.shop.id=1 AND c.category.id=7 AND ( c.bill.date BETWEEN :start AND :end) ORDER BY c.bill.date DESC"},
        
        {
           new Date(),
           null,
           "SELECT c FROM CostEntry c where c.bill.group.id="
                 + GROUP_ID
                 + " AND c.bill.createdBy.id=3 AND c.bill.shop.id=1 AND c.category.id=7 AND ( c.bill.date >= :start) ORDER BY c.bill.date DESC"},
             
    
        {
            null,
            new Date(),
            "SELECT c FROM CostEntry c where c.bill.group.id="
                + GROUP_ID
                + " AND c.bill.createdBy.id=3 AND c.bill.shop.id=1 AND c.category.id=7 AND ( c.bill.date <= :end) ORDER BY c.bill.date DESC"},
        {
            null,
            null,
            "SELECT c FROM CostEntry c where c.bill.group.id=" + GROUP_ID
                + " AND c.bill.createdBy.id=3 AND c.bill.shop.id=1 AND c.category.id=7 ORDER BY c.bill.date DESC"},};
  }
  
  @Test(dataProvider = "parameters")
  public void findByParameters(Date start, Date end, String expectedQuery){
    when(entityManager.createQuery(eq(expectedQuery))).thenReturn(query);
    when(query.setParameter("start", start, TemporalType.DATE)).thenReturn(query);
    when(query.setParameter("end", end, TemporalType.DATE)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(costEntry));
    when(costEntry.getCategory()).thenReturn(category);
    when(costEntry.getBill()).thenReturn(bill);

    final List<CostEntryDTO> result =
        repository.findCostEntryByParameters(GROUP_ID, 3, start, end, 7, 1);
    
    final List<CostEntryDTO> temp = new ArrayList<CostEntryDTO>();
    for(CostEntry c : Collections.singletonList(costEntry)){
      temp.add(new CostEntryDTO(c));
    }

    assertEquals(result, temp);
  }

}
