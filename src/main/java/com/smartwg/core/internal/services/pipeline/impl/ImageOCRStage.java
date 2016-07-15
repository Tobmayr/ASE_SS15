package com.smartwg.core.internal.services.pipeline.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.inject.Named;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartwg.core.internal.services.pipeline.Stage;

/**
 * In this stage the the image will be converted into a respective String representation
 *
 * @author Tobias Ortmayr (to)
 */
@Named
public class ImageOCRStage implements Stage<ImageBillDTO> {
  private static final Logger logger = LoggerFactory.getLogger(ImageOCRStage.class);
  /**
   * Option value, if enabled the input image will be converted in a binary image before the ocr
   * process
   */
  public static final int OPTION_USE_BINARY_SOURCE = 1;
  /**
   * Option value, if enabled the german training data files will be used for the OCR-process. 
   * Keep in mind that this is an experimental option
   */
  public static final int OPTION_USE_GERMAN_TRAINDATA=2;

  private static boolean binarysource;
  private static boolean germantrain;

  /**
   * Processes the ImageBillDTO and converts the text of the image into a String representation
   * using a OCR-Library (atm: tess4J) The dto should only be changed if the passed image is valid
   * -> not null
   * 
   * In addition its possible to enable various options to improve the result string:
   * 
   * 
   * @param dto ImageBillDTO on which all pipeline stages are operating
   * @throws NullPointerException if the passed parameter is null
   */
  @Override
  public void process(ImageBillDTO dto) {

    if (dto.getImage() != null) {
      ByteArrayInputStream in = null;
      try {
        in = new ByteArrayInputStream(dto.getImage());
        BufferedImage image = ImageIO.read(in);
        if (binarysource) {
          image = convertToBinary(image);
        }
        Tesseract tesseract = new Tesseract();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url;
        String path;
        if ((url = classloader.getResource("tess4j")) != null && (path = url.getFile()) != null) {
          File file = new File(path.replace("%20", " "));

          logger.info((file.exists() ? "File exists: " : "ERROR: File does not exist: ")
              + file.getAbsolutePath());

          
          tesseract.setDatapath(file.getAbsolutePath());
          if (germantrain){
            tesseract.setLanguage("deu");
          }
          String conversionString = tesseract.doOCR(image);
          conversionString = improve(conversionString);
          dto.setConversionString(conversionString);

        } else {
          throw new IOException("URL or path was null.");
        }
      } catch (IOException e) {
        logger.error("IOException occurred:/n" + e.getLocalizedMessage());
        e.printStackTrace();
      } catch (TesseractException e) {
        logger.error("TesseractException occurred:/n" + e.getLocalizedMessage());
        e.printStackTrace();
      } finally {
        try {
          if (in != null) {
            in.close();
          }
        } catch (IOException e) {
          // ignore exception on close
        }
      }
    }
  }


  /**
   * This method is used for enabling a certain option by using the respective static Constant.
   * Available options: OPTION_USE_BINARY_SOURCE, OPTION_USE_GERMAN_TRAINDATA
   * 
   * @param option
   */
  public  void enableOption(int option) {
    switch (option) {
      case 1:
        binarysource = true;
        break;
      case 2:
        germantrain=true;
        break;
      default:
        break;
    }
  }

  /**
   * This method is used for disabling a certain option by using the respective static Constant.
   * Available options: OPTION_USE_BINARY_SOURCE
   * 
   * @param option
   */
  public  void disableOption(int option) {
    switch (option) {
      case 1:
        binarysource = false;
        break;
      default:
        break;
    }
  }


  private String improve(String conversionString) {

    return conversionString.replace("~", "-").replace(",", ".");
  }

  private BufferedImage convertToBinary(BufferedImage img) {

    BufferedImage gray =
        new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    Graphics2D g = gray.createGraphics();
    g.drawImage(img, 0, 0, null);
    return gray;

  }
}
