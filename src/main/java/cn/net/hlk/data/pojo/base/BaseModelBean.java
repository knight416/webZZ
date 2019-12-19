
/**   
* @Title: BaseModelBean.java 
* @Package cn.net.hylink.bean 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年5月20日 上午11:07:56 
* @version V1.0   
*/ 

package cn.net.hlk.data.pojo.base;

import java.sql.Timestamp;


/** 
 * @author Wang Yingnan  
 */
public class BaseModelBean {

	private Timestamp createtime;
	private Timestamp updatetime;
	private String createuser;
	private String optuser;
	private Integer visibale;
	private String flag;
	private String source;
	/** 
	 * @return createtime 
	 */
	
	public Timestamp getCreatetime() {
		return createtime;
	}
	/** 
	 * @param createtime 要设置的 createtime 
	 */
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	/** 
	 * @return updatetime 
	 */
	
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	/** 
	 * @param updatetime 要设置的 updatetime 
	 */
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	/** 
	 * @return createuser 
	 */
	
	public String getCreateuser() {
		return createuser;
	}
	/** 
	 * @param createuser 要设置的 createuser 
	 */
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	/** 
	 * @return optuser 
	 */
	
	public String getOptuser() {
		return optuser;
	}
	/** 
	 * @param optuser 要设置的 optuser 
	 */
	public void setOptuser(String optuser) {
		this.optuser = optuser;
	}
	/** 
	 * @return visibale 
	 */
	
	public Integer getVisibale() {
		return visibale;
	}
	/** 
	 * @param visibale 要设置的 visibale 
	 */
	public void setVisibale(Integer visibale) {
		this.visibale = visibale;
	}
	/** 
	 * @return flag 
	 */
	
	public String getFlag() {
		return flag;
	}
	/** 
	 * @param flag 要设置的 flag 
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/** 
	 * @return source 
	 */
	
	public String getSource() {
		return source;
	}
	/** 
	 * @param source 要设置的 source 
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	
	
	/** 
	*  
	*   
	*/ 
	
	public BaseModelBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	/** 
	*  
	*  
	* @param createtime
	* @param updatetime
	* @param createuser
	* @param optuser
	* @param visibale
	* @param flag
	* @param source 
	*/ 
	
	public BaseModelBean(Timestamp createtime, Timestamp updatetime,
			String createuser, String optuser, Integer visibale, String flag,
			String source) {
		super();
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.createuser = createuser;
		this.optuser = optuser;
		this.visibale = visibale;
		this.flag = flag;
		this.source = source;
	}
	/** (非 Javadoc) 
	*  
	*  
	* @return 
	* @see Object#toString()
	*/
	@Override
	public String toString() {
		return "BaseModelBean [createtime=" + createtime + ", updatetime="
				+ updatetime + ", createuser=" + createuser + ", optuser="
				+ optuser + ", visibale=" + visibale + ", flag=" + flag
				+ ", source=" + source + "]";
	}
	
	
	
}
