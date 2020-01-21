package cn.net.hlk.data.service;

import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

import java.util.List;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: AlarmService   
 * @Description:岗位service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:06
 */
public interface PostService {

	/**
	 * @Title: addAlarm
	 * @discription 岗位新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:56     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addPost(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 岗位修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:39:51     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updatePost(PageData pd);

	/**
	 * @Title: searchAlarm
	 * @discription 岗位条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:33     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchPost(Page page);

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据岗位id 获取岗位信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:56:38     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getPostById(PageData pd);

	/**
	 * @Title postImport
	 * @Description 岗位导入
	 * @author 张泽恒
	 * @date 2020/1/21 21:01
	 * @param [personList]
	 * @param s
	 * @param xid
	 * @param uid
	 * @param optName
	 * @return void
	 */
	void postImport(List<PostPojo> personList, String system_id, String xid, String uid, String optName);
}
