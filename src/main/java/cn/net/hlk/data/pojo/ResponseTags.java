
/**   
* @Title: ResponseTagLib.java 
* @Package cn.net.hylink.clb.pojo 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年6月27日 上午10:03:34 
* @version V1.0   
*/ 

package cn.net.hlk.data.pojo;

import java.util.List;



/** 
 * @author Wang Yingnan  
 */
public class ResponseTags {

	private String code;
	private String text;
	private boolean isTarget;
	private boolean isChecked;
	private String errorCode;
	private String errorText;
	private String property;
	private List<PageData> infos;
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
	 
	 
	/** 
	 * @return isTarget 
	 */
	
	public boolean isTarget() {
		return isTarget;
	}
	/** 
	 * @param isTarget 要设置的 isTarget 
	 */
	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
	/** 
	 * @return isChecked 
	 */
	
	public boolean isChecked() {
		return isChecked;
	}
	/** 
	 * @param isChecked 要设置的 isChecked 
	 */
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	/** 
	 * @return errorCode 
	 */
	
	public String getErrorCode() {
		return errorCode;
	}
	/** 
	 * @param errorCode 要设置的 errorCode 
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/** 
	 * @return errorText 
	 */
	
	public String getErrorText() {
		return errorText;
	}
	/** 
	 * @param errorText 要设置的 errorText 
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public List<PageData> getInfos() {
		return infos;
	}
	public void setInfos(List<PageData> infos) {
		this.infos = infos;
	}
	
}
