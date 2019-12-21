package cn.net.hlk.data.service.serviceimpl;

import cn.net.hlk.data.mapper.AlarmMapper;
import cn.net.hlk.data.mapper.NewsMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.NewsService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: AlarmServiceImpl   
 * @Description:消息service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:46
 */
@Service
public class NewsServiceImpl extends BaseServiceImple implements NewsService {

	@Autowired
	private NewsMapper newsMapper;

	/**
	 * @Title: addAlarm
	 * @discription 消息新增
	 * @author 张泽恒
	 * @created 2018年10月8日 上午10:10:29
	 * @param pd
	 * @return
	 * @see AlarmService#addAlarm(PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addNews(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			if(StringUtil2.isEmpty(pd.get("news_message"))){
			}
			pd.put("xid", UuidUtil.get32UUID());//主键生成
			pd.put("news_message",JSON.toJSONString(pd.get("news_message")));
			newsMapper.addNews(pd);//消息添加
			// int n = informationService.xmppSend(pd,0);
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
	 * @see AlarmService#updateAlarm(PageData)
	 */
	@Override
	public ResponseBodyBean updateNews(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			pd.put("news_message",JSON.toJSONString(pd.get("news_message")));
			newsMapper.updateNews(pd);//消息修改
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
	 * @see AlarmService#searchAlarm(Page)
	 */
	@Override
	public ResponseBodyBean searchNews(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = newsMapper.searchNewsPgListPage(page);
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
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒
	 * @created 2018年10月19日 上午8:57:14
	 * @param pd
	 * @return
	 * @see AlarmService#getAlarmInfomationById(PageData)
	 */
	@Override
	public ResponseBodyBean getNewsById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			data = newsMapper.getNewsById(pd);
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
