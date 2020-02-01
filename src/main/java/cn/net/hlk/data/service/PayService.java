package cn.net.hlk.data.service;

import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

import java.util.List;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: AlarmService   
 * @Description:支付service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:06
 */
public interface PayService {

	/**
	 * @Title: addAlarm
	 * @discription 支付新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:56     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addPay(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 支付修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:39:51     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updatePay(PageData pd);

	/**
	 * @Title: searchAlarm
	 * @discription 支付条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:33     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchPay(Page page);

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据支付id 获取支付信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:56:38     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getPayById(PageData pd);


	/**
	 * @Title closeOtherPay
	 * @Description 其他支付关闭
	 * @author 张泽恒
	 * @date 2020/2/1 11:48
	 * @param [pdd]
	 * @return void
	 */
	void closeOtherPay(PageData pdd);
}
