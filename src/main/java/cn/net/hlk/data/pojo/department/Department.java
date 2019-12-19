package cn.net.hlk.data.pojo.department;


import lombok.Data;

import java.util.List;

@Data
public class Department implements java.io.Serializable {

	/** 【描 述】：serialVersionUID */
	private static final long serialVersionUID = -3560181513943618949L;
	
	/** 【描 述】：主键 */
	private String id;
    /** 【描 述】：父id */
    private String parentId;
    /** 【描 述】：资源id */
    private String resourceId;
    /** 【描 述】：深度 */
    private Integer depth;
    /** 【描 述】：机构编码 */
    private String code;
    /** 【描 述】：机构名称 */
    private String name;
    /** 【描述】：警种 */
    private String policeCategory; 
    /** 【描 述】：排序 */
    private Integer sort;
	/** 【描 述】：状态 */
    private Integer state;
    /** 【描 述】：操作时间 */
    private String optTime;
    /** 【描 述】：操作人Id */
    private String optId;
    /** 【描 述】：操作人名称 */
    private String optName;
    /** 【描 述】：逻辑删除 */
    private Integer visiable;
    /** 【描述】：子集 */
    private List<Department> childrenList;
	/** 【描述】：父节点名称 */
    private String parentName;
	/** 【描述】：拼音缩写 */
    private String namePyAbbr;
	/** 【描述】：机构别名 */
    private String alias;
}