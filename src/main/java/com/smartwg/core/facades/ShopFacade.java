package com.smartwg.core.facades;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.ShopDTO;

@Transactional
/**
 * Central access point for service logic which is associated with shops.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author Tobias Ortmayr (to)
 *
 */
public interface ShopFacade {


  /**
   * Returns all Shops which are associated with a specific group. If the group is associated with a
   * country entity the default Shops for this Country are also contained in the result.
   * 
   * @param group Id of the group
   * @return List with Shops which matched the passed argument or an empty List
   */

  List<ShopDTO> findByGroup(Integer group, Integer country);

  /**
   * Removes a certain shop from the database
   * 
   * @param shopDTO Shop which shoul be removed
   */
  void removeShop(ShopDTO shopDTO);

  /**
   * Persists a new shop or into the database
   *
   * @param shop Shop object which should be stored
   * 
   */
  void createShop(ShopDTO shop);


}
