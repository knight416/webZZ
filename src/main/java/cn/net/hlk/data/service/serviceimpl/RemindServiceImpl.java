package cn.net.hlk.data.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.net.hlk.data.mapper.RemindMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.InformationService;
import cn.net.hlk.data.service.RemindService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: RemindServiceImpl   
 * @Description:提醒service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年10月10日 下午2:54:18
 */
@Service
public class RemindServiceImpl extends BaseServiceImple implements RemindService {

	@Autowired
	private RemindMapper remindMapper;
	@Autowired
	private InformationService informationService;

	/**
	 * @Title: addRemind
	 * @discription 提醒新增
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:08:19      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.RemindService#addRemind(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addRemind(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			if(StringUtil2.isNotEmpty(pd.get("electronic_apply_id"))){
				//处理申请信息
			}
			
			
			pd.put("remind_id", UuidUtil.get32UUID());//主键生成
			remindMapper.addRemind(pd);//通知添加
			int n = informationService.xmppSend(pd,2);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: updateRemind
	 * @discription 提醒修改
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:08:30      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.RemindService#updateRemind(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public ResponseBodyBean updateRemind(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			
			remindMapper.updateRemind(pd);//提醒修改
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: searchRemind
	 * @discription 提醒条件搜索
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:18:30      
	 * @param page
	 * @return     
	 * @see cn.net.hlk.data.service.RemindService#searchRemind(cn.net.hlk.data.pojo.Page)
	 */
	@Override
	public ResponseBodyBean searchRemind(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = remindMapper.searchRemindPgListPage(page);
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
	
}
