package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.CountryFacade;
import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.services.CountryService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xsk
 */
@Named
public class CountryFacadeImpl implements CountryFacade {

  @Inject
  private CountryService countryService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CountryFacade#getAllCountries()
   */

  @Override
  public List<CountryDTO> getAllCountries() {
    return countryService.getAllCountries();
  }
}
