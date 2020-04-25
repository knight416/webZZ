package cn.net.hlk.data.service;

import cn.net.hlk.data.poi.easypoi.OperationPojo;
import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.poi.easypoi.ScorePojo;
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
public interface NewsOperationService {

	/**
	 * @Title: addAlarm
	 * @discription 消息新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:56     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addNewsOperation(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:39:51     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updateNewsOperation(PageData pd);

	/**
	 * @Title: searchAlarm
	 * @discription 告警条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:33     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchNewsOperation(Page page);

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:56:38     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getNewsOperationById(PageData pd);

	/**
	 * @Title achievementIntroduction
	 * @Description 成绩导入
	 * @author 张泽恒
	 * @date 2020/1/22 9:53
	 * @param [personList, uid, optName]
	 * @return void
	 */
	void achievementIntroduction(List<ScorePojo> personList, String uid, String optName);

	/**
	 * @Title postPerformance
	 * @Description 岗位成绩前三获取
	 * @author 张泽恒
	 * @date 2020/1/29 21:22
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean postPerformance(PageData pd);

	/**  
	 * @Title updateInterviewPermit
	 * @Description 修改面试准考证
	 * @author 张泽恒
	 * @date 2020/1/29 22:26  
	 * @param [pd]  
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean  
	 */  
	ResponseBodyBean updateInterviewPermit(PageData pd);

	/**
	 * @Title findicket
	 * @Description 获取准考证信息
	 * @author 张泽恒
	 * @date 2020/1/29 22:55
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	PageData findicket(PageData pd);

	/**  
	 * @Title operationListExport
	 * @Description 报考导出
	 * @author 张泽恒
	 * @date 2020/4/25 16:25  
	 * @param [pd]  
	 * @return java.util.List<cn.net.hlk.data.poi.easypoi.OperationPojo>  
	 */  
	List<OperationPojo> operationListExport(PageData pd);
}
