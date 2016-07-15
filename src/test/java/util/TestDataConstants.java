package util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.smartwg.core.internal.domain.dtos.CostEntryDTO;

public class TestDataConstants {
  private TestDataConstants() {};

  public static final String DATA1_SOURCE_PATH = "test-data/ir/images/01.jpg";
  public static final String DATA1_CONVERSION_STRING = "LIBRO" + "\n" + "VIELEN BANK FUR" + "\n"
      + "IHREN EINKAUF!" + "\n" + "Datum: 03.03.2015 Zeit: 13:22" + "\n"
      + "PUMA RUCKSACK APEX C 19.99" + "\n" + "Summe EUR 19.99" + "\n" + "Gegeben Bar 20.00" + "\n"
      + "Restgeld EUR 0 .01" + "\n" + "Betrag dankend erhalten" + "\n"
      + "Geschenkboxen, iTunes—Karten" + "\n" + "und elektronische Ladebons";

  public static final Date DATA1_DATE = DateTime.parse("2015-03-03").toDate();

  public static final String DATA2_CONVERSION_STRING = "INTERSPAR" + "\n" + "HYPERMARKT" + "\n"
      + "LandstraBer HauptstraBe 1b" + "\n" + "1030 Wien 01/7140242" + "\n"
      + " UID: ATU37198705; FN58299i" + "\n" + "EUR" + "\n" + "SPAREMMENTAL 1.79 A" + "\n"
      + "SPAR V. GEB. 0.90 A2" + "\n" + "YOGURT EIS 1.99 A" + "\n" + "BUTTERFLY 0.99 A" + "\n"
      + "FISOLEN 3.49 A+" + "\n" + "RAMA CREMEF. 1.19 A" + "\n" + "LAIBCHEN GEM 1.99 A" + "\n"
      + "SPAR 2MIN. R 1.29 A" + "\n" + "FRUCHTBECHER 2.99 A" + "\n" + "SUMME : 16.62" + "\n"
      + "GEGEBEN Bankomat 16.62" + "\n" + "BEZAHLI MAESTRO 16." + "\n" + "############7598 0001"
      + "\n" + "28006101 01.06.2015/19:44:54 7030";

  public static final List<CostEntryDTO> DATA2_COSTENTRIES = Arrays.asList(
      createCostEntry(1.79, "SPAREMMENTAL", 1), createCostEntry(0.90, "SPAR V. GEB.", 1),
      createCostEntry(1.99, "YOGURT EIS", 1), createCostEntry(0.99, "BUTTERFLY", 1),
      createCostEntry(3.49, "FISOLEN", 1), createCostEntry(1.19, "RAMA CREMEF.", 1),
      createCostEntry(1.99, "LAIBCHEN GEM", 1), createCostEntry(1.29, "SPAR 2MIN. R", 1),
      createCostEntry(2.99, "FRUCHTBECHER", 1));

  private static final CostEntryDTO createCostEntry(double amount, String desc, int times) {
    final CostEntryDTO costEntry = new CostEntryDTO();
    costEntry.setAmount(new BigDecimal(amount));
    costEntry.setName(desc);
    costEntry.setTimes(times);
    return costEntry;
  };


}
