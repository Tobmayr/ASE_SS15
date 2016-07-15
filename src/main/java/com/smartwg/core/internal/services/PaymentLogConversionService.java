package com.smartwg.core.internal.services;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public interface PaymentLogConversionService {

  HashMap<Integer, BigDecimal[]> deserialize(byte[] log);

  byte[] serialize(HashMap<Integer, BigDecimal[]> logMap);
}
