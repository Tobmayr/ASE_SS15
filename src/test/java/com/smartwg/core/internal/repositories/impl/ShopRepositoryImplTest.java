package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.repositories.ShopRepository;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ShopRepositoryImplTest {

  @InjectMocks
  private ShopRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private ShopDTO shopDTO;

  @BeforeMethod
  public void setUp() {
    repository = new ShopRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findByGroupSucceed() {
    when(
        entityManager
            .createQuery("SELECT  new com.smartwg.core.internal.domain.dtos.ShopDTO(s) FROM Shop s WHERE s.group.id=2 OR s.country.id=4"))
        .thenReturn(query);
    when(query.setParameter("userId", 23)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(shopDTO));

    final List<ShopDTO> result = repository.findByGroup(2, 4);

    assertEquals(result, Collections.singletonList(shopDTO));
  }

  public void findByGroupWhenGroupNull() {

    final List<ShopDTO> result = repository.findByGroup(null, 4);

    assertTrue(result.isEmpty());
  }
}
