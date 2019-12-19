package cn.net.hlk.data.pojo.resource;

import lombok.Data;

import java.util.List;

/**
 * 【描 述】：<资源树实体接收类>
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月25日
 */
@Data
public class ResourceTree {
	private String id;
	private String parentId;
	private Integer depth;
	private String name;
	private String type;
	private Integer checked; 
	private List<ResourceTree> children;
	private String appId;
	private int isApp;
	
}
