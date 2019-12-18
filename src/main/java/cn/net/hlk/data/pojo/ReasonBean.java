
/**   
* @Title: ReasonBean.java 
* @Package cn.net.hylink.entity 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年6月10日 下午2:18:36 
* @version V1.0   
*/ 

package cn.net.hlk.data.pojo;


/** 
 * @author Wang Yingnan  
 */
public class ReasonBean {

	private String code;
	private String text;
	/** 
	 * @return code 
	 */
	
	public String getCode() {
		return code;
	}
	/** 
	 * @param code 要设置的 code 
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/** 
	 * @return text 
	 */
	
	public String getText() {
		return text;
	}
	/** 
	 * @param text 要设置的 text 
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/** (非 Javadoc) 
	*  
	*  
	* @return 
	* @see java.lang.Object#toString() 
	*/
	@Override
	public String toString() {
		return "ReasonBean [code=" + code + ", text=" + text + "]";
	}
	
	/** 
	*  
	*  
	* @param code
	* @param text 
	*/ 
	
	public ReasonBean(String code, String text) {
		super();
		this.code = code;
		this.text = text;
	}
	
	/** 
	*  
	*   
	*/ 
	
	public ReasonBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
