package cn.net.hlk.data.service.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.net.hlk.data.mapper.AlarmMapper;
import cn.net.hlk.data.mapper.BacklogMapper;
import cn.net.hlk.data.mapper.InformationMaterialMapper;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.BacklogService;
import cn.net.hlk.data.service.InformationService;
import cn.net.hlk.data.service.RemindService;
import cn.net.hlk.util.ConfigUtil;
import cn.net.hlk.util.CustomConfigUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: InformationServiceImpl   
 * @Description:消息service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年10月13日 上午10:34:14
 */
@Service
public class InformationServiceImpl extends BaseServiceImple implements InformationService {

	@Autowired
	private AlarmService alarmService;
	@Autowired
	private BacklogService backlogService;
	@Autowired
	private RemindService remindService;
	@Autowired
	private InformationMaterialMapper informationMaterialMapper;
	@Autowired
	private AlarmMapper alarmMapper;
	@Autowired
	private BacklogMapper backlogMapper;

	
	/**
	 * @Title: alarmAndbacklogCreate
	 * @discription 待办 通知生成
	 * @author 张泽恒       
	 * @created 2018年10月13日 上午10:51:45      
	 * @param category 类别 0待办 1通知 
	 * @param type 类型 
	 * @param dossierid 卷宗id
	 * @param police 通知人
	 * @param updateuser 修改人
 	 * @return     
	 * @see InformationService#alarmAndbacklogCreate(int, int, int, String, PageData, String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean alarmAndbacklogCreate(int category, int type,
			String dossierid, PageData police, String updateuser) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData pd = new PageData();
			pd.put("updateuser", updateuser);//修改人
			pd.put("backlog_time", new Date());//时间
			pd.put("remind_time", new Date());//时间 
			pd.put("dossier_id",dossierid);//卷宗id
			
//			pd.put("backlog_state",state);//状态
			pd.put("backlog_type", type);//类型 
			pd.put("remind_type", type);//类型 
			pd.put("backlog_host", JSON.toJSONString(police));//通知人
			pd.put("reminding_people", JSON.toJSONString(police));//通知人
			//判断类型 确定操作
			if(0 == category){
				backlogService.addBacklog(pd);
			}else if(1 == category){
				remindService.addRemind(pd);
			}
			
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}
	
	/**
	 * @Title: alarmAndbacklogCreateAddAppId
	 * @discription 待办 通知生成
	 * @author 张泽恒       
	 * @created 2018年10月13日 上午10:51:45      
	 * @param category 类别 0待办 1通知 
	 * @param type 类型 
	 * @param dossierid 卷宗id
	 * @param police 通知人
	 * @param updateuser 修改人
	 * @return     
	 * @see InformationService#alarmAndbacklogCreate(int, int, int, String, PageData, String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean alarmAndbacklogCreateAddAppId(int category, int type,
			String dossierid, PageData police, String updateuser,String electronic_apply_id) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData pd = new PageData();
			pd.put("updateuser", updateuser);//修改人
			pd.put("backlog_time", new Date());//时间
			pd.put("remind_time", new Date());//时间 
			pd.put("dossier_id",dossierid);//卷宗id
			
			pd.put("electronic_apply_id",electronic_apply_id);//申请id
			pd.put("backlog_type", type);//类型 
			pd.put("remind_type", type);//类型 
			pd.put("backlog_host", JSON.toJSONString(police));//通知人
			pd.put("reminding_people", JSON.toJSONString(police));//通知人
			//判断类型 确定操作
			if(0 == category){
				backlogService.addBacklog(pd);
			}else if(1 == category){
				remindService.addRemind(pd);
			}
			
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: completeAlarmAndBacklog
	 * @discription 告警待办完成
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午1:31:36      
	 * @param pd
	 * @return     
	 * @see InformationService#completeAlarmAndBacklog(PageData)
	 */
	@Override
	public ResponseBodyBean completeAlarmAndBacklog(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			//告警信息获取
			pdList = alarmService.getALarmByDossierId(pd);
			if(pdList != null && pdList.size() > 0){
				logger.info("alarmList "+pdList.size());
				for(PageData alarmPd : pdList){
					PageData alarm = new PageData();
					alarm.put("alarm_id", alarmPd.getString("alarm_id"));
					alarm.put("alarm_state", 1);
					alarm.put("updateuser", pd.getString("updateuser"));
					alarm.put("alarm_processing_content", JSON.toJSONString(pd.get("alarm_processing_content")));
					if("need".equals(pd.get("dossier_message"))){
						PageData dossier_message = getDossierMessageById(pd);
						alarm.put("dossier_message", JSON.toJSONString(dossier_message));
					}
					alarmService.updateAlarm(alarm);
				}
			}
			
			//待办信息获取
			pdList = backlogService.getBacklogByDossierId(pd);
			if(pdList != null && pdList.size() > 0){
				logger.info("backlogList "+pdList.size());
				for(PageData backlogPd : pdList){
					PageData backlog = new PageData();
					backlog.put("backlog_id", backlogPd.getString("backlog_id"));
					backlog.put("backlog_state", 1);
					backlog.put("updateuser", pd.getString("updateuser"));
					
					backlogService.updateBacklog(backlog);
				}
			}
			
			
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: withdrawAlarmAndBacklog
	 * @discription 告警 待办撤回
	 * @author 张泽恒       
	 * @created 2018年10月15日 下午4:24:29      
	 * @param pd
	 * @return     
	 * @see InformationService#withdrawAlarmAndBacklog(PageData)
	 */
	@Override
	public ResponseBodyBean withdrawAlarmAndBacklog(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
//			//告警信息获取
//			pdList = alarmService.getALarmByDossierId(pd);
//			if(pdList != null && pdList.size() > 0){
//				logger.info("alarmList "+pdList.size());
//				PageData alarm = new PageData();
//				alarm.put("alarm_id", pdList.get(0).getString("alarm_id"));
//				alarm.put("alarm_state", 1);
//				alarm.put("updateuser", pd.getString("updateuser"));
//				
//				alarmService.updateAlarm(alarm);
//			}
			
			//待办信息获取
			pdList = backlogService.getBacklogByDossierId(pd);
			if(pdList != null && pdList.size() > 0){
				logger.info("backlogList "+pdList.size());
				for(PageData backlogPd : pdList){
					PageData backlog = new PageData();
					backlog.put("backlog_id", backlogPd.getString("backlog_id"));
//				backlog.put("backlog_state", 1);
					backlog.put("visibale", 0);
					backlog.put("updateuser", pd.getString("updateuser"));
					
					backlogService.updateBacklog(backlog);
				}
			}
			
			
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: xmppSend
	 * @discription xmpp消息发送
	 * @author 张泽恒       
	 * @created 2018年10月16日 上午9:59:33      
	 * @param pd
	 * @return     
	 * @see InformationService#xmppSend(PageData)
	 */
	@Override
	public int xmppSend(PageData data,int type) {
		String SmartlinkeyState = ConfigUtil.getString("Smartlinkey.server.state");
		if("true".equals(SmartlinkeyState)){
			try {
				//卷宗信息获取
				PageData dossier_message = JSON.parseObject(data.get("dossier_message").toString(),PageData.class);
				PageData dossier_belonger = JSON.parseObject(JSON.toJSONString(dossier_message.get("dossier_belonger")),PageData.class);
				PageData dossier_administrator = JSON.parseObject(JSON.toJSONString(dossier_message.get("dossier_administrator")),PageData.class);
				PageData electronic_apply_applicant = JSON.parseObject(JSON.toJSONString(dossier_message.get("electronic_apply_applicant")),PageData.class);
				PageData police = new PageData();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfsfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//数据配置获取
//			String dataModel = ConfigUtil.getString("spring.data.model");
				List<PageData> pdList = informationMaterialMapper.informationSearchGet(new PageData());
				PageData Smartlinkeymaterial = new PageData();
				if(pdList != null && pdList.size() > 0){
					for(PageData material:pdList){
						Smartlinkeymaterial.put(material.getString("code"), material.getString("message"));
					}
				}
				String url = ConfigUtil.getString("Smartlinkey.server.url");
				//创建数据获取字段
				String XXTBMsg = Smartlinkeymaterial.getString("xxtb");//信息来源图标 固定
				String XXTPMsg = Smartlinkeymaterial.getString("slt");//消息缩略图 固定
				
				String XXMCMsg = dossier_message.getString("dossier_name");//消息名称 卷宗名称
				String XXBJMsg = "";//类型图标 根据消息类型展示
				String XXZTMsg = "";//消息状态 消息类型
				String XXXS1Msg = "所属案件："+dossier_message.getString("case_name");//标签1
				String XXXS2Msg = "";//标签2
				String XXXS3Msg = "";//标签3
				String XXXS4Msg = "";//标签4
				String XXXS33Msg = "";//标签33
				String Btn1Act = "";//处理连接
				
				boolean flagg = true;
				//判断消息类型
				if(type == 0 ){
					//告警
					police = JSON.parseObject(data.get("warns").toString(),PageData.class);
					XXBJMsg = Smartlinkeymaterial.getString("gjtb");//类型图标 根据消息类型展示
					Integer alarm_type = Integer.valueOf(data.get("alarm_type").toString());
					String Itime = sdf.format((Date)data.get("alarm_time"));
					switch (alarm_type) {
					case 0:
						XXZTMsg = "超期送卷";//消息状态 消息类型
						XXXS2Msg = "借阅民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "卷宗保管员："+dossier_administrator.getString("police_name");//标签3
						XXXS4Msg = "告警时间："+Itime;//标签4
						break;
					case 1:
						XXZTMsg = "超期归还";//消息状态 消息类型
						XXXS2Msg = "送卷民警："+dossier_belonger.getString("police_name");//标签2
						XXXS4Msg = "告警时间："+Itime;//标签4
//						XXXS3Msg = "";//标签3
						break;
					case 3:
						XXZTMsg = "归还预警";//消息状态 消息类型
						XXXS2Msg = "送卷民警："+dossier_belonger.getString("police_name");//标签2
						XXXS4Msg = "预警时间："+Itime;//标签4
//						XXXS3Msg = "";//标签3
						break;
					default:
						XXZTMsg = alarm_type.toString();//消息状态 消息类型
						break;
					}
					url = url+"/#/user/loginBytoken?xxid="+data.getString("alarm_id")+"$type="+1+"$token=";
					Btn1Act = url;//处理连接 入库连接
					
				}else if(type == 1){
					//待办
					police = JSON.parseObject(data.get("backlog_host").toString(),PageData.class);
					XXBJMsg = Smartlinkeymaterial.getString("dbtb");//类型图标 根据消息类型展示
					Integer backlog_type = Integer.valueOf(data.get("backlog_type").toString());
					String Itime = sdf.format((Date)data.get("backlog_time"));
					switch (backlog_type) {
					case 0:
						XXZTMsg = "待入库";//消息状态 消息类型
						XXXS2Msg = "入库申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "卷宗保管员："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 保管员入库连接
						break;
					case 1:
						XXZTMsg = "待出库";//消息状态 消息类型
						XXXS2Msg = "出库申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "卷宗保管员："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 保管员出库连接
						break;
					case 2:
						XXZTMsg = "待送卷";//消息状态 消息类型
						XXXS2Msg = "送卷民警："+dossier_belonger.getString("police_name");//标签2
//						XXXS3Msg = "接收民警："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 民警入库连接
						break;
					case 3:
						XXZTMsg = "待审批";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "审核领导："+police.getString("police_name");//标签3
						XXXS33Msg = "审核类型：卷宗出库";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 监管审批连接
						break;
					case 4:
						XXZTMsg = "待审批";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "审核领导："+police.getString("police_name");//标签3
						XXXS33Msg = "审核类型：卷宗转交";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 监管审批连接
						break;
					case 5:
						XXZTMsg = "待归还";//消息状态 消息类型
						XXXS2Msg = "归还民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "卷宗保管员："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 民警入库连接
						break;
					case 6:
						XXZTMsg = "待签收";//消息状态 消息类型
						XXXS2Msg = "转交民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "接收民警："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 民警转交连接
						break;
					case 7:
						XXZTMsg = "待审批";//消息状态 消息类型
						XXXS2Msg = "申请民警："+electronic_apply_applicant.getString("police_name");//标签2
//						XXXS3Msg = "审核领导："+police.getString("police_name");//标签3
						XXXS3Msg = "审核类型：电子借阅";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 监管审批连接
						break;
					case 8:
						XXZTMsg = "待确认";//消息状态 消息类型
						XXXS2Msg = "借阅民警："+electronic_apply_applicant.getString("police_name");//标签2
						XXXS3Msg = "审核领导："+data.getString("updateuser");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 民警转交连接
						break;
					case 9:
						XXZTMsg = "待审阅";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
//						XXXS3Msg = "接收民警："+police.getString("police_name");//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
//						Btn1Act = "";//处理连接 民警转交连接
						break;
					default:
						XXZTMsg = backlog_type.toString();//消息状态 消息类型
						break;
					} 
					url = url+"/#/user/loginBytoken?xxid="+data.getString("backlog_id")+"$type="+2+"$token=";
					Btn1Act = url;//处理连接 民警转交连接
					
				}else if(type == 2){
					flagg = false;
					//通知
					police = JSON.parseObject(data.get("reminding_people").toString(),PageData.class);
					XXBJMsg = Smartlinkeymaterial.getString("tztb");//类型图标 根据消息类型展示
					Integer remind_type = Integer.valueOf(data.get("remind_type").toString());
					String Itime = sdf.format((Date)data.get("remind_time"));
					switch (remind_type) {
					case 0:
						XXZTMsg = "审批通过";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：出库";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 1:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "驳回民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：出库驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 2:
						XXZTMsg = "审批通过";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：移交";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 3:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "驳回民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：移交驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 4:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "驳回民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：出库驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 5:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "驳回民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：入库驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 6:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "驳回民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：转交驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 7:
						XXZTMsg = "审批通过";//消息状态 消息类型
						XXXS2Msg = "申请民警："+electronic_apply_applicant.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：电子借阅审批";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 8:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+electronic_apply_applicant.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：电子借阅审批驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 10:
						XXZTMsg = "审批通过";//消息状态 消息类型
						XXXS2Msg = "申请民警："+electronic_apply_applicant.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：电子借阅";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 9:
						XXZTMsg = "驳回";//消息状态 消息类型
						XXXS2Msg = "申请民警："+electronic_apply_applicant.getString("police_name");//标签2
						XXXS3Msg = "审核民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "审核类型：电子借阅驳回";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 11:
						XXZTMsg = "卷宗已阅";//消息状态 消息类型
						XXXS2Msg = "申请民警："+dossier_belonger.getString("police_name");//标签2
						XXXS3Msg = "审阅民警："+data.getString("updateuser");//标签3
						XXXS33Msg = "电子卷监管已阅";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 12:
						XXZTMsg = "页码不匹配";//消息状态 消息类型
						XXXS2Msg = "民警："+dossier_belonger.getString("police_name");//标签2
//						XXXS3Msg = "审阅民警："+dossier_message.getString("updateuser");//标签3
						XXXS3Msg = "页码不匹配";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					case 13:
						XXZTMsg = "案件状态不匹配";//消息状态 消息类型
						XXXS2Msg = "民警："+dossier_belonger.getString("police_name");//标签2
//						XXXS3Msg = "审阅民警："+dossier_message.getString("updateuser");//标签3
						XXXS3Msg = "案件状态不匹配";//标签3
						XXXS4Msg = "操作时间："+Itime;//标签4
						break;
					default:
						XXZTMsg = remind_type.toString();//消息状态 消息类型
						break;
					} 
					url = url+"/#/user/loginBytoken?xxid="+data.getString("remind_id")+"$type="+3+"$token=";
					Btn1Act = url;//处理连接
					
				}
				
				//############xmpp消息推送############
				//消息格式创建
				List<Object> list = new ArrayList<Object>();//消息list
				PageData xmppd = new PageData();//单条消息
				xmppd.put("read", 0);
				xmppd.put("read_m", 0);
				xmppd.put("active", 0);
				xmppd.put("nodeid", police.getString("police_idcard"));
				xmppd.put("systemid", "109006");
				xmppd.put("time", sdfsfm.format(new Date()));
				xmppd.put("id", UuidUtil.get32UUID());
				//显示消息来源系统图标
				PageData XXTB = new PageData();
				XXTB.put("type", 1);//类型 0标签 1图片 2 按钮
				XXTB.put("isvisible", true);//是否可见
				XXTB.put("msg", XXTBMsg);//显示数据
				XXTB.put("act", "");//触发动作
				XXTB.put("comment", "");//备注
				xmppd.put("xxtb", XXTB);
				//备注信息
				PageData XXBT = new PageData();
				XXBT.put("type", 0);//类型 0标签 1图片 2 按钮
				XXBT.put("isvisible", true);//是否可见
				XXBT.put("msg", "卷宗管理系统");//显示数据
				XXBT.put("act", "");//触发动作
				XXBT.put("comment", "");//备注
				xmppd.put("xxbt", XXBT);
				//收藏按钮 展示为类型
				PageData XXBJ = new PageData();
				XXBJ.put("type", 1);//类型 0标签 1图片 2 按钮
				XXBJ.put("isvisible", false);//是否可见
				XXBJ.put("msg", StringUtil2.isNotEmpty(XXBJMsg)?XXBJMsg:"");//显示数据
				XXBJ.put("actiontype", 1);////0收藏 1自定义
				XXBJ.put("act", "");//触发动作
				XXBJ.put("comment", "");//备注
//				XXBJ.put("id", "");//id
				xmppd.put("xxbj", XXBJ);
				//消息名称
				PageData XXMC = new PageData();
				XXMC.put("type", 0);//类型 0标签 1图片 2 按钮
				XXMC.put("isvisible", true);//是否可见
				XXMC.put("msg", XXMCMsg);//显示数据
				XXMC.put("act", "");//触发动作
				XXMC.put("comment", "");//备注
				xmppd.put("xxmc", XXMC);
				//消息状态
				PageData XXZT = new PageData();
				XXZT.put("type", 0);//类型 0标签 1图片 2 按钮
				XXZT.put("isvisible", true);//是否可见
				XXZT.put("msg", XXZTMsg);//显示数据
				XXZT.put("act", "");//触发动作
				XXZT.put("comment", "");//备注
				xmppd.put("xxzt", XXZT);
				//消息缩略图
				PageData XXTP = new PageData();
				XXTP.put("type", 1);//类型 0标签 1图片 2 按钮
				XXTP.put("isvisible", true);//是否可见
				XXTP.put("msg", "images/chatu1.png");//显示数据
				XXTP.put("act", "");//触发动作
				XXTP.put("comment", "");//备注
				xmppd.put("xxtp", XXTP);
				//标签信息
				List<PageData> XXXS_Ary = new ArrayList<PageData>();
				//所属案件
				PageData XXXS1 = new PageData();
				XXXS1.put("type", 0);//类型 0标签 1图片 2 按钮
				XXXS1.put("isvisible", true);//是否可见
				XXXS1.put("msg", XXXS1Msg);//显示数据
				XXXS1.put("act", "");//触发动作
				XXXS1.put("comment", "");//备注
				XXXS_Ary.add(XXXS1);
				//借阅民警 入库申请民警
				PageData XXXS2 = new PageData();
				XXXS2.put("type", 0);//类型 0标签 1图片 2 按钮
				XXXS2.put("isvisible", true);//是否可见
				XXXS2.put("msg", XXXS2Msg);//显示数据
				XXXS2.put("act", "");//触发动作
				XXXS2.put("comment", "");//备注
				XXXS_Ary.add(XXXS2);
				//保管员 驳回人信息
				PageData XXXS3 = new PageData();
				XXXS3.put("type", 0);//类型 0标签 1图片 2 按钮
				XXXS3.put("isvisible", true);//是否可见
				XXXS3.put("msg", XXXS3Msg);//显示数据
				XXXS3.put("act", "");//触发动作
				XXXS3.put("comment", "");//备注
				XXXS_Ary.add(XXXS3);
				//保管员 驳回人信息
				PageData XXXS33 = new PageData();
				XXXS33.put("type", 0);//类型 0标签 1图片 2 按钮
				XXXS33.put("isvisible", true);//是否可见
				XXXS33.put("msg", XXXS33Msg);//显示数据
				XXXS33.put("act", "");//触发动作
				XXXS33.put("comment", "");//备注
				XXXS_Ary.add(XXXS33);
				//告警时间 操作时间
				PageData XXXS4 = new PageData();
				XXXS4.put("type", 0);//类型 0标签 1图片 2 按钮
				XXXS4.put("isvisible", true);//是否可见
				XXXS4.put("msg", XXXS4Msg);//显示数据
				XXXS4.put("act", "");//触发动作
				XXXS4.put("Comment", "");//备注
				XXXS_Ary.add(XXXS4);
				xmppd.put("xxxs_ary", XXXS_Ary);
				//按钮信息
				List<PageData> Btn_Ary = new ArrayList<PageData>();
				//处理按钮
				PageData Btn1 = new PageData();
				Btn1.put("type", 2);//类型 0标签 1图片 2 按钮
				Btn1.put("isvisible", true);//是否可见
				if(flagg){
					Btn1.put("msg", "处理");//显示数据
				}else{
					Btn1.put("msg", "查看");//显示数据
				}
				Btn1.put("act", Btn1Act);//触发动作
				Btn1.put("comment", "");//备注
				Btn_Ary.add(Btn1);
				xmppd.put("btn_ary", Btn_Ary);
				list.add(xmppd);
				//获取本地数据
				/*logger.info("给xmpp推送的内容+++"+list);
				PageData xmppdata = new PageData();
				xmppdata.put("type", "109006");
				xmppdata.put("errormess", "");
				xmppdata.put("result", list);
				String messageSource = "109006";
				//推送消息内容,数据内容,主题ID,事件ID,消息来源
				boolean flag = true;
				logger.info("xmppSend = "+police.getString("police_idcard"));
				flag = xmppDataPullService.xmppDataPull(xmppdata, list,police.getString("police_idcard"),police.getString("police_idcard"), messageSource);
				if(flag){
					logger.info("+++入区xmpp推送成功");
				}else {
					logger.info("+++入区xmpp推送失败 "+police.getString("police_idcard"));
					return -3;
				}*/
				//新版smartLinked发送请求为post方式
				String smartLinkedUrl = CustomConfigUtil.getString("smartLinked.url");
				//############xmpp消息推送结束############
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * @Title: informationSearchGet
	 * @discription 查询条件接口
	 * @author 张泽恒       
	 * @created 2018年10月17日 下午7:15:02      
	 * @return     
	 * @see InformationService#informationSearchGet()
	 */
	@Override
	public ResponseBodyBean informationSearchGet() {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			PageData pd = new PageData();
			pd.put("code", "informationSearchGet");
			//待办信息获取
			pdList = informationMaterialMapper.informationSearchGet(pd);
			if(pdList != null && pdList.size() > 0){
				PageData list = JSON.parseObject(pdList.get(0).getString("message"),PageData.class);
				resData =  list; 
			}
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: getDossierMessageById
	 * @discription 根据卷宗id 获取卷宗信息
	 * @author 张泽恒       
	 * @created 2018年10月31日 下午7:54:45     
	 * @param pd
	 * @return
	 */
	public PageData getDossierMessageById(PageData pd){
		return null;
	}

	/**
	 * @Title: getInfomationById
	 * @discription 根据信息id 获取信息
	 * @author 张泽恒       
	 * @created 2018年11月1日 下午2:52:13      
	 * @param pd
	 * @return     
	 * @see InformationService#getInfomationById(PageData)
	 */
	@Override
	public ResponseBodyBean getInfomationById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			String type = pd.getString("type");
			PageData data = new PageData();
			if("0".equals(type)){
				pd.put("alarm_id", pd.getString("xxid"));
				data = alarmMapper.getAlarmById(pd);
				if(data != null){
					resData.put("state", data.get("alarm_state"));
				}else{
					resData.put("state", 0);
				}
			}else if("1".equals(type)){
				pd.put("backlog_id", pd.getString("xxid"));
				data = backlogMapper.getbacklogById(pd);
				if(data != null){
					resData.put("state", data.get("backlog_state"));
				}else{
					resData.put("state", 0);
				}
			}
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}
	
}
