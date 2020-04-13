package cn.net.hlk.data.mapper;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

import java.util.List;

public interface NewsOperationMapper {

	/**
	 * @Title: addAlarm
	 * @discription 消息添加
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:22:35     
	 * @param pd
	 */
	void addNewsOperation(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:41:45     
	 * @param pd
	 */
	void updateNewsOperation(PageData pd);

	/**
	 * @Title: searchAlarmPgListPage
	 * @discription 告警条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:09     
	 * @param page
	 * @return
	 */
	List<PageData> searchNewsOperationPgListPage(Page page);

	/**
	 * @Title: getAlarmById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月18日 上午9:04:15     
	 * @param pd
	 * @return
	 */
	PageData getNewsOperationById(PageData pd);

	/**
	 * @Title VerificationAdd
	 * @Description 验证是否允许投递
	 * @author 张泽恒
	 * @date 2020/1/2 19:16
	 * @param [pd]
	 * @return int
	 */
	int VerificationAdd(PageData pd);

	/**
	 * @Title postPerformance
	 * @Description 岗位成绩前三获取
	 * @author 张泽恒
	 * @date 2020/1/29 21:26
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	List<PageData> postPerformance(PageData pd);

	/**
	 * @Title updatePostPerformance
	 * @Description 前三状态修改
	 * @author 张泽恒
	 * @date 2020/1/29 21:59
	 * @param []
	 * @return void
	 */
	void updatePostPerformance(PageData pd);

	/**
	 * @Title findicket
	 * @Description 准考证信息获取
	 * @author 张泽恒
	 * @date 2020/1/29 22:57
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	PageData findicket(PageData pd);

	/**
	 * @Title 3
	 * @Description 获取数量
	 * @author 张泽恒
	 * @date 2020/2/25 15:44
	 * @param [pdn]
	 * @return int
	 */
	int getCount(PageData pdn);

	/**
	 * @Title getZWCount
	 * @Description 获取作为号
	 * @author 张泽恒
	 * @date 2020/4/13 14:43
	 * @param [pdn]
	 * @return int
	 */
	int getZWCount(PageData pdn);
}
