package com.smartwg.core.internal.repositories;

import java.util.Date;
import java.util.List;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.entities.Bill;

/**
 * This repository provides methods for CRUD-Operations for Bill-objects as well as advanced
 * queryable operations. The methods proved by the GenericRepostiory-Implementation return
 * Bill-objects the methods provided by this interface directly return BillDTOs to avoid later
 * typecasting or converting.
 *
 *
 * @author Tobias Ortmayr (to) and Oezde Simsek (os)
 */

public interface BillRepository extends GenericRepository<Bill> {
  /**
   * Retreives a List of all bills which were created between a certain timespan and are associated
   * with one group
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id associated group
   * @return a List with matching bills or an empty List in case no matching bills where found
   */
  public List<BillDTO> getBillsBetweenTimespan(Date start, Date end, Integer group_id);

  /**
   * Retrieves a List of all bills which were created between a certain timespan and are associated
   * with one group and additionally fetches the list of bill-associated CostEntries for each bill.
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id associated group
   * @return a List with matching bills or an empty List in case no matching bills where found
   */
  public List<BillDTO> getBillsWithCostEntriesBetweenTimespan(Date start, Date end, Integer group_id);

  /**
   * Retrieves a List of all CostEntries which were marked as privte (wrapped in a BillDTO) and all
   * private Bills which were created between a certain timepspan, by a certain user and are
   * associated with a certain group
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id associated group
   * @param user_id creator of the bills
   * @return a List with matching bills or an empty List in case no matching bills where found
   */
  public List<BillDTO> getPrivateBillsBetweenTimespan(Date start, Date end, Integer group_id,
      Integer user_id);

  /**
   * Retrieves the bill with a matchin id form the database. This method is bascially a wrapper
   * method for the findById()-Method from GenericRepository.
   * 
   * @param id id of the bill which should be retrieved
   * @return Bill or null in case no matching bill was found
   */
  public BillDTO findBillById(Integer id);

  /**
   * Retrieves a List of all Bills with the given parameters; the id of the group, the id of the
   * user who created the bill, timespan with start and end date, the id of the shop.
   * 
   * @param group_id id of the group
   * @param createdBy_id id of the user who created the bill
   * @param start start point
   * @param end end point
   * @param shop_id the id of the shop
   * @return a list with matching bills or an empty in case of no matching bills were found
   */
  public List<BillDTO> findByParameters(Integer group_id, Integer createdBy_id, Date start,
      Date end, Integer shop_id);


}
