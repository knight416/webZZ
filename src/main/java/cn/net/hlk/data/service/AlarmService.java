package cn.net.hlk.data.service;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: AlarmService   
 * @Description:告警service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:06
 */
public interface AlarmService {

	/**
	 * @Title: addAlarm
	 * @discription 告警新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:56     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addAlarm(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 告警修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:39:51     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updateAlarm(PageData pd);

	/**
	 * @Title: searchAlarm
	 * @discription 告警条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:33     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchAlarm(Page page);

	/**
	 * @Title: getalarmCategory1
	 * @discription 获取预警列表
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午7:58:57     
	 * @return
	 */
	List<PageData> getalarmCategory1();

	/**
	 * @Title: getALarmByDossierId
	 * @discription 获取告警信息根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:07:57     
	 * @param pd
	 * @return
	 */
	List<PageData> getALarmByDossierId(PageData pd);

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:56:38     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getAlarmInfomationById(PageData pd);

}
