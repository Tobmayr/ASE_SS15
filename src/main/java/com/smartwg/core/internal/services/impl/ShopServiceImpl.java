package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.entities.Shop;
import com.smartwg.core.internal.repositories.ShopRepository;
import com.smartwg.core.internal.services.EntityConverter;
import com.smartwg.core.internal.services.ShopService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Tobias Ortmayr (to)
 */
@Named
public class ShopServiceImpl implements ShopService {

  @Inject
  private EntityConverter factory;
  @Inject
  private ShopRepository shopRepository;

  @Override
  public List<ShopDTO> findByGroup(final Integer group, final Integer country) {
    return shopRepository.findByGroup(group, country);
  }

  @Override
  public void removeShop(final ShopDTO shopDTO) {
    final Shop shop = factory.createShop(shopDTO);
    shop.setDeleted(true);
    shopRepository.merge(shop);
  }

  @Override
  public void createShop(final ShopDTO shopDTO) {
    final Shop shop = factory.createShop(shopDTO);
    shopRepository.save(shop);
  }

  @Override
  public void updateShop(final ShopDTO shopDTO) {
    final Shop shop = factory.createShop(shopDTO);
    shopRepository.merge(shop);
  }
}
