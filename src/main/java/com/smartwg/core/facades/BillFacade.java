package com.smartwg.core.facades;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;

@Transactional
/**
 * Central access point for service logic which is associated with bills.
 * e.g. persisting and updating bills in the database, calculation of bill totals etc.
 * In case of implementing a REST-API in the future its methods will be bounded on the facade-interfaces
 * @author Tobias Ortmayr (to)
 *
 */
public interface BillFacade {

  /**
   * Persists a new bill or updates an existing bill in the database
   * 
   * @param billDTO bill object wich should be stored
   * @throws NullPointerException if the passed parameter is null
   */
  void saveBill(BillDTO billDTO);

  /**
   * Returns all bills (as BIllDTO) which were created between a certain start and end date and are
   * associated with a certain group
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id Id of the associated group
   * @return List of BillDTOs which are matching the passed parameters
   */
  List<BillDTO> getBillsBetweenTimespan(Date start, Date end, Integer group_id);

  /**
   * Returns all bills (as BIllDTO) which were created between a certain start and end date and are
   * associated with a certain group. Additionally associated CostEntryDTOs are loaded as well
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id Id of the associated group
   * @return List of BillDTOs with associated Costentries which are matching the passed parameters
   */
  List<BillDTO> getBillsWithCostEntriesBetweenTimespan(Date start, Date end, Integer group_id);

  /**
   * Returns all private bills or excluded CostEntries of billss (as BIllDTO) which were created
   * between a certain start and end date by a certain user and are associated with a certain group
   * 
   * @param start Timespan start point
   * @param end Timespan end point
   * @param group_id Id of the associated group
   * @param user_id Id of the user who created the bills
   * @return List of BillDTOs which are matching the passed parameters
   */
  List<BillDTO> getPrivateBillsBetweenTimespan(Date start, Date end, Integer group_id,
      Integer user_id);

  /**
   * This helper-method calculates the overall total of a List of bills. The passed argument is null
   * this method returns null as well. If the passed argument is an empty list 0 will be returned.
   * 
   * @param bills Bills which total cost should be calculated
   * @return total cost
   */
  BigDecimal calculateTotal(List<BillDTO> bills);

  /**
   * This helper method calculates the total amount of shared and private costs for one single bill.
   * 
   * @param bill Bill whichs costs should be calculated
   * @return Array of the length 2. First Entry contains the collective amount as BigInteger, the
   *         second one the private
   */
  BigDecimal[] calcualteBillTotal(BillDTO bill);

  /**
   * Updates the values of an existing bill in the datbase. If the value is null the database stays
   * unchanged
   * 
   * @param billDTO BillDTO with existing id but changed attributes
   */
  void editBill(BillDTO billDTO);

  /**
   * Deletes removes a bill with a certain id of the database. If the value is null the database
   * will remain unchanged
   * 
   * @param billDTO bill which should be deleted
   */
  void deleteBill(BillDTO billDTO);

  /**
   * Searches for a certain bill in the database using its id.
   * 
   * @param id Id of the wanted bill
   * @return found Bill as BillDTO, null if the passed id doesnt exist
   */
  BillDTO findById(Integer id);

  /**
   * Returns a List of all CostEntries associated with a certain bill(id)
   * 
   * @param bill_id Id id the bill whose costEntries are wanted
   * @return List of all matching costentries
   */
  List<CostEntryDTO> getCostEntries(Integer bill_id);


  /**
   * Returns a List of all Bills (as BillDTO) with the given parameters; the id of the group, the id
   * of the user who created the bill, timespan with start and end date, the id of the shop.
   * 
   * @param group_id id of the group
   * @param createdBy_id id of the user who created the bill
   * @param start start point
   * @param end end point
   * @param shop_id the id of the shop
   * @return a list with matching bills or an empty in case of no matching bills were found
   */
  List<BillDTO> findByParameters(Integer group_id, Integer createdBy_id, Date start, Date end,
      Integer shop_id);

  /**
   * Returns a List of all Cost Entries with the given parameters; the id of the group, the id of
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
  List<CostEntryDTO> findCostEntryByParameters(Integer group_id, Integer createdBy_id, Date start,
      Date end, Integer category_id, Integer shop_id);

  /**
   * Returns a List of all CostEntries associated with a certain bill(id) and witch are markes as
   * private(excluded)
   * 
   * @param bill_id Id id the bill whose costEntries are wanted
   * @return List of all matching costentries
   */
  List<CostEntryDTO> getPrivateCostEntries(Integer bill_id);

  /**
   * This method uses the automatic recogniton of bills (basica data and costentries). Therefore tis
   * necessary that the passed resourceDTO contains an image resource. In addtion a List of shops
   * and currencies has to be passed, if these entities should be considered by the recognition
   * 
   * @param resourceDTO resource which should be used for the recogntion
   * @return BilDTO with generated results
   */
  public BillDTO doImageRecognition(ResourceDTO resourceDTO, List<ShopDTO> shops,
      List<CurrencyDTO> currencies);
}
