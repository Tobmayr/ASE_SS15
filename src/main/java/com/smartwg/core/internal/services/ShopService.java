package com.smartwg.core.internal.services;

import com.smartwg.core.internal.domain.dtos.ShopDTO;

import java.util.List;

/**
 * This Interface provides the basic service methods which are associated with shops. The main
 * purpose of these methods is to wrap the functionality of the underlying ShopRepository and
 * convert the entities to dtos for user in higher-tiered layers
 *
 * @author Tobias Ortmayr (to)
 */
public interface ShopService {

  /**
   * Returns all Shops which are associated with a specific group. If the group is associated with a
   * country entity the default Shops for this Country are also contained in the result
   *
   * @param country id of the country
   * @param group Id of the group
   * @return List with Shops which matched the passed argument or an empty List
   */
  List<ShopDTO> findByGroup(Integer group, Integer country);

  /**
   * Removes the shop with given id. The shop is not deleted permanently but marked as deleted
   *
   * @param shopDTO {@link ShopDTO}
   */
  void removeShop(ShopDTO shopDTO);

  /**
   * Creates a new shop based on provided data in dto
   *
   * @param shop {@link ShopDTO}
   */
  void createShop(ShopDTO shop);

  /**
   * Updates the given shop according to provided data
   *
   * @param shop {@link ShopDTO}
   */
  void updateShop(ShopDTO shop);

}
