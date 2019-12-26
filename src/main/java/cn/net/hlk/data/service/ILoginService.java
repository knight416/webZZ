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

	/**
	 * @Title findPass
	 * @Description 密码找回
	 * @author 张泽恒
	 * @date 2019/12/26 22:30
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean findPass(PageData pd);

	/**
	 * @Title findPassCheck
	 * @Description 密码找回验证
	 * @author 张泽恒
	 * @date 2019/12/26 23:06
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean findPassCheck(PageData pd);

	/**
	 * @Title updatePasswordForFind
	 * @Description 密码找回 改密
	 * @author 张泽恒
	 * @date 2019/12/26 23:25
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean updatePasswordForFind(PageData pd);
}
