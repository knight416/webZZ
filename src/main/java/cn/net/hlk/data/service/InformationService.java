package cn.net.hlk.data.service;

import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;

/**
 * @package: cn.net.hlk.data.service   
 * @Title: InformationService   
 * @Description:消息service
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年10月13日 上午10:33:35
 */
public interface InformationService {

	/**
	 * @Title: alarmAndbacklogCreate
	 * @discription 待办、通知生成
	 * @author 张泽恒       
	 * @created 2018年10月13日 上午10:50:21     
	 * @param category 类型别 0待办 1通知
	 * @param type 类型
	 * @param state 状态
	 * @param dossierid 卷宗id
 	 * @param police 通知人
	 * @param updateuser 修改人
	 * @return
	 */
	ResponseBodyBean alarmAndbacklogCreate(int category,int type,String dossierid,PageData police,String updateuser );

	/**
	 * @Title: completeAlarmAndBacklog
	 * @discription 告警待办完成
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午1:31:04     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean completeAlarmAndBacklog(PageData pd);

	/**
	 * @Title: withdrawAlarmAndBacklog
	 * @discription 告警 待办 撤回
	 * @author 张泽恒       
	 * @created 2018年10月15日 下午4:24:00     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean withdrawAlarmAndBacklog(PageData pd);

	/***
	 * @Title: xmppSend
	 * @discription xmpp消息发送
	 * @author 张泽恒       
	 * @created 2018年10月16日 上午9:59:04     
	 * @param pd
	 * @return
	 */
	int xmppSend(PageData pd,int type);

	/**
	 * @Title: informationSearchGet
	 * @discription 查询条件接口
	 * @author 张泽恒       
	 * @created 2018年10月17日 下午7:14:28     
	 * @return
	 */
	ResponseBodyBean informationSearchGet();

	/**
	 * @Title: getInfomationById
	 * @discription 根据信息id 获取信息
	 * @author 张泽恒       
	 * @created 2018年11月1日 下午2:51:49     
	 * @param pd
	 * @return
	 */
	ResponseBodyBean getInfomationById(PageData pd);

	/**
	 * @Title: alarmAndbacklogCreate
	 * @discription 待办、通知生成 加申请id
	 * @author 张泽恒       
	 * @created 2018年10月13日 上午10:50:21     
	 * @param category 类型别 0待办 1通知
	 * @param type 类型
	 * @param state 状态
	 * @param dossierid 卷宗id
 	 * @param police 通知人
	 * @param updateuser 修改人
	 * @return
	 */
	ResponseBodyBean alarmAndbacklogCreateAddAppId(int i, int j, String string,
			PageData approver, String string2, String electronic_apply_id);

}
