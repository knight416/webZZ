package cn.net.hlk.data.service.serviceimpl;

import cn.net.hlk.data.mapper.PostMapper;
import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.PostService;
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
 * @Description:岗位service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:46
 */
@Service
public class PostServiceImpl extends BaseServiceImple implements PostService {

	@Autowired
	private PostMapper postMapper;

	/**
	 * @Title: addAlarm
	 * @discription 岗位新增
	 * @author 张泽恒
	 * @created 2018年10月8日 上午10:10:29
	 * @param pd
	 * @return
	 * @see AlarmService#addAlarm(PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addPost(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//验证是否允许投递
			// if("004002".equals(pd.getString("operation_type"))){
			// 	int n = postMapper.VerificationAdd(pd);
			// 	if(n > 0){
			// 		reasonBean.setCode("400");
			// 		reasonBean.setText("此岗位已投递");
			// 		responseBodyBean.setReason(reasonBean);
			// 		return responseBodyBean;
			// 	}
			// }

			String post_id = UuidUtil.get32UUID();
			pd.put("post_id", post_id);//主键生成

			//根据时间类型 区分操作
			if(StringUtil2.isNotEmpty(pd.get("post_message"))){
				pd.put("post_message",JSON.toJSONString(pd.get("post_message")));
			}
			if(StringUtil2.isNotEmpty(pd.get("unit_information"))){
				pd.put("unit_information",JSON.toJSONString(pd.get("unit_information")));
			}
			postMapper.addPost(pd);//消息添加
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
	 * @discription 岗位修改
	 * @author 张泽恒
	 * @created 2018年10月8日 下午1:40:38
	 * @param pd
	 * @return
	 * @see AlarmService#updateAlarm(PageData)
	 */
	@Override
	public ResponseBodyBean updatePost(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			if(StringUtil2.isNotEmpty(pd.get("post_message"))){
				pd.put("post_message",JSON.toJSONString(pd.get("post_message")));
			}
			if(StringUtil2.isNotEmpty(pd.get("unit_information"))){
				pd.put("unit_information",JSON.toJSONString(pd.get("unit_information")));
			}
			postMapper.updatePost(pd);//消息修改
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
	 * @discription 岗位条件查询
	 * @author 张泽恒
	 * @created 2018年10月10日 下午3:15:14
	 * @param page
	 * @return
	 * @see AlarmService#searchAlarm(Page)
	 */
	@Override
	public ResponseBodyBean searchPost(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = postMapper.searchPostPgListPage(page);
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
	 * @discription 根据岗位id 获取岗位信息
	 * @author 张泽恒
	 * @created 2018年10月19日 上午8:57:14
	 * @param pd
	 * @return
	 * @see AlarmService#getAlarmInfomationById(PageData)
	 */
	@Override
	public ResponseBodyBean getPostById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			data = postMapper.getPostById(pd);
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
	 * @Title postImport
	 * @Description 岗位导入
	 * @author 张泽恒
	 * @date 2020/1/21 21:01
	 * @param [personList]
	 * @param s
	 * @param xid
	 * @param uid
	 * @param optName
	 * @return void
	 */
	@Override
	public void postImport(List<PostPojo> personList, String system_id, String xid, String uid, String optName) {
		try {
			if(personList != null && personList.size() > 0){
				for(PostPojo person : personList){
					PageData pd = new PageData();
					String post_id = UuidUtil.get32UUID();
					pd.put("post_id", post_id);
					pd.put("xid", xid);
					pd.put("uid", uid);
					pd.put("post_type", person.getPosttype());
					pd.put("post_state", 1);
					pd.put("state", 1);
					pd.put("system_id", system_id);
					pd.put("updateuser", optName);

					PageData post_message = new PageData();
					post_message.put("numberofrecruits",person.getNumberofrecruits());
					post_message.put("academicrequirements",person.getAcademicrequirements());
					post_message.put("professionalrequirements",person.getProfessionalrequirements());
					post_message.put("genderrequirements",person.getGenderrequirements());
					post_message.put("maximumage",person.getMaximumage());
					post_message.put("politicaloutlook",person.getPoliticaloutlook());
					post_message.put("contactinformation",person.getContactinformation());
					post_message.put("employmentnature",person.getEmploymentnature());
					post_message.put("otherrequirements",person.getOtherrequirements());
					post_message.put("remarks",person.getRemarks());

					PageData unit_information = new PageData();
					unit_information.put("name",person.getRecruitmentunit());

					pd.put("post_message",JSON.toJSONString(post_message));
					pd.put("unit_information",JSON.toJSONString(unit_information));
					postMapper.addPost(pd);//消息添加
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}