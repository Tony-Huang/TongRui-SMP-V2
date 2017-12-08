package com.hdp.smp.front.ui.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter that ensures the FacesServlet is called before rendering
 * the page.  Most useful for getting JSP Faces pages to work correctly.
 * <p>
 * @author John Fallows
 */
public class RedirectFilter implements Filter 
{
  public static final String URL_PATTERN_PARAM = "faces-servlet-url-pattern";
  public static final String DEFAULT_URL_PATTERN = "/faces/*";

  public void init(
    FilterConfig filterConfig) throws ServletException
  {
    String pattern = filterConfig.getInitParameter(URL_PATTERN_PARAM);
    
    if (pattern == null)
      pattern = DEFAULT_URL_PATTERN;
      
    int offset = pattern.indexOf('*');
    if (offset > 1 && pattern.charAt(offset - 1) == '/')
      offset--;
      
    _servletPath = pattern.substring(0, offset);
    
    System.out.println("initialize... _servletPath="+ _servletPath);
  }

  public void destroy()
  {
    // Technically, we should dump _servletPath.  However,
    // OC4J is calling destroy(), then not calling init() before
    // using the Filter again!  So ignore destroy().
    //    _servletPath = null;
  }

  public void doFilter(
    ServletRequest  request, 
    ServletResponse response, 
    FilterChain     chain) throws IOException, ServletException
  {
    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      String servletPath = httpRequest.getServletPath(); 
      System.out.println("...doFilter...httpRequest.getgetServletPath()="+servletPath);
      
      if (!servletPath.startsWith(_servletPath))
      {
        servletPath = _servletPath + servletPath;
        String pathInfo = httpRequest.getPathInfo();
        String queryString = httpRequest.getQueryString();
        // Use a client-side redirect
        String url =
          (httpRequest.getContextPath() +
           servletPath + 
           (pathInfo == null ? "": pathInfo) +
           (queryString == null ? "": "?" + queryString));
        
        // Use a client-side redirect so that filters will run
        ((HttpServletResponse) response).sendRedirect(url);
        /*
          request.getRequestDispatcher(servletPath).
          forward(request, response);
        */
        return;
      }
    }
    chain.doFilter(request, response);

  }

  private String _servletPath;
}