package com.smartwg.core.internal.services.pipeline.impl;

import javax.inject.Named;

import com.smartwg.core.internal.services.pipeline.Pipeline;

/**
 * 
 * @author Tobias Ortmayr(to)
 *
 */
@Named
public class ImageBillPipeline implements Pipeline<ImageBillDTO> {

  private ImageOCRStage stage1;
  private ExtractBasicsStage stage2;
  private ExtractCostentryStage stage3;

  public ImageBillPipeline() {
    this.stage1 = new ImageOCRStage();
    this.stage2 = new ExtractBasicsStage();
    this.stage3 = new ExtractCostentryStage();
  }

  @Override
  public void doPipeline(ImageBillDTO dto) {
    stage1.process(dto);
    stage2.process(dto);
    stage3.process(dto);


  }

}
