package cn.net.hlk.data.service;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: RemindService   
 * @Description:提醒service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年10月10日 下午2:53:02
 */
public interface RemindService {

	/**
	 * @Title: addRemind
	 * @discription 提醒新增
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:07:04     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addRemind(PageData pd);

	/**
	 * @Title: updateRemind
	 * @discription 提醒修改
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:07:22     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updateRemind(PageData pd);

	/**
	 * @Title: searchRemind
	 * @discription 提醒条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:14:29     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchRemind(Page page);

}
