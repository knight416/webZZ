package cn.net.hlk.data.mapper;

import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.poi.easypoi.ScorePojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

import java.util.List;

public interface PayMapper {

	/**
	 * @Title: addAlarm
	 * @discription 支付添加
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:22:35     
	 * @param pd
	 */
	void addPay(PageData pd);

	/**
	 * @Title: updateAlarm
	 * @discription 支付修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:41:45     
	 * @param pd
	 */
	void updatePay(PageData pd);

	/**
	 * @Title: searchAlarmPgListPage
	 * @discription 支付条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:09     
	 * @param page
	 * @return
	 */
	List<PageData> searchPayPgListPage(Page page);

	/**
	 * @Title: getAlarmById
	 * @discription 根据支付id 获取支付信息
	 * @author 张泽恒       
	 * @created 2018年10月18日 上午9:04:15     
	 * @param pd
	 * @return
	 */
	PageData getPayById(PageData pd);

	/**
	 * @Title closeOtherPay
	 * @Description 其他支付关闭
	 * @author 张泽恒
	 * @date 2020/2/1 11:50
	 * @param [pdd]
	 * @return void
	 */
	void closeOtherPay(PageData pdd);
}
