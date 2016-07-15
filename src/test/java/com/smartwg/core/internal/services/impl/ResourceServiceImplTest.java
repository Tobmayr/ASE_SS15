package com.smartwg.core.internal.services.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.entities.Resource;
import com.smartwg.core.internal.repositories.ResourceRepository;
import com.smartwg.core.internal.services.ResourceService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ResourceServiceImplTest {

  @InjectMocks
  private ResourceService service;

  @Mock
  private ResourceRepository repository;

  @Mock
  private Resource resource;

  @BeforeMethod
  public void setUp() {
    service = new ResourceServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findById() {
    when(repository.findById(eq(2))).thenReturn(resource);
    when(resource.getId()).thenReturn(2);
    when(resource.getName()).thenReturn("Bill");
    when(resource.getType()).thenReturn("PDF");

    final ResourceDTO result = service.findById(2);

    assertEquals(result.getId(), resource.getId());
    assertEquals(result.getName(), resource.getName());
    assertEquals(result.getType(), resource.getType());
  }
}
