package cn.net.hlk.data.service.serviceimpl;


import cn.net.hlk.data.mapper.JWTMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.resource.ResourceBean;
import cn.net.hlk.data.pojo.role.Role;
import cn.net.hlk.data.pojo.user.FeatureEnum;
import cn.net.hlk.data.pojo.user.FeatureMqtt;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.pojo.user.UserExcel;
import cn.net.hlk.data.service.IUserService;
import cn.net.hlk.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 【描 述】：<用户管理ServiceImple>
 * 【表 名】：<t_user>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月8日
 */
@Service
public class UserServiceImple extends BaseServiceImple implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JWTMapper jwtMapper;

	Gson gson = new Gson();
	
	/**
	 * 【描 述】：<查询用户方法>
	 * @param page
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#findAllUserlistPage(cn.net.hylink.data.domain.Page)
	 */
	@Override
	public List<PageData> findAllUserlistPage(Page page) {
		List<PageData> userlistPage = userMapper.findAllUserPgListPage(page);
		return userlistPage;
		
	}
	
	/**
	 * 【描 述】：<用户删除方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#deleteUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean deleteUser(PageData pd){
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		if(isContainsAdmin(pd)){
			responseBodyBean.setReason(new ReasonBean("fail", "警告：admin用户不能删除!"));
		}else{
			Integer deleteUser = null;
			pd.put("visibale",1);
			deleteUser = userMapper.editUser(pd);
			if(deleteUser > 0){
				responseBodyBean.setResult("success:删除"+deleteUser+"条");
			}else{
				responseBodyBean.setReason(new ReasonBean("fail", "删除失败!"));
			}
		}
		return responseBodyBean;
	}

	/**
	 * 【描 述】：<用户禁用方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#disableUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean disableUser(PageData pd) {
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		if(isContainsAdmin(pd)){
			responseBodyBean.setReason(new ReasonBean("fail", "警告：admin用户不能禁用!"));
		}else{
			Integer disableUser = null;
			pd.put("state",0);
			disableUser = userMapper.editUser(pd);
			if(disableUser > 0){
				responseBodyBean.setResult("success:禁用"+disableUser+"条");
			}else{
				responseBodyBean.setReason(new ReasonBean("fail", "禁用失败!"));
			}
		}
		return responseBodyBean ;
	}
	/**
	 * 【描 述】：<用户启用>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#ableUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public Integer ableUser(PageData pd) {
		Integer disableUser = 0;
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		if(isContainsAdmin(pd)){
			responseBodyBean.setReason(new ReasonBean("fail", "警告：admin用户不能禁用!"));
		}else{
			pd.put("state",0);
			disableUser = userMapper.editUser(pd);
		}
		return disableUser ;
	}


	/**
	 * 【描 述】：<添加用户方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#addUser(cn.net.hylink.data.domain.PageData)
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
	@Override
	public Integer addUser(PageData pd) {
		pd.put("id", UUID.randomUUID().toString());
		/* 插入用户信息 */
		pd.put("user_message",JSON.parseObject(JSON.toJSONString(pd.get("user_message"))));
		Integer addUser = userMapper.addUser(pd);
		logger.info("addUser:"+addUser);
		return addUser;
	}


	/**
	 * 【描 述】：<根据id查询用户>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#findById(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public PageData findById(PageData pd) {
		return userMapper.findById(pd);
	}

	/**
	 * 【描 述】：<修改用户>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#editUser(cn.net.hylink.data.domain.PageData)
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
	@Override
	public Integer editUser(PageData pd) {
		Integer editUser = userMapper.editUser(pd);
		return editUser;
	}


	/**
	 * 【描 述】：<是否包含admin用户>
	 * @param pd
	 * @return
	 */
	private boolean isContainsAdmin(PageData pd){
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) pd.get("ids");
		for(String id:ids){
			if("1".equals(id)){
				return true;
			}
		}
		return false;

	}

}
