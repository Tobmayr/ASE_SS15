package com.smartwg.core.facades.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.facades.CurrencyFacade;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.services.CurrencyService;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class CurrencyFacadeImpl implements CurrencyFacade {

  @Inject
  private CurrencyService currencyService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.CurrencyFacade#findAll()
   */

  @Override
  public List<CurrencyDTO> findAll() {
    return currencyService.findAll();
  }


}
