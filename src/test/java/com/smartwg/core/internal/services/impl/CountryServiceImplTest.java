package com.smartwg.core.internal.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.repositories.CountryRepository;
import com.smartwg.core.internal.services.CountryService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class CountryServiceImplTest {

  @InjectMocks
  private CountryService service;

  @Mock
  private CountryRepository countryRepository;

  @Mock
  private CountryDTO country;
  @Mock
  private CountryDTO secondCountry;

  @BeforeMethod
  public void setUp() {
    service = new CountryServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void getAllCountries() {
    when(countryRepository.getAllCountries()).thenReturn(Arrays.asList(country, secondCountry));

    final List<CountryDTO> result = service.getAllCountries();

    assertEquals(result, Arrays.asList(country, secondCountry));
  }
}
