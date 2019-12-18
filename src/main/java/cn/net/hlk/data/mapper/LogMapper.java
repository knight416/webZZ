
/**   
* @Title: LogMapper.java 
* @Package cn.net.hylink.data.mapper 
* @Description: TODO(日志存储到数据库) 
* @author Wang Yingnan    
* @date 2018年1月15日 上午10:12:34 
* @version V1.0   
*/ 

package cn.net.hlk.data.mapper;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

/** 
 * @author Wang Yingnan  
 */
public interface LogMapper {

	
	/**
	 * @Title: saveLog
	 * @discription 日志新增
	 * @param pd
	 * @return
	 */
	void saveLog(PageData logPd);

	/**
	 * @Title: getLogListForMenuPgListPage
	 * @discription 日志数据列表查询
	 * @param pd
	 * @return
	 */
	List<PageData> queryLogPgListPage(Page page);

}
