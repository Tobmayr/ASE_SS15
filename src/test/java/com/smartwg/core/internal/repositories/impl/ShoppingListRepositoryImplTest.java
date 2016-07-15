package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.ShoppingListState;
import com.smartwg.core.internal.domain.dtos.ListItemDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.repositories.ShoppingListRepository;

@Test
public class ShoppingListRepositoryImplTest {

  @InjectMocks
  private ShoppingListRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private ShoppingListDTO shoppingListDTO;
  @Mock
  private ListItemDTO listItemDTO;

  @BeforeMethod
  public void setUp() {
    repository = new ShoppingListRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findUserShoppingLists() {
    when(entityManager.createNamedQuery("findShoppingListsByUserId")).thenReturn(query);
    when(query.setParameter("userId", 23)).thenReturn(query);
    when(query.setParameter("group", 2)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = repository.findUserShoppingLists(23,2);

    assertEquals(result, Collections.singletonList(shoppingListDTO));
  }

  public void findShoppingListsAssignedToUser() {
    when(entityManager.createNamedQuery("findShoppingListsAssignedToUser")).thenReturn(query);
    when(query.setParameter("userId", 23)).thenReturn(query);
    when(query.setParameter("group", 2)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = repository.findShoppingListsAssignedToUser(23,2);

    assertEquals(result, Collections.singletonList(shoppingListDTO));
  }

  public void findShoppingListById() {
    when(entityManager.createNamedQuery("findShoppingListsById")).thenReturn(query);
    when(query.setParameter("listId", 44)).thenReturn(query);
    when(query.getSingleResult()).thenReturn(shoppingListDTO);

    final ShoppingListDTO result = repository.findShoppingListById(44);

    assertEquals(result, shoppingListDTO);
  }

  public void findGroupShoppingLists() {
    final List<ShoppingListState> listStates =
        Arrays.asList(ShoppingListState.ASSIGNED, ShoppingListState.DONE);

    when(entityManager.createNamedQuery("findShoppingListsByGroupIdAndState")).thenReturn(query);
    when(query.setParameter("groupId", 2)).thenReturn(query);
    when(query.setParameter("listState", listStates)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(shoppingListDTO));

    final List<ShoppingListDTO> result = repository.findGroupShoppingLists(2, listStates);

    assertEquals(result, Collections.singletonList(shoppingListDTO));
  }
}
