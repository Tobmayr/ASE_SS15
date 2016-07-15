package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.repositories.CountryRepository;
import com.smartwg.core.internal.services.CountryService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kamil Sierzant(ks)
 */
@Named
public class CountryServiceImpl implements CountryService {

  @Inject
  private CountryRepository countryRepository;

  @Override
  public List<CountryDTO> getAllCountries() {
    return countryRepository.getAllCountries();
  }
}
