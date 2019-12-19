package cn.net.hlk.data.pojo.log;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 【描 述】：系统日志
 * 【表 名】：t_log
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月8日
 */
@Data
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	//操作者
	private String operation;
	//动作
	private String action;
	//请求方法
	private String method;
	//类型
	private String type;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//操作详情
	private String details;
	//创建时间
	private Timestamp opt_time;
	//操作人id
	private String opt_id;
	//操作人名称
	private String opt_name;
	
}
