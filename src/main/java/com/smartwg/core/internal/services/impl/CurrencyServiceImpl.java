package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.entities.Currency;
import com.smartwg.core.internal.repositories.CurrencyRepository;
import com.smartwg.core.internal.services.CurrencyService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Tobias Ortmayr (to)
 */
@Named
public class CurrencyServiceImpl implements CurrencyService {

  @Inject
  private CurrencyRepository repository;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CurrencyDTO> findAll() {
    final List<Currency> temp = repository.findAll();
    final List<CurrencyDTO> result = new ArrayList<>(temp.size());
    for (Currency currency : temp) {
      result.add(new CurrencyDTO(currency.getId(), currency.getIsoCode()));
    }
    return result;
  }

}
