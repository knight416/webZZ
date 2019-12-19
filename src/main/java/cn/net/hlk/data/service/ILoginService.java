package cn.net.hlk.data.service;



import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.user.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 【描 述】：登陆service接口
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月16日
 */
public interface ILoginService {

	/** 
	 * 【描 述】：用户登陆
	 * @param pd
	 * @return
	 */
	ResponseBodyBean login(PageData pd);
	
	PageData loginToken(User user);
	/** 
	 * 【描 述】：修改密码
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updatePassword(PageData pd);

	ResponseBodyBean getIPpath(HttpServletRequest request);

}
