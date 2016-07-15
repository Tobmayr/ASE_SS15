package com.smartwg.core.internal.services.pipeline.impl;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import util.TestDataConstants;

import com.smartwg.core.internal.services.pipeline.Stage;

@Test
public class ImageOCRStageTest {


  private Stage<ImageBillDTO> stage;
  private ImageBillDTO dto;

  @BeforeMethod
  public void init() throws IOException {
    stage = new ImageOCRStage();
    dto = new ImageBillDTO(null);
  }

  @Test(expectedExceptions = {NullPointerException.class})
  public void process_null() {
    stage.process(null);
  }

  public void process_ImageNull() {
    stage.process(dto);
  }

  /*
   * Simple Test, verfiying the result quality is not really possible via unittesting
   */
  public void process_withValidImage() throws IOException {
    Path path = Paths.get(TestDataConstants.DATA1_SOURCE_PATH);
    byte[] image = Files.readAllBytes(path);
    dto.setImage(image);
    stage.process(dto);
    assertNotNull(dto.getConversionString());
    assertNotEquals(dto.getConversionString(), "");
  }



}
