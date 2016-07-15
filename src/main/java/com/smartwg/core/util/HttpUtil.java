package com.smartwg.core.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class provides methods for often used functionality in the web context. like retrieving the
 * current session & request. And additionally methods for retrieving the session attributes fur the
 * currently logged in user.
 * 
 * @author Tobias Ortmayr (to)
 */
public class HttpUtil {
  /**
   * Retrieves the currently active HttpSession
   * 
   * @return current Session or null if there is no active session
   */

  private HttpUtil() {};

  public static HttpSession getSession() {
    ExternalContext context = getExternalContext();
    if (context != null) {
      Object session = context.getSession(false);
      if (session != null) {
        return (HttpSession) session;
      }
    }
    return null;
  }

  /**
   * Retrieves the current HttpRequest
   * 
   * @return current Request or null if there is no current Request
   */
  public static HttpServletRequest getRequest() {
    ExternalContext context = getExternalContext();
    if (context != null) {
      Object request = context.getRequest();
      if (request != null) {
        return (HttpServletRequest) request;
      }
    }
    return null;
  }

  /**
   * Retrieves the base URL under which the application is runnig
   * 
   * @return baseURL or null if the url cannot be retrieved
   */
  public static String getBaseURL() {
    HttpServletRequest request = getRequest();
    if (request != null) {
      String url = request.getRequestURL().toString();
      String baseURL =
          url.substring(0, url.length() - request.getRequestURI().length())
              + request.getContextPath();
      return baseURL;
    }
    return null;
  }

  /**
   * Retrieves the userName attribute of the current session
   * 
   * @return userName or null if there is no session or no currently logged in user
   */
  public static String getUserName() {
    HttpSession session = getSession();
    if (session != null) {
      return session.getAttribute("username").toString();
    }
    return null;
  }


  private static ExternalContext getExternalContext() {
    FacesContext instance = FacesContext.getCurrentInstance();
    if (instance != null) {
      return instance.getExternalContext();
    }
    return null;
  }
}
