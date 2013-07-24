package th.co.aerothai.digitalsignage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {
	
	 private FilterConfig config;

	  public void doFilter(ServletRequest req, ServletResponse resp,
	      FilterChain chain) throws IOException, ServletException {
	    if (((HttpServletRequest) req).getSession().getAttribute(
	        "user") == null) {
	      ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath()+"/login.jsf");
//	    	((HttpServletResponse) resp).sendRedirect("../login/");
	    } else {
	      chain.doFilter(req, resp);
	    }
	  }

	  public void init(FilterConfig config) throws ServletException {
	    this.config = config;
	  }

	  public void destroy() {
	    config = null;
	  }

//	private FilterConfig filterConfig = null;
//	private ServletContext servletContext = null;
//	
//	public void init(FilterConfig aFilterConfig) throws ServletException {
//		filterConfig = aFilterConfig;
//		servletContext = filterConfig.getServletContext();
//	}
//	
//	public void destroy() {
//		filterConfig = null;
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		
//		HttpServletRequest httpRequest = (HttpServletRequest)request;
//		HttpServletResponse httpResponse = (HttpServletResponse)response;
//		HttpSession session = httpRequest.getSession();
//		
//		String requestPath = httpRequest.getServletPath();
//		String contextPath = httpRequest.getContextPath();
//		UserSession user = (UserSession) session.getAttribute("UserSession");
//		//System.out.println("User : "+user);
//		//System.out.println("Context :"+contextPath);
//		System.out.println("Request :"+requestPath);
//		if (user == null && !requestPath.equals("/pages/login.jsf")) {
//			// redirect to welcome page
//			//System.out.println("Redirect");
//			session.setAttribute("origin", requestPath);
//			httpResponse.sendRedirect(contextPath+"/pages/login.jsf");
//		} else {
//			//System.out.println("Render");
//			session.removeAttribute("origin");
//			chain.doFilter(request, response);
//		}
//		
//	}


}
