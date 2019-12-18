
/**   
* @Title: BaseController.java 
* @Package cn.net.hylink.data.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年8月15日 下午6:42:50 
* @version V1.0   
*/ 

package cn.net.hlk.data.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;




/** 
 * @author Wang Yingnan  
 */
public class BaseController {
	protected Logger logger =  LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
	/** 
	 * @return request 
	 */
	
	public HttpServletRequest getRequest() {
		return request;
	}
	/** 
	 * @param request 要设置的 request 
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	/** 
	 * @return response 
	 */
	
	public HttpServletResponse getResponse() {
		return response;
	}
	/** 
	 * @param response 要设置的 response 
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
    
    
    
}
