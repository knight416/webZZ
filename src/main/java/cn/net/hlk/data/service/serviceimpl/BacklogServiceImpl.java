package cn.net.hlk.data.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.net.hlk.data.mapper.BacklogMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.BacklogService;
import cn.net.hlk.data.service.InformationService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;

import com.alibaba.fastjson.JSON;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: BacklogServiceImpl   
 * @Description:待办 service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:29:54
 */
@Service
public class BacklogServiceImpl implements BacklogService {

	@Autowired
	private BacklogMapper backlogMapper;
	@Autowired
	private InformationService informationService;

	/**
	 * @Title: addBacklog
	 * @discription 待办新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:39:39      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.BacklogService#addBacklog(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addBacklog(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			if(StringUtil2.isNotEmpty(pd.get("electronic_apply_id"))){
				//处理申请信息
			}
			pd.put("backlog_id", UuidUtil.get32UUID());//主键生成
			backlogMapper.addBacklog(pd);//待办添加
			int n = informationService.xmppSend(pd,1);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: updateBacklog
	 * @discription 待办修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:50:29      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.BacklogService#updateBacklog(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public ResponseBodyBean updateBacklog(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			
			backlogMapper.updateBacklog(pd);//待办修改
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: searchBacklog
	 * @discription 待办条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:18:04      
	 * @param page
	 * @return     
	 * @see cn.net.hlk.data.service.BacklogService#searchBacklog(cn.net.hlk.data.pojo.Page)
	 */
	@Override
	public ResponseBodyBean searchBacklog(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = backlogMapper.searchBacklogPgListPage(page);
			resData.put("list", pdList);
			resData.put("page", page);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: getBacklogByDossierId
	 * @discription 获取待办信息 根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:14:04      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.BacklogService#getBacklogByDossierId(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public List<PageData> getBacklogByDossierId(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		List<PageData> pdList = new ArrayList<PageData>();
		try {
			pdList = backlogMapper.getBacklogByDossierId(pd);
			resData.put("list", pdList);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return pdList;
	}
	
}
