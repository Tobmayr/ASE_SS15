package com.smartwg.core.facades.impl;

import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.ShopService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Tobias Ortmayr (to)
 */
@Named
public class ShopFacadesImpl implements ShopFacade {

  @Inject
  private ShopService shopService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShopFacade#findByGroup(java.lang.Integer, java.lang.Integer)
   */

  @Override
  public List<ShopDTO> findByGroup(final Integer group, final Integer country) {
    return shopService.findByGroup(group, country);

  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShopFacade#removeShop(com.smartwg.core.internal.domain.dtos.ShopDTO)
   */

  @Override
  public void removeShop(ShopDTO shopDTO) {
    shopService.removeShop(shopDTO);

  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.ShopFacade#createShop(com.smartwg.core.internal.domain.dtos.ShopDTO)
   */

  @Override
  public void createShop(ShopDTO shop) {
    if (shop.getId() == null) {
      shopService.createShop(shop);
    } else {
      shopService.updateShop(shop);
    }

  }


}
