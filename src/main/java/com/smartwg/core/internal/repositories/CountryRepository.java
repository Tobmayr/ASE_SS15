package com.smartwg.core.internal.repositories;

import com.smartwg.core.internal.domain.dtos.CountryDTO;
import com.smartwg.core.internal.domain.entities.Country;

import java.util.List;

/**
 * This repository provides methods for CRUD-Operations for Country-objects
 * 
 * @author Anna Sadriu (as)
 */
public interface CountryRepository extends GenericRepository<Country> {

  /**
   * Retrieves all countries which are stored in the database
   * 
   * @return List of countries as CountryDTO or an empty list
   */
  List<CountryDTO> getAllCountries();
}
