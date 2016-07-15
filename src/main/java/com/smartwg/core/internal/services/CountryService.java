package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.CountryDTO;

import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with countries.The main
 * purpose of these methods is to wrap the functionality of the underlying CountryRepostitory and
 * convert the entities to dtos for user in higher-tiered layers
 * 
 * @author xsk
 */
public interface CountryService {

  /**
   * Retrieves all Countries which are stored in the database.
   * 
   * @return List of all Countries represented as CountryDTO
   */
  List<CountryDTO> getAllCountries();
}
