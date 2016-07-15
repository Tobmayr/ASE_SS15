package com.smartwg.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartwg.core.controllers.NavigationBean;

/**
 * This class handles incoming Requests and determines whether the application is accessed from a
 * mobile device and if the user has the privileges to access the page (a.k.a. is logged in)
 * 
 * @author Tobias Ortmayr, to
 *
 */
public class AuthAndMobileFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(AuthAndMobileFilter.class);

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      // check whether session variable is set
      HttpServletRequest req = (HttpServletRequest) request;
      HttpServletResponse res = (HttpServletResponse) response;
      HttpSession ses = req.getSession(false);
      String userAgent = req.getHeader("user-agent");
      String accept = req.getHeader("Accept");

      if (userAgent != null && accept != null) {
        String reqURI = req.getRequestURI();
        UserAgentInfo agent = new UserAgentInfo(userAgent);
        String contextPath = req.getContextPath();
        // Check if mobile request
        boolean mobileRequest =
            reqURI.startsWith(contextPath + "/m/")
                || (agent.isMobileBrowser() || agent.isTabletBrowser());
        if (mobileRequest)
          NavigationBean.setMobileAcces(true);
        else
          NavigationBean.setMobileAcces(false);

        if (!reqURI.contains("javax.faces.resource")
            && !reqURI.contains(contextPath + "/resources") && mobileRequest) {

          // change uri to mobile folder
          if (!reqURI.startsWith(contextPath + "/m/")) {
            logger.info(reqURI);
            reqURI = reqURI.substring(contextPath.length(), req.getRequestURI().length());
            reqURI = contextPath + "/m/" + reqURI;
            logger.info("URI change mobile: " + reqURI);
          }
        }

        // Now check if logged in
        if (reqURI.indexOf("/public/index.jsf") >= 0
            && (ses != null && ses.getAttribute("username") != null)) {
          res.sendRedirect(req.getContextPath() + "/pages/main.jsf");
          return;
        }
        if (reqURI.indexOf("/public/") >= 0 || reqURI.indexOf("/m/public/") >= 0
            || (ses != null && ses.getAttribute("username") != null)
            || reqURI.contains("javax.faces.resource")) {
          chain.doFilter(request, response);
        } else {
          logger.info("Not logged in");
          String path = "/public/index.jsf";
          if (mobileRequest)
            path = "/m" + path;
          res.sendRedirect(req.getContextPath() + path);
        }
      }
    } catch (Exception t) {
      logger.error(t.getMessage());
    }


  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub

  }

}
