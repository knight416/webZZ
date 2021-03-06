package cn.net.hlk.data.service;

import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.poi.easypoi.ScorePojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import org.springframework.web.multipart.MultipartFile;

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

	/**
	 * @Title saveApprovalFile
	 * @Description 文件上传
	 * @author 张泽恒
	 * @date 2019/12/24 17:11
	 * @param [police_idcard, file]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	PageData saveApprovalFile(String police_idcard, MultipartFile file,String optName);

	/**
	 * @Title delFile
	 * @Description 文件删除
	 * @author 张泽恒
	 * @date 2019/12/24 18:46
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean delFile(PageData pd);

	/**
	 * @Title delNews
	 * @Description 消息删除
	 * @author 张泽恒
	 * @date 2019/12/25 18:03
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	ResponseBodyBean delNews(PageData pd);

	/**
	 * @Title applicationListExport
	 * @Description 报考导出
	 * @author 张泽恒
	 * @date 2020/1/21 15:40
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	List<ScorePojo> applicationListExport(PageData pd);

	/**
	 * @Title postExport
	 * @Description 岗位导出
	 * @author 张泽恒
	 * @date 2020/1/21 20:23
	 * @param [pd]
	 * @return java.util.List<cn.net.hlk.data.poi.easypoi.ScorePojo>
	 */
	List<PostPojo> postExport(PageData pd);
}
