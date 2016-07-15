package com.smartwg.core.internal.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.entities.Currency;
import com.smartwg.core.internal.repositories.CurrencyRepository;
import com.smartwg.core.internal.services.CurrencyService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class CurrencyServiceImplTest {

  @InjectMocks
  private CurrencyService service;

  @Mock
  private CurrencyRepository repository;

  @Mock
  private Currency euro;
  @Mock
  private Currency dolar;

  @BeforeMethod
  public void setUp() {
    service = new CurrencyServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findAll() {
    when(repository.findAll()).thenReturn(Arrays.asList(euro, dolar));
    when(euro.getId()).thenReturn(1);
    when(euro.getIsoCode()).thenReturn("EUR");
    when(dolar.getId()).thenReturn(2);
    when(dolar.getIsoCode()).thenReturn("USD");

    final List<CurrencyDTO> result = service.findAll();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(result.size(), 2);
    assertEquals(result.get(0).getId(), Integer.valueOf(1));
    assertEquals(result.get(0).getIsoCode(), "EUR");
    assertEquals(result.get(1).getId(), Integer.valueOf(2));
    assertEquals(result.get(1).getIsoCode(), "USD");
  }
}
