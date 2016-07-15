package com.smartwg.core.internal.services.pipeline.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.services.pipeline.Pipeline;

@Test
public class ImageBillPipelineTest {
  @InjectMocks
  private Pipeline<ImageBillDTO> pipeline;
  @Mock
  private ImageOCRStage imageOCRStage;
  @Mock
  private ExtractBasicsStage extractBasicsStage;
  @Mock
  private ExtractCostentryStage extractCostentryStage;


  @Mock
  private ImageBillDTO dto;

  @BeforeMethod
  public void init() {
    pipeline = new ImageBillPipeline();
    MockitoAnnotations.initMocks(this);
  }

  public void testPipelineFlow() {
    pipeline.doPipeline(dto);
    verify(imageOCRStage, times(1)).process(dto);
    verify(extractBasicsStage, times(1)).process(dto);
    verify(extractCostentryStage, times(1)).process(dto);
  }
}
