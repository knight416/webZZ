package cn.net.hlk.data.service;

import java.util.List;

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: BacklogService   
 * @Description:待办service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:29:20
 */
public interface BacklogService {

	/**
	 * @Title: addBacklog
	 * @discription 待办新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:38:20     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean addBacklog(PageData pd);

	/**
	 * @Title: updateBacklog
	 * @discription 待办修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:50:08     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean updateBacklog(PageData pd);

	/**
	 * @Title: searchBacklog
	 * @discription 待办条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:14:07     
	 * @param page
	 * @return
	 */
	ResponseBodyBean searchBacklog(Page page);

	/**
	 * @Title: getBacklogByDossierId
	 * @discription 获取待办信息根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:08:30     
	 * @param pd
	 * @return
	 */
	List<PageData> getBacklogByDossierId(PageData pd);

}
