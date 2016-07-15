package com.smartwg.core.util;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * This class provides methods for often used functionallity of the primefaces library
 * 
 * @author Tobias Ortmayr (to)
 *
 */
public class PrimefacesUtil {

  private PrimefacesUtil() {};

  /**
   * Retrieves the current instance of the FacesContext and adds the validation message if the
   * context is not null
   * 
   * @param severity Severity level of the message
   * @param message The actual validation message
   */
  public static void addValidationMessage(final FacesMessage.Severity severity, final String message) {
    final FacesContext context = FacesContext.getCurrentInstance();
    if (context != null) {
      final FacesMessage msg = new FacesMessage(severity, message, null);
      context.addMessage(null, msg);
    }
  }

  /**
   * Retrieves the requestParameterMap for the current request. If depenedt objects like the
   * external context are null this method returns null as well
   * 
   * @return current RequestParameterMap or null
   */
  public static Map<String, String> getRequestParameterMap() {
    FacesContext context = FacesContext.getCurrentInstance();
    if (context != null) {
      ExternalContext externalContext = context.getExternalContext();
      if (externalContext != null) {
        return externalContext.getRequestParameterMap();
      }
    }
    return null;
  }
}
