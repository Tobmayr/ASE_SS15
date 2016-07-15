package com.smartwg.core.internal.services.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.services.PipelineService;
import com.smartwg.core.internal.services.pipeline.impl.ImageBillDTO;
import com.smartwg.core.internal.services.pipeline.impl.ImageBillPipeline;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
@Named
public class PipelineServiceImpl implements PipelineService {
  @Inject
  private ImageBillPipeline pipeline;

  /**
   * 
   * {@inheritDoc}
   * 
   * @see com.smartwg.core.internal.services.PipelineService#doImageToBillPipeline(com.smartwg.core.internal.domain.dtos.ResourceDTO,
   *      java.util.List, java.util.List)
   */
  @Override
  public BillDTO doImageToBillPipeline(ResourceDTO resourceDTO, List<ShopDTO> shops,
      List<CurrencyDTO> currencies) {
    if (resourceDTO == null)
      return new BillDTO();
    ImageBillDTO dto = new ImageBillDTO(resourceDTO.getContent());
    dto.setCurrencyMap(currencies);
    dto.setShopMap(shops);
    pipeline.doPipeline(dto);
    BillDTO bill = new BillDTO();
    bill.setDate(dto.getDate());
    bill.setCurrency(dto.getCurrency());
    bill.setShopDTO(dto.getShop());
    bill.setCostEntries(dto.getCostEntries());
    return bill;

  }

}
