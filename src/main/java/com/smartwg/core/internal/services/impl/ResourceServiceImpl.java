package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.repositories.ResourceRepository;
import com.smartwg.core.internal.services.ResourceService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Tobias Ortmayr (to)
 */
@Named
public class ResourceServiceImpl implements ResourceService {

  @Inject
  private ResourceRepository repository;

  @Override
  public ResourceDTO findById(final Integer id) {
    return new ResourceDTO(repository.findById(id));
  }
}
