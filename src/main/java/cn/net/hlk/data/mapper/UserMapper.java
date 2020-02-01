package cn.net.hlk.data.mapper;


import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.resource.ResourceBean;
import cn.net.hlk.data.pojo.role.Role;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.pojo.user.UserExcel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 【描 述】：<>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月5日
 */
@Repository
public interface UserMapper {
	/**用户查询方法*/
	List<PageData> findAllUserPgListPage(Page page);
	/**添加用户*/
	Integer addUser(PageData pd);
	/** 根据id查询用户 */
	PageData findById(PageData pd);
	/** 修改用户 */
	Integer editUser(PageData pd);
/*获取用户其他信息*/
	PageData getuserOtherMessage(String uid);

	/**
	 * @Title yanzheng
	 * @Description 验证是否允许删除
	 * @author 张泽恒
	 * @date 2020/2/1 17:30
	 * @param [pd]
	 * @return int
	 */
	int yanzheng(PageData pd);
}
