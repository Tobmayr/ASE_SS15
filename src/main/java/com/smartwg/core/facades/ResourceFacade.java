package com.smartwg.core.facades;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.ResourceDTO;

/**
 * Central access point for service logic which is associated with resource. In case of implementing
 * a REST-API in the future its methods will be bounded on the facade-interfaces
 * 
 * Tobias Ortmayr (to)
 *
 */
@Transactional
public interface ResourceFacade {
  /**
   * Searches for a certain resource in the database using its id.
   * 
   * @param id Id of the wanted resource
   * @return found Resource as ResourceDTO, null if the passed id doesn't exist
   */
  ResourceDTO findById(Integer id);
}
