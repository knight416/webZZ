package cn.net.hlk.data.mapper;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;

public interface RemindMapper {

	/**
	 * @Title: addRemind
	 * @discription 提醒新增
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:08:45     
	 * @param pd
	 */
	void addRemind(PageData pd);

	/**
	 * @Title: updateRemind
	 * @discription 提醒修改
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:09:00     
	 * @param pd
	 */
	void updateRemind(PageData pd);

	/**
	 * @Title: searchRemindPgListPage
	 * @discription 提醒条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:20:12     
	 * @param page
	 * @return
	 */
	List<PageData> searchRemindPgListPage(Page page);

}
