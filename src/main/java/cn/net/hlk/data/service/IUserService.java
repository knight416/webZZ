package cn.net.hlk.data.service;



import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.resource.ResourceBean;
import cn.net.hlk.data.pojo.role.Role;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.pojo.user.UserExcel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 
 * 【描 述】：<用户管理相关接口>
 * 【表 名】：<t_user>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月5日
 */
public interface IUserService {
	/**用户查询方法*/
	List<PageData> findAllUserlistPage(Page page);
	/**逻辑删除用户*/
	ResponseBodyBean deleteUser(PageData pd);
	/**禁用用户*/
	ResponseBodyBean disableUser(PageData pd);
	/**启用用户*/
	Integer ableUser(PageData pd);
	/**添加用户*/
	Integer addUser(PageData pd);
	/** 修改用户 */
	Integer editUser(PageData pd);
	/** 根据id查询用户 */
	PageData findById(PageData pd);

}
