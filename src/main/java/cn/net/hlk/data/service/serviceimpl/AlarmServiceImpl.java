package cn.net.hlk.data.service.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.net.hlk.data.mapper.AlarmMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.InformationService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;

import com.alibaba.fastjson.JSON;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: AlarmServiceImpl   
 * @Description:告警service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:46
 */
@Service
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	private AlarmMapper alarmMapper;
	@Autowired
	private InformationService informationService;

	/**
	 * @Title: addAlarm
	 * @discription 告警新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:10:29      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#addAlarm(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addAlarm(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			if(StringUtil2.isEmpty(pd.get("dossier_message"))){
			}
			pd.put("alarm_id", UuidUtil.get32UUID());//主键生成
			alarmMapper.addAlarm(pd);//告警添加
			int n = informationService.xmppSend(pd,0);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: updateAlarm
	 * @discription 告警修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:40:38      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#updateAlarm(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public ResponseBodyBean updateAlarm(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			
			alarmMapper.updateAlarm(pd);//告警修改
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: searchAlarm
	 * @discription 告警条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:15:14      
	 * @param page
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#searchAlarm(cn.net.hlk.data.pojo.Page)
	 */
	@Override
	public ResponseBodyBean searchAlarm(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = alarmMapper.searchAlarmPgListPage(page);
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
	 * @Title: getalarmCategory1
	 * @discription 获取预警列表
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午8:01:17      
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#getalarmCategory1()
	 */
	@Override
	public List<PageData> getalarmCategory1() {
		return alarmMapper.getalarmCategory1();
	}

	/**
	 * @Title: getALarmByDossierId
	 * @discription 获取告警信息 根据卷宗id
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午2:11:53      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#getALarmByDossierId(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public List<PageData> getALarmByDossierId(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		List<PageData> pdList = new ArrayList<PageData>();
		try {
			pdList = alarmMapper.getALarmByDossierId(pd);
			resData.put("list", pdList);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return pdList;
	}

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:57:14      
	 * @param pd
	 * @return     
	 * @see cn.net.hlk.data.service.AlarmService#getAlarmInfomationById(cn.net.hlk.data.pojo.PageData)
	 */
	@Override
	public ResponseBodyBean getAlarmInfomationById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			data = alarmMapper.getAlarmById(pd);
			resData.put("data", data);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}
	
	
	
}
