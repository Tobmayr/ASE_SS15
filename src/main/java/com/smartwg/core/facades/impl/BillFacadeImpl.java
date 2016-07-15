package com.smartwg.core.facades.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.BillService;
import com.smartwg.core.internal.services.PipelineService;

@Named
public class BillFacadeImpl implements BillFacade {
  @Inject
  private BillService billService;
  @Inject
  private PipelineService pipelineService;

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#saveBill(com.smartwg.core.internal.domain.dtos.BillDTO)
   */

  @Override
  public void saveBill(BillDTO billDTO) {

    if (billDTO.getId() == null) {
      billService.createBill(billDTO);
    } else {
      billService.editBill(billDTO);
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#calculateTotal(java.util.List)
   */

  @Override
  public BigDecimal calculateTotal(List<BillDTO> bills) {
    if (bills == null)
      return null;
    BigDecimal overviewTotal = new BigDecimal(0.00);
    for (BillDTO b : bills) {
      overviewTotal = overviewTotal.add(b.getTotal());
    }
    return overviewTotal;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#calcualteBillTotal(com.smartwg.core.internal.domain.dtos.BillDTO)
   */

  @Override
  public BigDecimal[] calcualteBillTotal(BillDTO bill) {
    if (bill == null)
      return null;

    BigDecimal collective = new BigDecimal(0);
    BigDecimal excluded = new BigDecimal(0);
    BigDecimal[] temp = new BigDecimal[2];
    if (!bill.getCostEntries().isEmpty()) {
      for (CostEntryDTO dto : bill.getCostEntries()) {
        if (dto.isExcluded() || bill.isPrivateBill())
          excluded = excluded.add(dto.getAmount());
        else
          collective = collective.add(dto.getAmount());
      }
    } else {
      if (bill.isPrivateBill())
        excluded = excluded.add(bill.getTotal());
      else
        collective = collective.add(bill.getTotal());
    }

    temp[0] = collective;
    temp[1] = excluded;
    bill.setTotal(temp[0]);
    return temp;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#editBill(com.smartwg.core.internal.domain.dtos.BillDTO)
   */

  public void editBill(BillDTO billDTO) {
    billService.editBill(billDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#deleteBill(com.smartwg.core.internal.domain.dtos.BillDTO)
   */

  @Override
  public void deleteBill(BillDTO billDTO) {
    billService.deleteBill(billDTO);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#getBillsBetweenTimespan(java.util.Date,
   *      java.util.Date, java.lang.Integer)
   */

  @Override
  public List<BillDTO> getBillsBetweenTimespan(Date start, Date end, Integer group_id) {
    return billService.findBillsBetweenTimespan(start, end, group_id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#getBillsWithCostEntriesBetweenTimespan(java.util.Date,
   *      java.util.Date, java.lang.Integer)
   */

  @Override
  public List<BillDTO> getBillsWithCostEntriesBetweenTimespan(Date start, Date end, Integer group_id) {
    return billService.findBillsWithCostEntriesBetweenTimespan(start, end, group_id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#getPrivateBillsBetweenTimespan(java.util.Date,
   *      java.util.Date, java.lang.Integer, java.lang.Integer)
   */

  @Override
  public List<BillDTO> getPrivateBillsBetweenTimespan(Date start, Date end, Integer group_id,
      Integer user_id) {
    return billService.findPrivateBillsBetweenTimespan(start, end, group_id, user_id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#findById(java.lang.Integer)
   */

  @Override
  public BillDTO findById(Integer id) {
    return billService.findById(id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#getCostEntries(java.lang.Integer)
   */

  @Override
  public List<CostEntryDTO> getCostEntries(Integer bill_id) {
    return billService.getCostEntries(bill_id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#findByParameters(java.lang.Integer, java.lang.Integer,
   *      java.util.Date, java.util.Date, java.lang.Integer)
   */

  @Override
  public List<BillDTO> findByParameters(Integer group_id, Integer createdBy_id, Date start,
      Date end, Integer shop_id) {
    return billService.findByParameters(group_id, createdBy_id, start, end, shop_id);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#findCostEntryByParameters(java.lang.Integer,
   *      java.lang.Integer, java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)
   */

  @Override
  public List<CostEntryDTO> findCostEntryByParameters(Integer group_id, Integer createdBy_id,
      Date start, Date end, Integer category_id, Integer shop_id) {
    return billService.findCostEntryByParameters(group_id, createdBy_id, start, end, category_id,
        shop_id);
  }

  /**
   * {@inheritdoc}
   */
  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#getPrivateCostEntries(java.lang.Integer)
   */

  @Override
  public List<CostEntryDTO> getPrivateCostEntries(Integer bill_id) {
    return billService.getPrivateCostEntries(bill_id);
  }

  /**
   * {@inheritdoc}
   */
  /**
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.facades.BillFacade#doImageRecognition(com.smartwg.core.internal.domain.dtos.ResourceDTO)
   */

  @Override
  public BillDTO doImageRecognition(ResourceDTO resourceDTO, List<ShopDTO> shops,
      List<CurrencyDTO> currencies) {
    if (resourceDTO.getType().startsWith("image/")) {
      BillDTO result = pipelineService.doImageToBillPipeline(resourceDTO, shops, currencies);
      result.setResource(resourceDTO);
      return result;
    }
    return null;
  }


}
