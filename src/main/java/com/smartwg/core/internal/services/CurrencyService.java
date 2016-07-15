package com.smartwg.core.internal.services;

import java.util.List;

import com.smartwg.core.internal.domain.dtos.CurrencyDTO;

/**
 * This Interface provides the basic service methods which are associated with currencies.The main
 * purpose of these methods is to wrap the functionality of the underlying CurrencyRepostitory and
 * convert the entities to dtos for user in higher-tiered layers
 * 
 * @author Tobias Ortmayr (to)
 */
public interface CurrencyService {
  /**
   * Retrieves all Currencies which are stored in the database wrapped in CurrencyDTO objects.
   * 
   * @return Returns a List with all currencies stored in the database
   */
  List<CurrencyDTO> findAll();
}
