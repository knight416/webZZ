package cn.net.hlk.data.mapper;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

import java.util.List;

public interface NewsMapper {

	/**
	 * @Title: addAlarm
	 * @discription 消息添加
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:22:35     
	 * @param pd
	 */
	void addNews(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:41:45     
	 * @param pd
	 */
	void updateNews(PageData pd);

	/**
	 * @Title: searchAlarmPgListPage
	 * @discription 告警条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:09     
	 * @param page
	 * @return
	 */
	List<PageData> searchNewsPgListPage(Page page);

	/**
	 * @Title: getAlarmById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月18日 上午9:04:15     
	 * @param pd
	 * @return
	 */
	PageData getNewsById(PageData pd);

}
