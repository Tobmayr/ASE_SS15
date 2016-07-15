package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.ShopService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ShopFacadesImplTest {

  private static final Integer GROUP_ID = 2;
  private static final Integer COUNTRY_ID = 3;
  @InjectMocks
  private ShopFacade facades;

  @Mock
  private ShopService shopService;

  @Mock
  private ShopDTO shopDTO;

  @BeforeMethod
  public void setUp() {
    facades = new ShopFacadesImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findByGroup() {
    when(shopService.findByGroup(eq(GROUP_ID), eq(COUNTRY_ID))).thenReturn(singletonList(shopDTO));

    final List<ShopDTO> result = facades.findByGroup(GROUP_ID, COUNTRY_ID);

    assertEquals(result, singletonList(shopDTO));
  }

  public void removeShop() {
    facades.removeShop(shopDTO);

    verify(shopService, times(1)).removeShop(eq(shopDTO));
  }

  public void createShop() {
    when(shopDTO.getId()).thenReturn(null);

    facades.createShop(shopDTO);

    verify(shopService, times(1)).createShop(eq(shopDTO));
  }

  public void updateShop() {
    when(shopDTO.getId()).thenReturn(1);

    facades.createShop(shopDTO);

    verify(shopService, times(1)).updateShop(eq(shopDTO));
  }
}
