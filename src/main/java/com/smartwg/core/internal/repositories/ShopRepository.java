package com.smartwg.core.internal.repositories;

import java.util.List;

import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.entities.Shop;

/**
 * This repository provides methods for CRUD-Operations for Shop-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * Shop-objects the methods provided by this interface directly return BillDTOs to avoid later
 * typecasting or converting.
 *
 * @author Tobias Ortmayr (to)
 */
public interface ShopRepository extends GenericRepository<Shop> {

  /**
   * Retrieves a List of Shops which are associated with a specific group. If the group is
   * associated with a country entity the default Shops for this Country are also contained in the
   * result
   * 
   * @param group Id of the group
   * @return List with Shops which matched the passed argument or an empty List
   */
  List<ShopDTO> findByGroup(Integer group, Integer country);

}
