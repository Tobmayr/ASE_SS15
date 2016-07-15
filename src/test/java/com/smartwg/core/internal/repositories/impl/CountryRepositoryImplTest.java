package com.smartwg.core.internal.repositories.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.dtos.ShoppingListDTO;
import com.smartwg.core.internal.repositories.CountryRepository;
import com.smartwg.core.util.Constants;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class CountryRepositoryImplTest {

  @InjectMocks
  private CountryRepository repository;

  @Mock
  private EntityManager entityManager;
  @Mock
  private Query query;
  @Mock
  private ShoppingListDTO shoppingListDTO;
  @Mock
  private CountryDTO countryDTO;

  @BeforeMethod
  public void setUp() {
    repository = new CountryRepositoryImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getAllCountries() {
    when(entityManager.createNamedQuery(Constants.QUERY_FIND_COUNTRIES)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(countryDTO));

    final List<CountryDTO> result = repository.getAllCountries();

    assertEquals(result, Collections.singletonList(countryDTO));
  }
}
