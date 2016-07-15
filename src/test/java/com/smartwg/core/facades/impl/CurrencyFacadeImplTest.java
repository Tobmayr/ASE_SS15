package com.smartwg.core.facades.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.CurrencyFacade;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.services.CurrencyService;

@Test
public class CurrencyFacadeImplTest {

  @InjectMocks
  private CurrencyFacade facade;
  @Mock
  private CurrencyService currencyService;

  @BeforeMethod
  public void beforeMethod() {
    facade = new CurrencyFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findAll_succeed() {
    final List<CurrencyDTO> expected =
        Arrays.asList(new CurrencyDTO(2, "EUR"), new CurrencyDTO(3, "USD"));
    when(currencyService.findAll()).thenReturn(expected);
    final List<CurrencyDTO> result = facade.findAll();
    verify(currencyService, times(1)).findAll();
    assertEquals(result, expected);
  }

}
