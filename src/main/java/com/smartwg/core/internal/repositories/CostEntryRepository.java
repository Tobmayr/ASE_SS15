package com.smartwg.core.internal.repositories;

import java.util.Date;
import java.util.List;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.entities.CostEntry;

/**
 * @author Tobias Ortmayr , Oezde Simsek (os)
 */
public interface CostEntryRepository extends GenericRepository<CostEntry> {

  /**
   * Retrieves a list of Cost Entries which belongs to the bill with the given id
   * 
   * @param bill_id id of the bill
   * @return a list of Cost Entries
   */
  public List<CostEntryDTO> getCostEntries(Integer bill_id);

  /**
   * Retrieves a List of all Cost Entries with the given parameters; the id of the group, the id of
   * the user who created the cost entry, timespan with start and end date, the id of the shop, the
   * id of the category
   * 
   * @param group_id id of the group
   * @param createdBy_id id of the user who created the cost entry
   * @param start start point
   * @param end end point
   * @param category_id the id of the category
   * @param shop_id the id of the shop
   * @return a list with matching cost entries or an empty in case of no matching cost entries were
   *         found
   */
  public List<CostEntryDTO> findCostEntryByParameters(Integer group_id, Integer createdBy_id,
      Date start, Date end, Integer category_id, Integer shop_id);

  /**
   * Retrieves a list of Cost Entries which belongs to the bill with the given id and are marked as
   * private (excluded)
   * 
   * @param bill_id id of the bill
   * @return a list of Cost Entries
   */
  public List<CostEntryDTO> getPrivateCostEntries(Integer bill_id);
}
