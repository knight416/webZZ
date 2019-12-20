package cn.net.hlk.data.service;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

import java.util.List;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: AlarmService   
 * @Description:消息service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:06
 */
public interface NewsService {

	/**
	 * @Title: addAlarm
	 * @discription 消息新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:56     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addNews(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:39:51     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updateNews(PageData pd);

	/**
	 * @Title: searchAlarm
	 * @discription 告警条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:33     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchNews(Page page);

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:56:38     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getNewsById(PageData pd);

}
