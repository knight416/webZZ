package cn.net.hlk.data.pojo.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 【描 述】：资源实体类
 * 【表 名】：t_resource
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	
 * @version  version 1.0
 * @since    2018年1月16日
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceBean {
	
	/** 【描 述】：id */
	private String id;
	/** 【描 述】：rid */
	private Integer rid;
	/** 【描 述】：是否选中 */
	private Integer checked;
	/** 【描 述】：资源名称 */
	private String name;
	/** 【描 述】：条件 */
	private String cname;
	/** 【描 述】：深度 */
	private Integer depth;
	/** 【描 述】：资源类型 */
	private String type;
	/** 【描 述】：pid */
	private String pid;
	/** 【描 述】：子集 */
	private List<ResourceBean> children;
	/** 【描 述】：资源编码 */
	private String resourceCode;
	
	private String appId;
	private int isApp;
	
}
