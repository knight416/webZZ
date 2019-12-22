package cn.net.hlk.data.pojo.dictionary;

import java.util.List;

public class DictionaryBean {
	
	/** 【描 述】：id */
	private String id;
	/** 【描 述】：name */
	private String name;
	/** 【描 述】：pid */
	private String pid;
	/** 【描 述】：条件 */
	private String condition;
	/** 【描 述】：cname */
	private String cname;
	/** 【描 述】：编码 */
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	private List<DictionaryBean> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DictionaryBean> getChildren() {
		return children;
	}
	public void setChildren(List<DictionaryBean> children) {
		this.children = children;
	}
	

}
