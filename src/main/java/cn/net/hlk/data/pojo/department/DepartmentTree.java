package cn.net.hlk.data.pojo.department;

import java.util.List;

/**
 * 【描 述】：<机构管理树状结构表>
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月12日
 */
public class DepartmentTree {
	/** 【描 述】：主键 对应 id*/
	private Integer value;
	 /** 【描 述】：父id */
    private Integer parentId;
	/** 【描 述】：机构编码 对应 code */
    private String key;
    /** 【描 述】：机构名称 对应 name */
    private String label;
    /** 【描述】：子集 */
    private List<DepartmentTree> children;
    
    
    
    public DepartmentTree(){}



	public DepartmentTree(Integer value, Integer parentId, String key, String label, List<DepartmentTree> children) {
		super();
		this.value = value;
		this.parentId = parentId;
		this.key = key;
		this.label = label;
		this.children = children;
	}



	public Integer getValue() {
		return value;
	}



	public void setValue(Integer value) {
		this.value = value;
	}



	public Integer getParentId() {
		return parentId;
	}



	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public List<DepartmentTree> getChildren() {
		return children;
	}



	public void setChildren(List<DepartmentTree> children) {
		this.children = children;
	}



	@Override
	public String toString() {
		return "DepartmentTree [value=" + value + ", parentId=" + parentId + ", key=" + key + ", label=" + label
				+ ", children=" + children + "]";
	}
    
  

}
