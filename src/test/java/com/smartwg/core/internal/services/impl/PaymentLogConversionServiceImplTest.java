package com.smartwg.core.internal.services.impl;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.services.PaymentLogConversionService;

public class PaymentLogConversionServiceImplTest {
  private PaymentLogConversionService service;

  private byte[] testLog;
  final HashMap<Integer, BigDecimal[]> map = new HashMap<>();
  private BigDecimal[] firsts;
  private BigDecimal[] seconds;

  @BeforeMethod
  public void setUp() throws IOException {
    service = new PaymentLogConversionServiceImpl();

    Path path = Paths.get("testlog.ser");
    testLog = Files.readAllBytes(path);
    firsts = new BigDecimal[] {new BigDecimal(1.0), new BigDecimal(-2.0), new BigDecimal(1.0)};
    seconds =
        new BigDecimal[] {new BigDecimal(33.33), new BigDecimal(33.33), new BigDecimal(-66.66)};
    map.put(1, firsts);
    map.put(2, seconds);
  }

  @Test
  public void serialize_Succeed() throws IOException {
    byte[] result = service.serialize(map);
    Assert.assertEquals(result, testLog);
  }

  @Test
  public void deserialize_succeed() {
    final HashMap<Integer, BigDecimal[]> result = service.deserialize(testLog);
    assertEquals(result.size(), 2);
    BigDecimal[] resultOne = result.get(1);
    BigDecimal[] resultTwo = result.get(2);

    Assert.assertEquals(resultOne.length, 3);
    Assert.assertEquals(resultTwo.length, 3);

    Assert.assertEquals(resultOne[0], firsts[0]);
    Assert.assertEquals(resultOne[1], firsts[1]);
    Assert.assertEquals(resultOne[2], firsts[2]);

    Assert.assertEquals(resultTwo[0], seconds[0]);
    Assert.assertEquals(resultTwo[1], seconds[1]);
    Assert.assertEquals(resultTwo[2], seconds[2]);
  }
}
