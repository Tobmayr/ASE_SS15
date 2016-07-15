package com.smartwg.core.facades;

import com.smartwg.core.internal.domain.dtos.CountryDTO;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Central access point for service logic which is associated with countries. In case of
 * implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * 
 * Anna Sadriu (as)
 *
 */
@Transactional
public interface CountryFacade {
  /**
   * Retrieves all Countries which are stored in the database.
   * 
   * @return List of all Countries represented as CountryDTO
   */
  List<CountryDTO> getAllCountries();
}
