package com.smartwg.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides helper methods and often used functionality for the recognition pipelines. Additionally
 * methods used for quality metrics of the recognition process are provided
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class PipelineUtil {

  private PipelineUtil() {};

  /**
   * Method can be used to measure the similarity between to Strings. The method computes the
   * simularity using the LevenshteinnDistanceMethod.
   * 
   * @param s1 First String
   * @param s2 Second String
   * @return simularity in percent 0.0-1.0
   */
  public static double similarity(String s1, String s2) {
    String longer = s1, shorter = s2;
    if (s1.length() < s2.length()) { // longer should always have greater length
      longer = s2;
      shorter = s1;
    }
    int longerLength = longer.length();
    if (longerLength == 0) {
      return 1.0; /* both strings are zero length */
    }
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter))
        / (double) longerLength;
  }
}
