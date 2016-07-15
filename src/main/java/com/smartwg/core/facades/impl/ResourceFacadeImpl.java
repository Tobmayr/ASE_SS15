package com.smartwg.core.facades.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.facades.ResourceFacade;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.services.ResourceService;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class ResourceFacadeImpl implements ResourceFacade {

  @Inject
  private ResourceService service;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ResourceFacade#findById(java.lang.Integer)
   */

  @Override
  public ResourceDTO findById(Integer id) {
    return service.findById(id);
  }

}
