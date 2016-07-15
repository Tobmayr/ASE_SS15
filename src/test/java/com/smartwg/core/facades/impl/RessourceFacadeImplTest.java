package com.smartwg.core.facades.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.facades.ResourceFacade;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.services.ResourceService;

@Test
public class RessourceFacadeImplTest {

  @InjectMocks
  private ResourceFacade facade;
  @Mock
  private ResourceService resourceService;
  @Mock
  private ResourceDTO resourceDTO;

  @BeforeMethod
  public void beforeMethod() {
    facade = new ResourceFacadeImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findById() {
    when(resourceService.findById(24)).thenReturn(resourceDTO);
    final ResourceDTO result = facade.findById(24);
    verify(resourceService, times(1)).findById(24);
    assertEquals(result, resourceDTO);
  }
}
