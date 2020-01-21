package cn.net.hlk.data.mapper;

import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.poi.easypoi.ScorePojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

import java.util.List;

public interface PostMapper {

	/**
	 * @Title: addAlarm
	 * @discription 岗位添加
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:22:35     
	 * @param pd
	 */
	void addPost(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 岗位修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:41:45     
	 * @param pd
	 */
	void updatePost(PageData pd);

	/**
	 * @Title: searchAlarmPgListPage
	 * @discription 岗位条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:09     
	 * @param page
	 * @return
	 */
	List<PageData> searchPostPgListPage(Page page);

	/**
	 * @Title: getAlarmById
	 * @discription 根据岗位id 获取岗位信息
	 * @author 张泽恒       
	 * @created 2018年10月18日 上午9:04:15     
	 * @param pd
	 * @return
	 */
	PageData getPostById(PageData pd);

	/**
	 * @Title applicationListExport
	 * @Description 导出岗位报名
	 * @author 张泽恒
	 * @date 2020/1/21 18:13
	 * @param [pd]
	 * @return java.util.List<cn.net.hlk.data.poi.easypoi.ScorePojo>
	 */
	List<ScorePojo> applicationListExport(PageData pd);

	/**
	 * @Title postExport
	 * @Description 岗位导出
	 * @author 张泽恒
	 * @date 2020/1/21 20:25
	 * @param [pd]
	 * @return java.util.List<cn.net.hlk.data.poi.easypoi.ScorePojo>
	 */
	List<PostPojo> postExport(PageData pd);

	/**
	 * @Title VerificationAdd
	 * @Description 验证是否允许投递
	 * @author 张泽恒
	 * @date 2020/1/2 19:16
	 * @param [pd]
	 * @return int
	 */
	// int VerificationAdd(PageData pd);
}
