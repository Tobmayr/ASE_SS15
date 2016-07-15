package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.ResourceDTO;

/**
 * @author Tobias Ortmayr (to)
 */
public interface ResourceService {

  /**
   * Returns resource for given id
   *
   * @param id {@link Integer} of resource
   * @return {@link ResourceDTO}
   */
  ResourceDTO findById(Integer id);
}
