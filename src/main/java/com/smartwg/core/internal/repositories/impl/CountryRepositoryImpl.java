package com.smartwg.core.internal.repositories.impl;

import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.entities.Country;
import com.smartwg.core.internal.repositories.CountryRepository;
import com.smartwg.core.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Anna Sadriu (as)
 */
@Named
public class CountryRepositoryImpl extends GenericRepositoryImpl<Country> implements
                                                                          CountryRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(CountryRepositoryImpl.class);

  @SuppressWarnings("unchecked")
  @Override
  public List<CountryDTO> getAllCountries() {
    final Query query = em.createNamedQuery(Constants.QUERY_FIND_COUNTRIES);
    LOGGER.info("find all countries.");
    return query.getResultList();
  }
}
