package cn.net.hlk.data.mapper;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

public interface AlarmMapper {

	/**
	 * @Title: addAlarm
	 * @discription 告警添加
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:22:35     
	 * @param pd
	 */
	void addAlarm(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 告警修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:41:45     
	 * @param pd
	 */
	void updateAlarm(PageData pd);

	/**
	 * @Title: searchAlarmPgListPage
	 * @discription 告警条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:09     
	 * @param page
	 * @return
	 */
	List<PageData> searchAlarmPgListPage(Page page);

	/**
	 * @Title: getalarmCategory1
	 * @discription 获取预警列表
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午8:01:49     
	 * @return
	 */
	List<PageData> getalarmCategory1();

	/**
	 * @Title: getALarmByDossierId
	 * @discription 获取告警信息 根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:14:53     
	 * @param pd
	 * @return
	 */
	List<PageData> getALarmByDossierId(PageData pd);

	/**
	 * @Title: getAlarmById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月18日 上午9:04:15     
	 * @param pd
	 * @return
	 */
	PageData getAlarmById(PageData pd);

}
