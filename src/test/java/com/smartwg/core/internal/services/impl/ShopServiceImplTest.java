package com.smartwg.core.internal.services.impl;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.entities.Shop;
import com.smartwg.core.internal.repositories.ShopRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.ShopService;

/**
 * @author Kamil Sierzant(ks)
 */
@Test
public class ShopServiceImplTest {

  private static final Integer GROUP_ID = 2;
  private static final Integer COUNTRY_ID = 4;

  @InjectMocks
  private ShopService service;

  @Mock
  private EntityConverter factory;
  @Mock
  private ShopRepository shopRepository;

  @Mock
  private ShopDTO shopDTO;
  @Mock
  private ShopDTO secondShopDTO;
  @Mock
  private Shop shop;

  @BeforeMethod
  public void setUp() {
    service = new ShopServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  public void findByGroup() {
    when(shopRepository.findByGroup(eq(GROUP_ID), eq(COUNTRY_ID))).thenReturn(
        asList(shopDTO, secondShopDTO));

    final List<ShopDTO> result = service.findByGroup(GROUP_ID, COUNTRY_ID);

    assertEquals(result, asList(shopDTO, secondShopDTO));
  }

  public void removeShop() {
    when(factory.createShop(eq(shopDTO))).thenReturn(shop);

    service.removeShop(shopDTO);

    verify(shop, times(1)).setDeleted(eq(Boolean.TRUE));
    verify(shopRepository, times(1)).merge(eq(shop));
  }

  public void createShop() {
    when(factory.createShop(eq(shopDTO))).thenReturn(shop);

    service.createShop(shopDTO);

    verify(shopRepository, times(1)).save(eq(shop));
  }

  public void updateShop() {
    when(factory.createShop(eq(shopDTO))).thenReturn(shop);

    service.updateShop(shopDTO);

    verify(shopRepository, times(1)).merge(eq(shop));
  }
}
