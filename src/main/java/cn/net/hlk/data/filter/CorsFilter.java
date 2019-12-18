package cn.net.hlk.data.filter;

import org.springframework.stereotype.Component;  

import javax.servlet.*;  
import javax.servlet.http.HttpServletResponse;  
import java.io.IOException;  
/**
 * 
 * @author Hylink
 *跨域接口
 */
@Component 
public class CorsFilter implements Filter {
	
	 final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);  
	
	    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {  
	    	HttpServletResponse response = (HttpServletResponse) res;  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE,PUT,PATCH");  
	        response.setHeader("Access-Control-Max-Age", "3600");  
	        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type, Accept, Access-Token,token,Authorization,authorization");  
	        chain.doFilter(req, res);  
	    }  
	    public void init(FilterConfig filterConfig) {}  
	    public void destroy() {}  

}
