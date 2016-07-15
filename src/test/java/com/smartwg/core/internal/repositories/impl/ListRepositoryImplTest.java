package com.smartwg.core.internal.repositories.impl;

import static com.smartwg.core.util.Constants.QUERY_FIND_SHOPPINGLIST_POSITION_ID;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.repositories.ListPositionRepository;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ListRepositoryImplTest {

  @InjectMocks
  private ListPositionRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;

  @Mock
  private ListItemDTO listItemDTO;

  @BeforeMethod
  public void setUp() {
    repository = new ListRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findShoppingListPositions() {
    when(entityManager.createNamedQuery(QUERY_FIND_SHOPPINGLIST_POSITION_ID)).thenReturn(query);
    when(query.setParameter("shoppingListId", 2)).thenReturn(query);
    when(query.getResultList()).thenReturn(singletonList(listItemDTO));

    final List<ListItemDTO> result = repository.findShoppingListPositions(2);

    assertEquals(result, singletonList(listItemDTO));
  }
}
