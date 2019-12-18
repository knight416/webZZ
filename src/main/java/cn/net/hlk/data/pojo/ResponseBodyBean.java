
/**   
* @Title: CallBackJson.java 
* @Package com.hctec.clb.pojo 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年5月22日 下午3:08:50 
* @version V1.0   
*/ 

package cn.net.hlk.data.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
 
/** 
* @author Wang Yingnan  
*/

/** 
* @author Wang Yingnan  
*/
public class ResponseBodyBean implements Serializable{

	private ReasonBean reason;
	private Object result;
	 
	
	/** 
	 * @return result 
	 */
	
	public Object getResult() {
		return result;
	}
	/** 
	 * @param result 要设置的 result 
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	 
	
	/** 
	 * @return reason 
	 */
	
	public ReasonBean getReason() {
		return reason;
	}
	/** 
	 * @param reason 要设置的 reason 
	 */
	public void setReason(ReasonBean reason) {
		this.reason = reason;
	}
	
	/** (非 Javadoc) 
	*  
	*  
	* @return 
	* @see java.lang.Object#toString() 
	*/
	@Override
	public String toString() {
		return "ResponseBodyBean [reason=" + reason + ", result=" + result
				+ "]";
	}
	
	/** 
	*  
	*  
	* @param reason
	* @param result 
	*/ 
	
	public ResponseBodyBean(ReasonBean reason, Object result) {
		super();
		this.reason = reason;
		this.result = result;
	}
	
	/** 
	*  
	*   
	*/ 
	
	public ResponseBodyBean() {
		super();
		// TODO Auto-generated constructor stub
	}

 
 
	
}
