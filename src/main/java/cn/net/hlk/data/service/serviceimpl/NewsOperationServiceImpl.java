package cn.net.hlk.data.service.serviceimpl;

import cn.net.hlk.data.mapper.NewsMapper;
import cn.net.hlk.data.mapper.NewsOperationMapper;
import cn.net.hlk.data.mapper.PostMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.poi.easypoi.ScorePojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.NewsOperationService;
import cn.net.hlk.data.service.NewsService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class NewsOperationServiceImpl extends BaseServiceImple implements NewsOperationService {

	@Autowired
	private NewsOperationMapper newsOperationMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private NewsMapper newsMapper;
	@Autowired
	private PostMapper postMapper;

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
	public ResponseBodyBean addNewsOperation(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//验证是否允许投递
			if("004002".equals(pd.getString("operation_type")) || "004001".equals(pd.getString("operation_type"))){
				if("004002".equals(pd.getString("operation_type")) ){
					int n = newsOperationMapper.VerificationAdd(pd);
					if(n > 0){
						reasonBean.setCode("400");
						reasonBean.setText("此岗位已投递");
						responseBodyBean.setReason(reasonBean);
						return responseBodyBean;
					}
				}


				if("004001".equals(pd.getString("operation_type")) ){
					PageData user = userMapper.findById(pd);
					String photo = "";
					if(user != null){
						photo = user.getString("photo");
					}
					if(StringUtil2.isEmpty(photo) || "null".equals(photo)){
						reasonBean.setCode("400");
						reasonBean.setText("照片未上传");
						responseBodyBean.setReason(reasonBean);
						return responseBodyBean;
					}

					PageData news = newsMapper.getNewsById(pd);
					if(news != null){
						PageData newsMessage = JSON.parseObject(JSON.toJSONString(news.get("news_message")),PageData.class);
						if(newsMessage != null){

							PageData pdd = new PageData();
							pdd.put("xid",pd.get("xid"));
							pdd.put("uid",pd.get("uid"));
							pdd.put("operation_type","004001");
							int nn = newsOperationMapper.VerificationAdd(pdd);

							String operation_number = newsMessage.getString("operation_number");
							if("one".equals(operation_number) && nn > 0){
								reasonBean.setCode("400");
								reasonBean.setText("公告报考次数已达上限");
								responseBodyBean.setReason(reasonBean);
								return responseBodyBean;
							}

							PageData pddd = new PageData();
							pddd.put("xid",pd.get("xid"));
							pddd.put("uid",pd.get("uid"));
							pddd.put("operation_type","004001");
							pddd.put("post_id1",pd.get("post_id"));
							int nnn = newsOperationMapper.VerificationAdd(pddd);

							if(nnn > 0){
								reasonBean.setCode("400");
								reasonBean.setText("已报考");
								responseBodyBean.setReason(reasonBean);
								return responseBodyBean;
							}


							List<String> registrationTime = JSON.parseArray(JSON.toJSONString(newsMessage.get("registrationTime")),String.class);
							if(registrationTime != null && registrationTime.size() > 1){
								String registrationTimes = registrationTime.get(0);
								String registrationTimee = registrationTime.get(1);

								Date dates = null;
								Date datee = null;
								registrationTimes = registrationTimes.replace("Z", " UTC");
								registrationTimee = registrationTimee.replace("Z", " UTC");
								SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
								dates = formatter.parse(registrationTimes);
								datee = formatter.parse(registrationTimee);
								Date now =new Date();
								if(dates.getTime() > now.getTime() || datee.getTime() < now.getTime()){
									reasonBean.setCode("400");
									reasonBean.setText("报名时间未匹配");
									responseBodyBean.setReason(reasonBean);
									return responseBodyBean;
								}
							}
						}
					}
				}
			}

			//根据时间类型 区分操作
			if(StringUtil2.isNotEmpty(pd.get("operation_message"))){
				pd.put("operation_message",JSON.toJSONString(pd.get("operation_message")));
			}
			PageData interview_message = new PageData();
			String ticketNumber = UuidUtil.get32UUID();
			interview_message.put("ticketnumber",ticketNumber);
			pd.put("interview_message",JSON.toJSONString(interview_message));

			newsOperationMapper.addNewsOperation(pd);//消息添加
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
	public ResponseBodyBean updateNewsOperation(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//state为1时 判断是否考试同意
			if(Integer.valueOf(pd.get("state").toString()) == 1){
				PageData pdd = new PageData();
				pdd.put("xid",pd.get("xid"));
				pdd.put("post_id",pd.get("post_id"));
				pdd.put("uid",pd.get("uid"));
				pdd.put("operation_type","004001");
				PageData newsOperation = newsOperationMapper.getNewsOperationById(pdd);
				if(newsOperation != null){
					//处理准考证号
					PageData interview_message = JSON.parseObject(JSON.toJSONString(newsOperation.get("interview_message")),PageData.class);

					PageData pdn = new PageData();
					pdn.put("state",1);
					pdn.put("operation_type","004001");
					int n = newsOperationMapper.getCount(pdn);

					String ticketNumber = "3501"+(2000000+n+1);
					interview_message.put("ticketnumber",ticketNumber);
					pd.put("interview_message",JSON.toJSONString(interview_message));
				}
			}

			//根据时间类型 区分操作
			if(StringUtil2.isNotEmpty(pd.get("operation_message"))){
				pd.put("operation_message",JSON.toJSONString(pd.get("operation_message")));
			}
			newsOperationMapper.updateNewsOperation(pd);//消息修改
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
	public ResponseBodyBean searchNewsOperation(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = newsOperationMapper.searchNewsOperationPgListPage(page);
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
	public ResponseBodyBean getNewsOperationById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			data = newsOperationMapper.getNewsOperationById(pd);
			resData.put("data", data);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title achievementIntroduction
	 * @Description 成绩导入
	 * @author 张泽恒
	 * @date 2020/1/22 9:54
	 * @param [personList, uid, optName]
	 * @return void
	 */
	@Override
	public void achievementIntroduction(List<ScorePojo> personList, String uid, String optName) {
		try {
			if(personList != null && personList.size() > 0){
				for(ScorePojo person : personList){
					PageData pd = new PageData();
					pd.put("uid", uid);
					pd.put("uid", person.getUid());
					pd.put("post_id", person.getPost_id());
					pd.put("written_results", person.getWrittenresults());
					pd.put("interview_results", person.getInterviewresults());
					newsOperationMapper.updateNewsOperation(pd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Title postPerformance
	 * @Description 岗位成绩前三获取
	 * @author 张泽恒
	 * @date 2020/1/29 21:23
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@Override
	public ResponseBodyBean postPerformance(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			// PageData data = new PageData();
			//生成前三成绩
			List<PageData> data = newsOperationMapper.postPerformance(pd);
			//修改前三成绩状态
			pd.put("isinterview",1);
			newsOperationMapper.updateNewsOperation(pd);
			newsOperationMapper.updatePostPerformance(pd);
			resData.put("data", data);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title updateInterviewPermit
	 * @Description 修改面试准考证
	 * @author 张泽恒
	 * @date 2020/1/29 22:27
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@Override
	public ResponseBodyBean updateInterviewPermit(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			List<PageData> newsInterviewMessageList = JSON.parseArray(JSON.toJSONString(pd.get("interviewMessageList")),PageData.class);
			if(newsInterviewMessageList != null && newsInterviewMessageList.size() > 0){
				for(PageData newsInterviewMessage : newsInterviewMessageList){
					if(StringUtil2.isNotEmpty(newsInterviewMessage.get("interview_message"))){
						newsInterviewMessage.put("interview_message",JSON.toJSONString(newsInterviewMessage.get("interview_message")));
					}
					newsOperationMapper.updatePostPerformance(newsInterviewMessage);
				}
			}
			PageData pdd = new PageData();
			pdd.put("post_id",pd.get("post_id"));
			pdd.put("post_state",2);
			postMapper.updatePost(pdd);
			resData.put("data", data);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title findicket
	 * @Description 获取准考证信息
	 * @author 张泽恒
	 * @date 2020/1/29 22:56
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	@Override
	public PageData findicket(PageData pd) {
		return newsOperationMapper.findicket(pd);
	}


}
