package cn.net.hlk.data.pojo.department;

import lombok.Data;

/**
 * 【描 述】：机构实体类
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   董洪伟
 * @version  version 1.0
 * @since    2018年1月15日
 */
@Data
public class Group {
	
	/** 【描 述】：id */
    private String id;
    /** 【描 述】：父id */
    private Integer parentId;
	/** 【描 述】：深度 */
    private Integer depth;
    /** 【描 述】：机构名称 */
    private String name;
    /** 【描 述】：机构编码 */
    private String code;

}
