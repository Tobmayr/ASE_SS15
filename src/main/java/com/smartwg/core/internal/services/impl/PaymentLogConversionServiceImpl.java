package com.smartwg.core.internal.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartwg.core.internal.services.PaymentLogConversionService;

/**
 * @author Tobias Ortmayr (to)
 */
@Named
public class PaymentLogConversionServiceImpl implements PaymentLogConversionService {
  private static final Logger logger = LoggerFactory
      .getLogger(PaymentLogConversionServiceImpl.class);

  @SuppressWarnings("unchecked")
  @Override
  public HashMap<Integer, BigDecimal[]> deserialize(byte[] log) {
    ByteArrayInputStream ios = new ByteArrayInputStream(log);
    ObjectInputStream is = null;
    try {
      is = new ObjectInputStream(ios);
      return (HashMap<Integer, BigDecimal[]>) is.readObject();
    } catch (IOException e) {
      logger.error("IOException occured:\n" + e.getLocalizedMessage());
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      logger.error("ClassNotFoundException:\n" + e.getLocalizedMessage());
      e.printStackTrace();
    } finally {
      try {
        if (is != null) {
          is.close();
        }
        ios.close();
      } catch (IOException e) {
        // ignore close exception
      }
    }
    return null;
  }

  @Override
  public byte[] serialize(HashMap<Integer, BigDecimal[]> logMap) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(logMap);
      return bos.toByteArray();
    } catch (IOException e) {
      logger.error("IOException occured:\n" + e.getLocalizedMessage());
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        bos.close();
      } catch (IOException e) {
        // ignore close exception
      }

    }
    return null;
  }

}
