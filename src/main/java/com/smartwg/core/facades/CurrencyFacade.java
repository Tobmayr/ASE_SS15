package com.smartwg.core.facades;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.CurrencyDTO;

/**
 * Central access point for service logic which is associated with currencies. In case of
 * implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Transactional
public interface CurrencyFacade {

  /**
   * Retrieves all Currencies which are stored in the database wrapped in CurrencyDTO objects.
   * 
   * @return Returns a List with all currencies stored in the database
   */
  List<CurrencyDTO> findAll();
}
