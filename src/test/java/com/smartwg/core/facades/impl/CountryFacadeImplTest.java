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

import com.smartwg.core.facades.CountryFacade;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.services.CountryService;

@Test
public class CountryFacadeImplTest {

  @InjectMocks
  private CountryFacade facade;
  @Mock
  private CountryService countryService;
  @Mock
  private CountryDTO c1;
  @Mock
  private CountryDTO c2;

  @BeforeMethod
  public void beforeMethod() {
    facade = new CountryFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getAllCountries() {
    final List<CountryDTO> expected = Arrays.asList(c1, c2);
    when(countryService.getAllCountries()).thenReturn(expected);
    final List<CountryDTO> result = facade.getAllCountries();
    verify(countryService, times(1)).getAllCountries();
    assertEquals(result, expected);
  }
}
