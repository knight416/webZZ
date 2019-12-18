package cn.net.hlk.data.mapper;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

public interface BacklogMapper {

	/**
	 * @Title: addBacklog
	 * @discription 待办新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:50:47     
	 * @param pd
	 */
	void addBacklog(PageData pd);

	/**
	 * @Title: updateBacklog
	 * @discription 待办修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:51:01     
	 * @param pd
	 */
	void updateBacklog(PageData pd);

	/**
	 * @Title: searchBacklogPgListPage
	 * @discription 待办条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:19:55     
	 * @param page
	 * @return
	 */
	List<PageData> searchBacklogPgListPage(Page page);

	/**
	 * @Title: getBacklogByDossierId
	 * @discription 获取待办信息 根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:15:34     
	 * @param pd
	 * @return
	 */
	List<PageData> getBacklogByDossierId(PageData pd);

	/**
	 * @Title: getbacklogById
	 * @discription 
	 * @author 张泽恒       
	 * @created 2018年11月1日 下午2:58:55     
	 * @param pd
	 * @return
	 */
	PageData getbacklogById(PageData pd);

}
