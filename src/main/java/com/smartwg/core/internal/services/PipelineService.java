package com.smartwg.core.internal.services;

import java.util.List;

import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CurrencyDTO;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public interface PipelineService {
  /**
   * This method prepares for and delegates an uploaded resource to the underlying Pipeline. In
   * addtion to the resource a List of Shops and Currencies which should be considered by the
   * Recogniton Pipeline have to be passed
   * 
   * @param resourceDTO
   * @param shops
   * @param currencies
   * @return a BillDTO representation with the extracted values or empty values
   */
  BillDTO doImageToBillPipeline(ResourceDTO resourceDTO, List<ShopDTO> shops,
      List<CurrencyDTO> currencies);
}
