package cn.net.hlk.data.mapper;


import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.department.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JWTMapper {

    /** 
     * 【描 述】：查询用户所属机构
     * @param department
     * @return
     */
    Group getDepartment(String department);
	
    /** 
     * 【描 述】：查询用户管辖机构
     * @param department
     * @return
     */
	List<Group> getDepartmentList(String id);

	/**
	 * @Title: getRoleList
	 * @discription 根据用户id 获取用户角色
	 * @author 张泽恒       
	 * @created 2019年4月16日 下午4:53:46     
	 * @param id2
	 * @return
	 */
	List<PageData> getRoleList(String id2);
}
